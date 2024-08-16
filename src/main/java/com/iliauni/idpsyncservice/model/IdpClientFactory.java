package com.iliauni.idpsyncservice.model;

import com.iliauni.idpsyncservice.idp.IdpUserManager;
import com.iliauni.idpsyncservice.idp.IdpUsergroupManager;
import com.iliauni.idpsyncservice.idp.UserIdpSyncHandler;
import com.iliauni.idpsyncservice.idp.UsergroupIdpSyncHandler;
import com.iliauni.idpsyncservice.service.CookieClientService;
import com.iliauni.idpsyncservice.service.Oauth2ClientService;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdpClientFactory {

    private final List<IdpClient<? extends Client>> clients;

    @Autowired
    public IdpClientFactory(
            CookieClientService<IpaClient> ipaCookieClientService,
            UsergroupIdpSyncHandler<IpaClient> ipaUsergroupIdpSyncHandler,
            UserIdpSyncHandler<IpaClient> ipaUserIdpSyncHandler,
            IdpUserManager<IpaClient> ipaIdpUserManager,
            IdpUsergroupManager<IpaClient> ipaIdpUsergroupManager,
            Oauth2ClientService<SyncKcClient> syncKcOauth2ClientService,
            UsergroupIdpSyncHandler<SyncKcClient> syncKcUsergroupIdpSyncHandler,
            UserIdpSyncHandler<SyncKcClient> syncKcUserIdpSyncHandler,
            IdpUserManager<SyncKcClient> syncKcIdpUserManager,
            IdpUsergroupManager<SyncKcClient> syncKcIdpUsergroupManager,
            Oauth2ClientService<WinClient> winOauth2ClientService,
            UsergroupIdpSyncHandler<WinClient> winUsergroupIdpSyncHandler,
            UserIdpSyncHandler<WinClient> winUserIdpSyncHandler,
            IdpUserManager<WinClient> winIdpUserManager,
            IdpUsergroupManager<WinClient> winIdpUsergroupManager
    ) {

        this.clients = Arrays.asList(
                new IdpClient<>(
                        ipaCookieClientService,
                        ipaUsergroupIdpSyncHandler,
                        ipaUserIdpSyncHandler,
                        ipaIdpUserManager,
                        ipaIdpUsergroupManager
                ),
                new IdpClient<>(
                        syncKcOauth2ClientService,
                        syncKcUsergroupIdpSyncHandler,
                        syncKcUserIdpSyncHandler,
                        syncKcIdpUserManager,
                        syncKcIdpUsergroupManager
                ),
                new IdpClient<>(
                        winOauth2ClientService,
                        winUsergroupIdpSyncHandler,
                        winUserIdpSyncHandler,
                        winIdpUserManager,
                        winIdpUsergroupManager
                )
        );
    }

    public List<IdpClient<? extends Client>> getAllClients() {
        return clients;
    }
}
