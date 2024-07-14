package com.iliauni.usersyncglobalservice.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.model.SyncKcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncKcTokenRequestBuilder extends GenericKcTokenRequestBuilder<SyncKcClient> {

    @Autowired
    public SyncKcTokenRequestBuilder(ObjectMapper objectMapper) {
        super(objectMapper);
    }
}
