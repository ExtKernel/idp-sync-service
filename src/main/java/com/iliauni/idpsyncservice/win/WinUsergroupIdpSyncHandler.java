package com.iliauni.idpsyncservice.win;

import com.iliauni.idpsyncservice.difference.DifferenceCalculator;
import com.iliauni.idpsyncservice.idp.GenericUsergroupIdpSyncHandler;
import com.iliauni.idpsyncservice.idp.IdpUsergroupManager;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.WinClient;
import com.iliauni.idpsyncservice.service.UsergroupService;
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
