package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public interface TokenRequestBuilder<T extends Oauth2Client> {
    HttpEntity buildHttpRequestEntity(Map<String, String> requestBody);
    MultiValueMap buildPasswordRequestBody(T client);
    MultiValueMap buildRefreshTokenRequestBody(
            T client,
            RefreshToken refreshToken
    );

    /**
     * Get a custom {@link RestTemplate} object, tailored for the specific needs.
     *
     * @return the custom {@link RestTemplate}.
     */
    RestTemplate getRestTemplate();
}
