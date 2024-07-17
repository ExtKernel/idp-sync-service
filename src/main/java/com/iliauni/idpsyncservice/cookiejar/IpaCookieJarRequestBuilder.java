package com.iliauni.idpsyncservice.cookiejar;

import com.iliauni.idpsyncservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.idpsyncservice.model.IpaClient;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
 * A component class implementing the {@link CookieJarRequestBuilder} interface for building HTTP request entities and API base URLs specific to FreeIPA cookie jar systems.
 *
 * @param <T> a type of FreeIPA client used for the request
 */
@Component
public class IpaCookieJarRequestBuilder<T extends IpaClient> implements CookieJarRequestBuilder<T> {

    /**
     * @inheritDoc
     * Builds the HTTP request entity with the client principal's credentials.
     */
    @Override
    public HttpEntity<MultiValueMap<String, String>> buildHttpRequestEntity(T client) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        requestBody.add("user", client.getPrincipalUsername());
        requestBody.add("password", client.getPrincipalPassword());

        if (client.getIp() != null && client.getPort() != null) {
            return new HttpEntity<>(requestBody, setHeaders(client.getIp()));
        } else {
            return new HttpEntity<>(requestBody, setHeaders(client.getFqdn()));
        }
    }

    /**
     * Sets the HTTP headers with cookie included for the request.
     *
     * @return the constructed HTTP headers
     */
    private HttpHeaders setHeaders(String ipaHostname) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));
        headers.set("referer", "https://" + ipaHostname + "/ipa");

        return headers;
    }

    /**
     * @inheritDoc
     * Retrieves a configured RestTemplate instance with custom HttpClient, interceptor, and error handler.
     */
    @Override
    public RestTemplate getRestTemplate(IpaClient client) {
        try {
            // Create a RestTemplate with the custom HttpClient, interceptor and error handler
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(buildHttpClientWithIpaCert(client.getCertPath()));
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
        }
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
}
