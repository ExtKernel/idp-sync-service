package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.Oauth2Client;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;

public interface TokenRequestBuilder<T extends Oauth2Client> {
    HttpEntity<MultiValueMap<String, String>> buildHttpRequestEntityWithPasswordGrantType(T client);
    HttpEntity<MultiValueMap<String, String>> buildHttpRequestEntityWithRefreshTokenGrantType(
            T client,
            String refreshToken
    );
}
