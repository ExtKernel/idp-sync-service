package com.iliauni.idpsyncservice.model;

import com.iliauni.idpsyncservice.idp.IdpUserManager;
import com.iliauni.idpsyncservice.idp.IdpUsergroupManager;
import com.iliauni.idpsyncservice.idp.UserIdpSyncHandler;
import com.iliauni.idpsyncservice.idp.UsergroupIdpSyncHandler;
import com.iliauni.idpsyncservice.service.ClientService;
import lombok.Getter;

import java.util.List;

@Getter
public class IdpClient<T extends Client> {
    private final ClientService<T> clientService;
    private final UsergroupIdpSyncHandler<T> usergroupSyncHandler;
    private final UserIdpSyncHandler<T> userSyncHandler;
    private final IdpUserManager<T> userManager;
    private final IdpUsergroupManager<T> usergroupManager;

    public IdpClient(
            ClientService<T> clientService,
            UsergroupIdpSyncHandler<T> usergroupSyncHandler,
            UserIdpSyncHandler<T> userSyncHandler,
            IdpUserManager<T> userManager,
            IdpUsergroupManager<T> usergroupManager
    ) {
        this.clientService = clientService;
        this.usergroupSyncHandler = usergroupSyncHandler;
        this.userSyncHandler = userSyncHandler;
        this.userManager = userManager;
        this.usergroupManager = usergroupManager;
    }

    public List<T> getClients() {
        return clientService.findAll();
    }
}