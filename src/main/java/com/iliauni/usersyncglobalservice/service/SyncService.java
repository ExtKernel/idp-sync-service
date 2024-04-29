package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.difference.DifferenceCalculator;
import com.iliauni.usersyncglobalservice.exception.NoRecordOfUsergroupsException;
import com.iliauni.usersyncglobalservice.idp.IdpSyncHandler;
import com.iliauni.usersyncglobalservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Lazy
@Service
public class SyncService {
    private final IdpSyncHandler<KcClient> kcIdpSyncHandler;
    private final IdpSyncHandler<IpaClient> ipaIdpSyncHandler;
    private final IdpSyncHandler<WinClient> winIdpSyncHandler;
    private final DifferenceCalculator<Usergroup> usergroupDifferenceCalculator;
    private final SyncKcClientService syncKcClientService;
    private final IpaClientService ipaClientService;
    private final WinClientService winClientService;
    private final UsergroupService usergroupService;

    @Autowired
    public SyncService(
            @Lazy IdpSyncHandler<KcClient> kcIdpSyncHandler,
            @Lazy IdpSyncHandler<IpaClient> ipaIdpSyncHandler,
            @Lazy IdpSyncHandler<WinClient> winIdpSyncHandler,
            DifferenceCalculator<Usergroup> usergroupDifferenceCalculator,
            SyncKcClientService syncKcClientService,
            IpaClientService ipaClientService,
            WinClientService winClientService,
            UsergroupService usergroupService
    ) {
        this.kcIdpSyncHandler = kcIdpSyncHandler;
        this.ipaIdpSyncHandler = ipaIdpSyncHandler;
        this.winIdpSyncHandler = winIdpSyncHandler;
        this.usergroupDifferenceCalculator = usergroupDifferenceCalculator;
        this.syncKcClientService = syncKcClientService;
        this.ipaClientService = ipaClientService;
        this.winClientService = winClientService;
        this.usergroupService = usergroupService;
    }

    public void sync(Optional<List<Usergroup>> optionalUsergroups) {
        List<SyncKcClient> syncKcClients = syncKcClientService.findAll();
//        List<IpaClient> ipaClients = ipaClientService.findAll();
//        List<WinClient> winClients = winClientService.findAll();
        List<Usergroup> dbUsergroups;

        try {
            dbUsergroups = usergroupService.findAll();
        } catch (NoRecordOfUsergroupsException exception) {
            dbUsergroups = new ArrayList<>();
        }

        List<Usergroup> finalDbUsergroups = dbUsergroups;
        optionalUsergroups.ifPresent(usergroups -> {
            syncKcClients.forEach(client -> kcIdpSyncHandler.syncUsergroupChanges(
                    client,
                    usergroupDifferenceCalculator.calculate(
                            finalDbUsergroups,
                            usergroups
                    ))
            );

//            ipaClients.forEach(client -> ipaIdpSyncHandler.syncUsergroupChanges(
//                    client,
//                    usergroupDifferenceCalculator.calculate(
//                            usergroupService.findAll(),
//                            usergroups
//                    ))
//            );
//
//            winClients.forEach(client -> winIdpSyncHandler.syncUsergroupChanges(
//                    client,
//                    usergroupDifferenceCalculator.calculate(
//                            usergroupService.findAll(),
//                            usergroups
//                    ))
//            );
        });
    }
}
