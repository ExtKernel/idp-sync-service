package com.iliauni.usersyncglobalservice.cookiejar;

import com.iliauni.usersyncglobalservice.model.IpaClient;

public interface CookieJarRequestSender<T extends IpaClient> {
    public String getCookie(T client);
}
