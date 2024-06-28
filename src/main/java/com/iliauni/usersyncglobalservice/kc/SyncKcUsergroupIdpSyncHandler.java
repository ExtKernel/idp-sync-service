package com.iliauni.usersyncglobalservice.kc;

import com.iliauni.usersyncglobalservice.difference.DifferenceCalculator;
import com.iliauni.usersyncglobalservice.idp.GenericUsergroupIdpSyncHandler;
import com.iliauni.usersyncglobalservice.idp.IdpUsergroupManager;
import com.iliauni.usersyncglobalservice.model.SyncKcClient;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.service.UsergroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SyncKcUsergroupIdpSyncHandler extends GenericUsergroupIdpSyncHandler<SyncKcClient> {

    @Autowired
    protected SyncKcUsergroupIdpSyncHandler(
            UsergroupService usergroupService,
            IdpUsergroupManager<SyncKcClient> usergroupManager,
            DifferenceCalculator<User> userDifferenceCalculator
    ) {
        super(
                usergroupService,
                usergroupManager,
                userDifferenceCalculator
        );
    }
}
