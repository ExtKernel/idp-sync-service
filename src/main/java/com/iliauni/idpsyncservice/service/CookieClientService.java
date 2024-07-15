package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.model.Cookie;
import com.iliauni.idpsyncservice.model.CookieClient;

public interface CookieClientService<T extends CookieClient> extends ClientService<T> {
    Cookie generateAndSaveCookieJar(
            String clientId,
            String endpointUrl
    );
}
