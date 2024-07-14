package com.iliauni.usersyncglobalservice.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.model.ApiAccessKcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiAccessKcTokenRequestSender extends GenericTokenRequestSender<ApiAccessKcClient> {

    @Autowired
    public ApiAccessKcTokenRequestSender(
            ObjectMapper objectMapper,
            TokenRequestBuilder<ApiAccessKcClient> requestBuilder
    ) {
        super(objectMapper, requestBuilder);
    }
}
