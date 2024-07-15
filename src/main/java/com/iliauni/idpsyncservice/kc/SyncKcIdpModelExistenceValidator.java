package com.iliauni.idpsyncservice.kc;

import com.iliauni.idpsyncservice.idp.GenericIdpModelExistenceValidator;
import com.iliauni.idpsyncservice.idp.IdpUserManager;
import com.iliauni.idpsyncservice.idp.IdpUsergroupManager;
import com.iliauni.idpsyncservice.model.SyncKcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncKcIdpModelExistenceValidator extends GenericIdpModelExistenceValidator<SyncKcClient> {
    @Autowired
    public SyncKcIdpModelExistenceValidator(
            IdpUsergroupManager<SyncKcClient> usergroupManager,
            IdpUserManager<SyncKcClient> userManager
    ) {
        super(
                usergroupManager,
                userManager
        );
    }
}
