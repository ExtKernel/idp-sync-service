package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.difference.DifferenceCalculator;
import com.iliauni.idpsyncservice.exception.NoRecordOfUsersException;
import com.iliauni.idpsyncservice.idp.UserDbSyncHandler;
import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.IdpClient;
import com.iliauni.idpsyncservice.model.IdpClientFactory;
import com.iliauni.idpsyncservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class UserSyncService implements SyncService<User> {

    private final UserService userService;
    private final DifferenceCalculator<User> userDifferenceCalculator;
    private final UserDbSyncHandler userDbSyncHandler;
    private final IdpClientFactory idpClientFactory;

    @Autowired
    public UserSyncService(
            UserService userService,
            DifferenceCalculator<User> userDifferenceCalculator,
            UserDbSyncHandler userDbSyncHandler,
            IdpClientFactory idpClientFactory
    ) {
        this.userService = userService;
        this.userDifferenceCalculator = userDifferenceCalculator;
        this.userDbSyncHandler = userDbSyncHandler;
        this.idpClientFactory = idpClientFactory;
    }

    @Override
    public void sync(List<User> users) {
        List<User> dbUsers = getDbUsers();
        Map<String, List<User>> differenceMap = userDifferenceCalculator.calculate(
                dbUsers,
                users
        );

        CompletableFuture<Void> idpSyncFuture = syncUsersWithAllIdps(differenceMap);
        idpSyncFuture.join();

        userDbSyncHandler.sync(differenceMap);
    }

    private CompletableFuture<Void> syncUsersWithAllIdps(
            Map<String, List<User>> differenceMap
    ) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (IdpClient<? extends Client> idpClient : idpClientFactory.getAllClients()) {
            for (Client client : idpClient.getClients()) {
                futures.add(CompletableFuture.runAsync(() ->
                        syncUsersForClient(
                                idpClient,
                                client,
                                differenceMap
                        )
                ));
            }
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private <T extends Client> void syncUsersForClient(
            IdpClient<T> idpClient,
            Client client,
            Map<String, List<User>> differenceMap) {
        @SuppressWarnings("unchecked")
        T typedClient = (T) client;
        idpClient.getUserSyncHandler().sync(
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
                        syncUsersFromClient(
                                idpClient,
                                client
                        )
                ));
            }
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private <T extends Client> void syncUsersFromClient(
            IdpClient<T> idpClient,
            Client client) {
        @SuppressWarnings("unchecked")
        T typedClient = (T) client;
        sync(idpClient.getUserManager().getUsers(typedClient));
    }

    private List<User> getDbUsers() {
        try {
            return userService.findAll();
        } catch (NoRecordOfUsersException exception) {
            return new ArrayList<>();
        }
    }
}
