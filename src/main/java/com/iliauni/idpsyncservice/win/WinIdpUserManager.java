package com.iliauni.idpsyncservice.win;

import com.iliauni.idpsyncservice.idp.*;
import com.iliauni.idpsyncservice.model.WinClient;
import com.iliauni.idpsyncservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * A component class implementing the {@link IdpUserManager} interface
 * for managing User operations in an Identity Provider (IDP) context specific to Windows.
 */
@Component
public class WinIdpUserManager extends GenericIdpUserManager<WinClient> {

    @Autowired
    public WinIdpUserManager(
            ClientService<WinClient> clientService,
            @Qualifier("winIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            IdpUserRequestSender<WinClient> requestSender,
            @Lazy IdpModelExistenceValidator<WinClient> modelExistenceValidator,
            UserIdpRequestSenderResultBlackListFilter<WinClient> blackListFilter
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
