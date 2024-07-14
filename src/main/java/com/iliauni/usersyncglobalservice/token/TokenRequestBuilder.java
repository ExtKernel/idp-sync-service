package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import com.iliauni.usersyncglobalservice.model.RefreshToken;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public interface TokenRequestBuilder<T extends Oauth2Client> {
    HttpEntity buildHttpRequestEntity(String requestBody);
    String buildPasswordRequestBody(T client);
    String buildRefreshTokenRequestBody(
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
