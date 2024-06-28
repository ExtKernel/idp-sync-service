package com.iliauni.usersyncglobalservice.win;

import com.iliauni.usersyncglobalservice.idp.GenericUserIdpSyncHandler;
import com.iliauni.usersyncglobalservice.idp.IdpUserManager;
import com.iliauni.usersyncglobalservice.model.WinClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WinUserIdpSyncHandler extends GenericUserIdpSyncHandler<WinClient> {

    @Autowired
    protected WinUserIdpSyncHandler(IdpUserManager<WinClient> userManager) {
        super(userManager);
    }
}
