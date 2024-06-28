package com.iliauni.usersyncglobalservice.ipa;

import com.iliauni.usersyncglobalservice.idp.GenericUserIdpSyncHandler;
import com.iliauni.usersyncglobalservice.idp.IdpUserManager;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IpaUserIdpSyncHandler extends GenericUserIdpSyncHandler<IpaClient> {

    @Autowired
    protected IpaUserIdpSyncHandler(IdpUserManager<IpaClient> userManager) {
        super(userManager);
    }
}
