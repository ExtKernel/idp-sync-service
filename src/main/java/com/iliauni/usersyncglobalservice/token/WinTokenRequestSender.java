package com.iliauni.usersyncglobalservice.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.model.WinClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WinTokenRequestSender extends GenericTokenRequestSender<WinClient> {

    @Autowired
    public WinTokenRequestSender(
            ObjectMapper objectMapper,
            TokenRequestBuilder<WinClient> requestBuilder
    ) {
        super(objectMapper, requestBuilder);
    }
}
