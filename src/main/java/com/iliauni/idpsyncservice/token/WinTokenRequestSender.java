package com.iliauni.idpsyncservice.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.idpsyncservice.model.WinClient;
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
