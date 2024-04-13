package com.iliauni.usersyncglobalservice.cookiejar;

import com.iliauni.usersyncglobalservice.exception.RestTemplateResponseErrorHandler;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class IpaCookieJarRequestSender<T extends IpaClient> implements CookieJarRequestSender<T> {
    private final CookieJarRequestBuilder<T> requestBuilder;
    private final RestTemplate restTemplate;
    
    @Value("${ipaApiUrl}")
    private String ipaApiUrl;

    @Autowired
    public IpaCookieJarRequestSender(
            CookieJarRequestBuilder<T> requestBuilder,
            RestTemplateBuilder restTemplateBuilder) {
        this.requestBuilder = requestBuilder;
        this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    @Override
    public String getCookie(T client) {
        ResponseEntity<Map> response = restTemplate.exchange(
                ipaApiUrl,
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client),
                Map.class);

        return response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }
}
