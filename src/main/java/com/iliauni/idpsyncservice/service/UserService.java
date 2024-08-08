package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.IdpClient;
import com.iliauni.idpsyncservice.model.IdpClientFactory;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@Service
public class UserService extends GenericCrudService<User, String>{

    private final IdpClientFactory idpClientFactory;
    private final UserSyncService userSyncService;

    @Autowired
    public UserService(
            UserRepository repository,
            IdpClientFactory idpClientFactory,
            UserSyncService userSyncService
    ) {
        super(repository);
        this.idpClientFactory = idpClientFactory;
        this.userSyncService = userSyncService;
    }

    public String updatePassword(
            String userId,
            String newPassword
    ) {
        User user = findById(userId);
        user.setPassword(newPassword);

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (IdpClient<? extends Client> idpClient : idpClientFactory.getAllClients()) {
            for (Client client : idpClient.getClients()) {
                futures.add(CompletableFuture.runAsync(() -> {
                    userSyncService.syncPasswordWithAllIdps(
                            idpClient,
                            client,
                            user.getUsername(),
                            newPassword
                    );
                }));
            }
        }

        try {
            // Wait for all futures to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (CancellationException | CompletionException exception) {
            log.error(
                    "An error occurred while updating the password on IDPs asynchronously",
                    exception
            );
            throw exception;
        }

        return newPassword;
    }
}
