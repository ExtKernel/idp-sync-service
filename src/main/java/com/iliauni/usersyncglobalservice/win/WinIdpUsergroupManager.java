package com.iliauni.usersyncglobalservice.win;

import com.iliauni.usersyncglobalservice.idp.*;
import com.iliauni.usersyncglobalservice.model.WinClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class WinIdpUsergroupManager extends GenericIdpUsergroupManager<WinClient> {

    @Autowired
    public WinIdpUsergroupManager(
            @Qualifier("winIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            IdpUsergroupRequestSender<WinClient> requestSender,
            @Lazy IdpModelExistenceValidator<WinClient> modelExistenceValidator,
            UsergroupIdpRequestSenderResultBlackListFilter<WinClient> blackListFilter
    ) {
        super(
                jsonObjectMapper,
                requestSender,
                modelExistenceValidator,
                blackListFilter
        );
    }
}
