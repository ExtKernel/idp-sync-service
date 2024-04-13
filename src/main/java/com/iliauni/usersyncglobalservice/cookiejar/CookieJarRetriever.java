package com.iliauni.usersyncglobalservice.cookiejar;

public interface CookieJarRetriever<T> {
    String retrieveCookieJar(T client);
}
