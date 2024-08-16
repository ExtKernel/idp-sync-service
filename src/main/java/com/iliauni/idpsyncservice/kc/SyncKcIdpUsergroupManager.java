package com.iliauni.idpsyncservice.kc;

import com.iliauni.idpsyncservice.idp.GenericIdpUsergroupManager;
import com.iliauni.idpsyncservice.idp.IdpJsonObjectMapper;
import com.iliauni.idpsyncservice.idp.IdpModelExistenceValidator;
import com.iliauni.idpsyncservice.idp.IdpUserManager;
import com.iliauni.idpsyncservice.idp.IdpUsergroupRequestSender;
import com.iliauni.idpsyncservice.idp.UsergroupIdpRequestSenderResultBlackListFilter;
import com.iliauni.idpsyncservice.model.SyncKcClient;
import com.iliauni.idpsyncservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class SyncKcIdpUsergroupManager extends GenericIdpUsergroupManager<SyncKcClient> {

    @Autowired
    public SyncKcIdpUsergroupManager(
            ClientService<SyncKcClient> clientService,
            @Qualifier("syncKcIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            IdpUsergroupRequestSender<SyncKcClient> requestSender,
            @Lazy IdpModelExistenceValidator<SyncKcClient> modelExistenceValidator,
            @Lazy IdpUserManager<SyncKcClient> userManager,
            UsergroupIdpRequestSenderResultBlackListFilter<SyncKcClient> blackListFilter
    ) {
        super(
                clientService,
                jsonObjectMapper,
                requestSender,
                modelExistenceValidator,
                userManager,
                blackListFilter
        );
    }
}
