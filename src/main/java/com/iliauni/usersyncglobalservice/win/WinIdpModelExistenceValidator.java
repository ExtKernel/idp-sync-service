package com.iliauni.usersyncglobalservice.win;

import com.iliauni.usersyncglobalservice.idp.GenericIdpModelExistenceValidator;
import com.iliauni.usersyncglobalservice.idp.IdpUserManager;
import com.iliauni.usersyncglobalservice.idp.IdpUsergroupManager;
import com.iliauni.usersyncglobalservice.model.WinClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WinIdpModelExistenceValidator extends GenericIdpModelExistenceValidator<WinClient> {
    @Autowired
    public WinIdpModelExistenceValidator(
            IdpUsergroupManager<WinClient> usergroupManager,
            IdpUserManager<WinClient> userManager
    ) {
        super(
                usergroupManager,
                userManager
        );
    }
}
