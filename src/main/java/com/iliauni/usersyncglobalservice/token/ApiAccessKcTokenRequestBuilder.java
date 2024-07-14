package com.iliauni.usersyncglobalservice.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.model.ApiAccessKcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiAccessKcTokenRequestBuilder extends GenericKcTokenRequestBuilder<ApiAccessKcClient> {

    @Autowired
    public ApiAccessKcTokenRequestBuilder(ObjectMapper objectMapper) {
        super(objectMapper);
    }
}
