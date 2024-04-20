package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.model.CookieClient;
import com.iliauni.usersyncglobalservice.model.Cookie;

public interface CookieClientService<T extends CookieClient> extends ClientService<T> {
    Cookie generateAndSaveCookieJar(
            String clientId,
            String endpointUrl
    );
}
