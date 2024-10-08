package com.iliauni.idpsyncservice.ipa;

import com.iliauni.idpsyncservice.exception.ClientHasNoFqdnOrIpAndPortException;
import com.iliauni.idpsyncservice.idp.IdpRequestBuilder;
import com.iliauni.idpsyncservice.model.IpaClient;
import com.iliauni.idpsyncservice.service.CookieClientService;
import com.iliauni.idpsyncservice.service.IpaClientService;
import lombok.extern.slf4j.Slf4j;
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
 * A component class implementing the {@link IdpRequestBuilder} interface
 * for building HTTP request entities and API base URLs specific to FreeIPA (Identity, Policy, and Audit) IDP systems.
 */
@Slf4j
@Component
public class IpaIdpRequestBuilder implements IdpRequestBuilder<IpaClient> {
    CookieClientService<IpaClient> clientService;

    @Value("${ipaApiAuthEndpoint}")
    private String ipaAuthEndpoint;

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
    public HttpEntity<String> buildHttpRequestEntity(
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
                null,
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
                + ipaAuthEndpoint;
    }

    @Override
    public RestTemplate getRestTemplate(IpaClient client) {
        try {
            // Create a RestTemplate with the custom HttpClient, interceptor and error handler
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(buildHttpClientWithIpaCert(client.getCertPath()));
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            restTemplate.setInterceptors(Collections.singletonList(new StatefulRestTemplateInterceptor()));

            return restTemplate;
        } catch (
                NoSuchAlgorithmException |
                KeyStoreException |
                KeyManagementException |
                CertificateException |
                IOException exception) {
            log.error(
                    "RestTemplate wasn't built for the FreeIPA client with id "
                            + client.getId()
                            + " . Most likely, due to an invalid certificate path"
            );
            return null; // bc IDK how to handle this
        }
    }

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
    private HttpClient buildHttpClientWithIpaCert(String ipaCertPath) throws
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

        if (clientFqdn != null) {
            return clientFqdn;
        } else {
            throw new ClientHasNoFqdnOrIpAndPortException(
                    "FreeIPA client with ID "
                            + client.getId()
                            + " has no FQDN"
            );
        }
    }
}
