package com.iliauni.idpsyncservice.ipa;

import com.iliauni.idpsyncservice.difference.DifferenceCalculator;
import com.iliauni.idpsyncservice.idp.GenericUsergroupIdpSyncHandler;
import com.iliauni.idpsyncservice.idp.IdpUsergroupManager;
import com.iliauni.idpsyncservice.model.IpaClient;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.service.UsergroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IpaUsergroupIdpSyncHandler extends GenericUsergroupIdpSyncHandler<IpaClient> {

    @Autowired
    protected IpaUsergroupIdpSyncHandler(
            UsergroupService usergroupService,
            IdpUsergroupManager<IpaClient> usergroupManager,
            DifferenceCalculator<User> userDifferenceCalculator
    ) {
        super(
                usergroupService,
                usergroupManager,
                userDifferenceCalculator
        );
    }
}
