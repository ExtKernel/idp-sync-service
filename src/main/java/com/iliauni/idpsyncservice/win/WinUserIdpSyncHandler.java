package com.iliauni.idpsyncservice.win;

import com.iliauni.idpsyncservice.idp.GenericUserIdpSyncHandler;
import com.iliauni.idpsyncservice.idp.IdpUserManager;
import com.iliauni.idpsyncservice.model.WinClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WinUserIdpSyncHandler extends GenericUserIdpSyncHandler<WinClient> {

    @Autowired
    protected WinUserIdpSyncHandler(IdpUserManager<WinClient> userManager) {
        super(userManager);
    }
}
