package com.iliauni.usersyncglobalservice.kc;

import com.iliauni.usersyncglobalservice.idp.GenericUserIdpSyncHandler;
import com.iliauni.usersyncglobalservice.idp.IdpUserManager;
import com.iliauni.usersyncglobalservice.model.SyncKcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncKcUserIdpSyncHandler extends GenericUserIdpSyncHandler<SyncKcClient> {

    @Autowired
    protected SyncKcUserIdpSyncHandler(IdpUserManager<SyncKcClient> userManager) {
        super(userManager);
    }
}
