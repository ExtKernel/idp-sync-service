package com.iliauni.idpsyncservice.win;

import com.iliauni.idpsyncservice.idp.GenericIdpUsergroupManager;
import com.iliauni.idpsyncservice.idp.IdpJsonObjectMapper;
import com.iliauni.idpsyncservice.idp.IdpModelExistenceValidator;
import com.iliauni.idpsyncservice.idp.IdpUserManager;
import com.iliauni.idpsyncservice.idp.IdpUsergroupRequestSender;
import com.iliauni.idpsyncservice.idp.UsergroupIdpRequestSenderResultBlackListFilter;
import com.iliauni.idpsyncservice.model.WinClient;
import com.iliauni.idpsyncservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class WinIdpUsergroupManager extends GenericIdpUsergroupManager<WinClient> {

    @Autowired
    public WinIdpUsergroupManager(
            ClientService<WinClient> clientService,
            @Qualifier("winIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            IdpUsergroupRequestSender<WinClient> requestSender,
            @Lazy IdpModelExistenceValidator<WinClient> modelExistenceValidator,
            @Lazy IdpUserManager<WinClient> userManager,
            UsergroupIdpRequestSenderResultBlackListFilter<WinClient> blackListFilter
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
