package com.iliauni.idpsyncservice.ipa;

import com.iliauni.idpsyncservice.idp.GenericIdpModelExistenceValidator;
import com.iliauni.idpsyncservice.idp.IdpUserManager;
import com.iliauni.idpsyncservice.idp.IdpUsergroupManager;
import com.iliauni.idpsyncservice.model.IpaClient;
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
