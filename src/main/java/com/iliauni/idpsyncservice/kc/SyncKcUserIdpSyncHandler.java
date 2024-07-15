package com.iliauni.idpsyncservice.kc;

import com.iliauni.idpsyncservice.idp.GenericUserIdpSyncHandler;
import com.iliauni.idpsyncservice.idp.IdpUserManager;
import com.iliauni.idpsyncservice.model.SyncKcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncKcUserIdpSyncHandler extends GenericUserIdpSyncHandler<SyncKcClient> {

    @Autowired
    protected SyncKcUserIdpSyncHandler(IdpUserManager<SyncKcClient> userManager) {
        super(userManager);
    }
}
