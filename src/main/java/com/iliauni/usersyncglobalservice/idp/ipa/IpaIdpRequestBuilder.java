package com.iliauni.usersyncglobalservice.idp.ipa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iliauni.usersyncglobalservice.exception.ClientHasNoFqdnOrIpOrPortException;
import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.idp.IdpRequestBuilder;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.service.CookieClientService;
import com.iliauni.usersyncglobalservice.service.IpaClientService;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;

/**
 * A component class implementing the {@link IdpRequestBuilder} interface for building HTTP request entities and API base URLs specific to FreeIPA (Identity, Policy, and Audit) IDP systems.
 */
@Component
public class IpaIdpRequestBuilder implements IdpRequestBuilder<IpaClient> {
    CookieClientService<IpaClient> clientService;

    @Value("${ipaCertPath}")
    private String ipaCertPath;

    /**
     * Constructs an {@code IpaIdpRequestBuilder} instance with the specified {@link IpaClientService}.
     *
     * @param cookieClientService the service for FreeIPA clients
     */
    @Autowired
    public IpaIdpRequestBuilder(CookieClientService<IpaClient> cookieClientService) {
        this.clientService = cookieClientService;
    }

    @Override
    public HttpEntity buildHttpRequestEntity(
            String clientId,
            String requestBody,
            String authEndpointUrl
    ) {
        return new HttpEntity<>(
                requestBody,
                buildHeaders(
                        clientService.generateAndSaveCookieJar(
                                clientId,
                                authEndpointUrl
                        ).getCookie(),
                        clientService.findById(clientId).getFqdn()
                )
        );
    }

    @Override
    public HttpEntity buildAuthOnlyHttpRequestEntity(
            String clientId,
            String authEndpointUrl
    ) {
        return new HttpEntity<>(
                buildEmptyRequestBody(),
                buildHeaders(
                        clientService.generateAndSaveCookieJar(
                                clientId,
                                authEndpointUrl
                        ).getCookie(),
                        clientService.findById(clientId).getFqdn()
                )
        );
    }

    @Override
    public String buildRequestUrl(
            IpaClient client,
            String protocol,
            String endpoint
    ) {
        return protocol + "://" + getClientHostUrl(client) + endpoint;
    }

    @Override
    public String buildAuthRequestUrl(
            IpaClient client,
            String protocol
    ) {
        return protocol + "://"
                + getClientHostUrl(client)
                + "/ipa/session/login_password";
    }

    @Override
    public RestTemplate getRestTemplate() {
        try {
            // Create a RestTemplate with the custom HttpClient, interceptor and error handler
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(buildHttpClientWithIpaCert());
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            restTemplate.setInterceptors(Collections.singletonList(new StatefulRestTemplateInterceptor()));
            restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());

            return restTemplate;
        } catch (
                NoSuchAlgorithmException |
                KeyStoreException |
                KeyManagementException |
                CertificateException |
                IOException exception) {
            return null; // bc idk how to handle this shit. Anyway, a problem of future me
        }    }

    /**
     * Builds the HTTP headers for the request with the given cookie.
     *
     * @param cookie the cookie to include in the headers
     * @return the constructed HTTP headers
     */
    private HttpHeaders buildHeaders(
            String cookie,
            String ipaFqdn
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        headers.add("Referer", "https://" + ipaFqdn + "/ipa");
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    /**
     * Builds an HttpClient with custom SSL configuration using the FreeIPA certificate.
     *
     * @return the configured HttpClient
     */
    private HttpClient buildHttpClientWithIpaCert() throws
            NoSuchAlgorithmException,
            KeyStoreException,
            KeyManagementException,
            CertificateException,
            IOException {
        // Load the custom certificate
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream inputStream = new FileInputStream(ipaCertPath);
        X509Certificate caCert = (X509Certificate) cf.generateCertificate(inputStream);
        inputStream.close();

        // Create a KeyStore containing the custom certificate
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("custom", caCert);

        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(null, new TrustAllStrategy())
                .build();
        SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslcontext)
                .build();
        HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslSocketFactory)
                .build();
        return HttpClients.custom()
                .setConnectionManager(cm)
                .evictExpiredConnections()
                .build();
    }

    /**
     * Interceptor class to handle stateful behavior with RestTemplate.
     */
    private class StatefulRestTemplateInterceptor implements ClientHttpRequestInterceptor {
        private String cookie;

        /**
         * Intercepts the HTTP request to add the cookie if available.
         */
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            if (cookie != null) {
                request.getHeaders().add(HttpHeaders.COOKIE, cookie);
            }
            ClientHttpResponse response = execution.execute(request, body);

            if (cookie == null) {
                cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            }
            return response;
        }
    }

    private String getClientHostUrl(IpaClient client) {
        String clientFqdn = client.getFqdn();
        String clientIp = client.getIp();
        String clientPort = client.getPort();

        if (clientFqdn != null & clientPort != null) {
            return mergeHostWithPort(clientFqdn, clientPort);
        } else if (clientIp != null & clientPort != null) {
            return mergeHostWithPort(clientIp, clientPort);
        } else {
            throw new ClientHasNoFqdnOrIpOrPortException(
                    "Keycloak client with ID "
                            + client.getId()
                            + " has no FQDN, IP or port"
            );
        }
    }

    private String mergeHostWithPort(
            String host,
            String port
    ) {
        return host + ":" + port;
    }

    private ObjectNode buildEmptyRequestBody() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createObjectNode();
    }
}
