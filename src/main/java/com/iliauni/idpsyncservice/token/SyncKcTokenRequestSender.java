package com.iliauni.idpsyncservice.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.idpsyncservice.model.SyncKcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncKcTokenRequestSender extends GenericTokenRequestSender<SyncKcClient> {

    @Autowired
    public SyncKcTokenRequestSender(
            ObjectMapper objectMapper,
            TokenRequestBuilder<SyncKcClient> requestBuilder
    ) {
        super(objectMapper, requestBuilder);
    }
}
