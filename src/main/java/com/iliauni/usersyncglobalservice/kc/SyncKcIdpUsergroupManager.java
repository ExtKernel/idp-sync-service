package com.iliauni.usersyncglobalservice.kc;

import com.iliauni.usersyncglobalservice.idp.*;
import com.iliauni.usersyncglobalservice.model.SyncKcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SyncKcIdpUsergroupManager extends GenericIdpUsergroupManager<SyncKcClient> {

    @Autowired
    public SyncKcIdpUsergroupManager(
            @Qualifier("syncKcIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            IdpUsergroupRequestSender<SyncKcClient> requestSender,
            IdpModelExistenceValidator<SyncKcClient> modelExistenceValidator,
            UsergroupIdpRequestSenderResultBlackListFilter<SyncKcClient> blackListFilter
    ) {
        super(
                jsonObjectMapper,
                requestSender,
                modelExistenceValidator,
                blackListFilter
        );
    }
}
