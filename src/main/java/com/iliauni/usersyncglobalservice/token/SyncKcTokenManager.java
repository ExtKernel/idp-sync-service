package com.iliauni.usersyncglobalservice.token;

import com.iliauni.usersyncglobalservice.model.SyncKcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SyncKcTokenManager extends GenericTokenManager<SyncKcClient> {

    @Autowired
    public SyncKcTokenManager(
            @Qualifier("syncKcTokenJsonObjectMapper") TokenJsonObjectMapper jsonObjectMapper,
            TokenRequestSender<SyncKcClient> requestSender
    ) {
        super(jsonObjectMapper, requestSender);
    }
}
