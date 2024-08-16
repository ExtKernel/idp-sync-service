package com.iliauni.idpsyncservice.kc;

import com.iliauni.idpsyncservice.idp.GenericIdpUserManager;
import com.iliauni.idpsyncservice.idp.IdpJsonObjectMapper;
import com.iliauni.idpsyncservice.idp.IdpModelExistenceValidator;
import com.iliauni.idpsyncservice.idp.IdpUserRequestSender;
import com.iliauni.idpsyncservice.idp.UserIdpRequestSenderResultBlackListFilter;
import com.iliauni.idpsyncservice.model.SyncKcClient;
import com.iliauni.idpsyncservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class SyncKcIdpUserManager extends GenericIdpUserManager<SyncKcClient> {

    @Autowired
    public SyncKcIdpUserManager(
            ClientService<SyncKcClient> clientService,
            @Qualifier("syncKcIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            IdpUserRequestSender<SyncKcClient> requestSender,
            @Lazy IdpModelExistenceValidator<SyncKcClient> modelExistenceValidator,
            UserIdpRequestSenderResultBlackListFilter<SyncKcClient> blackListFilter
    ) {
        super(
                clientService,
                jsonObjectMapper,
                requestSender,
                modelExistenceValidator,
                blackListFilter
        );
    }
}
