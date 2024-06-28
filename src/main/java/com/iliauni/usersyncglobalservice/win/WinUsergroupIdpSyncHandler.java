package com.iliauni.usersyncglobalservice.win;

import com.iliauni.usersyncglobalservice.difference.DifferenceCalculator;
import com.iliauni.usersyncglobalservice.idp.GenericUsergroupIdpSyncHandler;
import com.iliauni.usersyncglobalservice.idp.IdpUsergroupManager;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.WinClient;
import com.iliauni.usersyncglobalservice.service.UsergroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WinUsergroupIdpSyncHandler extends GenericUsergroupIdpSyncHandler<WinClient> {

    @Autowired
    protected WinUsergroupIdpSyncHandler(
            UsergroupService usergroupService,
            IdpUsergroupManager<WinClient> usergroupManager,
            DifferenceCalculator<User> userDifferenceCalculator
    ) {
        super(
                usergroupService,
                usergroupManager,
                userDifferenceCalculator
        );
    }
}
