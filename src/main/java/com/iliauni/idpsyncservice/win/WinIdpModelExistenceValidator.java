package com.iliauni.idpsyncservice.win;

import com.iliauni.idpsyncservice.idp.GenericIdpModelExistenceValidator;
import com.iliauni.idpsyncservice.idp.IdpUserManager;
import com.iliauni.idpsyncservice.idp.IdpUsergroupManager;
import com.iliauni.idpsyncservice.model.WinClient;
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
