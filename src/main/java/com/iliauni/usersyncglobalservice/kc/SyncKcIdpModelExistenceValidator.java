package com.iliauni.usersyncglobalservice.kc;

import com.iliauni.usersyncglobalservice.idp.GenericIdpModelExistenceValidator;
import com.iliauni.usersyncglobalservice.idp.IdpUserManager;
import com.iliauni.usersyncglobalservice.idp.IdpUsergroupManager;
import com.iliauni.usersyncglobalservice.model.SyncKcClient;
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
