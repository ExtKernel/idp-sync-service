package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.difference.DifferenceCalculator;
import com.iliauni.idpsyncservice.exception.NoRecordOfUsergroupsException;
import com.iliauni.idpsyncservice.idp.UsergroupDbSyncHandler;
import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.IdpClient;
import com.iliauni.idpsyncservice.model.IdpClientFactory;
import com.iliauni.idpsyncservice.model.Usergroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@Service
public class UsergroupSyncService implements SyncService<Usergroup> {
    private final UsergroupService usergroupService;
    private final DifferenceCalculator<Usergroup> usergroupDifferenceCalculator;
    private final UsergroupDbSyncHandler usergroupDbSyncHandler;
    private final IdpClientFactory idpClientFactory;

    @Autowired
    public UsergroupSyncService(
            UsergroupService usergroupService,
            DifferenceCalculator<Usergroup> usergroupDifferenceCalculator,
            UsergroupDbSyncHandler usergroupDbSyncHandler,
            IdpClientFactory idpClientFactory
    ) {
        this.usergroupService = usergroupService;
        this.usergroupDifferenceCalculator = usergroupDifferenceCalculator;
        this.usergroupDbSyncHandler = usergroupDbSyncHandler;
        this.idpClientFactory = idpClientFactory;
    }

    public void sync(List<Usergroup> usergroups) {
        List<Usergroup> dbUsergroups = getDbUsergroups();
        Map<String, List<Usergroup>> differenceMap = usergroupDifferenceCalculator.calculate(
                dbUsergroups,
                usergroups
        );

        try {
            // wait for all futures to complete
            syncUsergroupsWithAllIdps(differenceMap).join();
        } catch (CancellationException | CompletionException exception) {
            log.error(
                    "An error occurred while synchronizing user groups with all IDPs asynchronously",
                    exception
            );
            throw exception;
        }

        usergroupDbSyncHandler.sync(differenceMap);
    }

    private CompletableFuture<Void> syncUsergroupsWithAllIdps(
            Map<String, List<Usergroup>> differenceMap
    ) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (IdpClient<? extends Client> idpClient : idpClientFactory.getAllClients()) {
            for (Client client : idpClient.getClients()) {
                futures.add(CompletableFuture.runAsync(() ->
                        syncUsergroupForClient(
                                idpClient,
                                client,
                                differenceMap
                        )
                ));
            }
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private <T extends Client> void syncUsergroupForClient(
            IdpClient<T> idpClient,
            Client client,
            Map<String, List<Usergroup>> differenceMap
    ) {
        @SuppressWarnings("unchecked")
        T typedClient = (T) client;
        idpClient.getUsergroupSyncHandler().sync(
                typedClient,
                differenceMap
        );
    }

    @Override
    public CompletableFuture<Void> syncFromAllIdps() {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (IdpClient<? extends Client> idpClient : idpClientFactory.getAllClients()) {
            for (Client client : idpClient.getClients()) {
                futures.add(CompletableFuture.runAsync(() ->
                        syncUsergroupsFromClient(
                                idpClient,
                                client
                        )
                ));
            }
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private <T extends Client> void syncUsergroupsFromClient(
            IdpClient<T> idpClient,
            Client client) {
        @SuppressWarnings("unchecked")
        T typedClient = (T) client;
        List<Usergroup> idpUsergroups = idpClient.getUsergroupManager().getUsergroups(typedClient);
        sync(idpUsergroups);
    }

    private List<Usergroup> getDbUsergroups() {
        try {
            return usergroupService.findAll();
        } catch (NoRecordOfUsergroupsException exception) {
            return new ArrayList<>();
        }
    }
}
