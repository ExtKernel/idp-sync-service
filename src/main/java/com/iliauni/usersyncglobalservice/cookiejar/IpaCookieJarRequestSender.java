package com.iliauni.usersyncglobalservice.cookiejar;

import com.iliauni.usersyncglobalservice.model.IpaClient;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IpaCookieJarRequestSender<T extends IpaClient> implements CookieJarRequestSender<T> {
    private final CookieJarRequestBuilder<T> requestBuilder;

    @Value("${ipaHostname}")
    private String ipaHostname;

    @Value("${ipaApiAuthEndpoint}")
    private String ipaApiEndpoint;

    @Autowired
    public IpaCookieJarRequestSender(
            CookieJarRequestBuilder<T> requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    @Override
    public String getCookie(T client) {
        ResponseEntity<Map> response = requestBuilder.getRestTemplate().exchange(
                requestBuilder.buildApiBaseUrl(ipaHostname, ipaApiEndpoint),
                HttpMethod.POST,
                requestBuilder.buildHttpRequestEntity(client),
                Map.class);

        return response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }
}
