package com.iliauni.idpsyncservice.token;

import com.iliauni.idpsyncservice.model.Oauth2Client;
import com.iliauni.idpsyncservice.model.RefreshToken;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
