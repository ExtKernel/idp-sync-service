package com.iliauni.usersyncglobalservice.cookiejar;

import com.iliauni.usersyncglobalservice.model.IpaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IpaCookieJarRetriever<T extends IpaClient> implements CookieJarRetriever<T> {
    CookieJarRequestSender<T> requestSender;

    @Autowired
    public IpaCookieJarRetriever(CookieJarRequestSender<T> requestSender) {
        this.requestSender = requestSender;
    }

    @Override
    public String retrieveCookieJar(T client) {
        return requestSender.getCookie(client);
    }
}
