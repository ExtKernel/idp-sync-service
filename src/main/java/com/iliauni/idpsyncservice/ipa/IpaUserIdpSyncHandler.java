package com.iliauni.idpsyncservice.ipa;

import com.iliauni.idpsyncservice.idp.GenericUserIdpSyncHandler;
import com.iliauni.idpsyncservice.idp.IdpUserManager;
import com.iliauni.idpsyncservice.model.IpaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IpaUserIdpSyncHandler extends GenericUserIdpSyncHandler<IpaClient> {

    @Autowired
    protected IpaUserIdpSyncHandler(IdpUserManager<IpaClient> userManager) {
        super(userManager);
    }
}
