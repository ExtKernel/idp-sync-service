package com.iliauni.usersyncglobalservice.ipa;

import com.iliauni.usersyncglobalservice.idp.GenericIdpModelExistenceValidator;
import com.iliauni.usersyncglobalservice.idp.IdpUserManager;
import com.iliauni.usersyncglobalservice.idp.IdpUsergroupManager;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IpaIdpModelExistenceValidator extends GenericIdpModelExistenceValidator<IpaClient> {
    @Autowired
    public IpaIdpModelExistenceValidator(
            IdpUsergroupManager<IpaClient> usergroupManager,
            IdpUserManager<IpaClient> userManager
    ) {
        super(
                usergroupManager,
                userManager
        );
    }
}
