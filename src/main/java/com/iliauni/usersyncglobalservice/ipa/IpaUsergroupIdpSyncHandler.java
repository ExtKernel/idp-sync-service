package com.iliauni.usersyncglobalservice.ipa;

import com.iliauni.usersyncglobalservice.difference.DifferenceCalculator;
import com.iliauni.usersyncglobalservice.idp.GenericUsergroupIdpSyncHandler;
import com.iliauni.usersyncglobalservice.idp.IdpUsergroupManager;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.service.UsergroupService;
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
