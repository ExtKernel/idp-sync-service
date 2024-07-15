package com.iliauni.idpsyncservice.kc;

import com.iliauni.idpsyncservice.idp.*;
import com.iliauni.idpsyncservice.model.SyncKcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class SyncKcIdpUserManager extends GenericIdpUserManager<SyncKcClient> {

    @Autowired
    public SyncKcIdpUserManager(
            @Qualifier("syncKcIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            IdpUserRequestSender<SyncKcClient> requestSender,
            @Lazy IdpModelExistenceValidator<SyncKcClient> modelExistenceValidator,
            UserIdpRequestSenderResultBlackListFilter<SyncKcClient> blackListFilter
    ) {
        super(
                jsonObjectMapper,
                requestSender,
                modelExistenceValidator,
                blackListFilter
        );
    }
}
