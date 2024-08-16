package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.IdpClient;
import com.iliauni.idpsyncservice.model.IdpClientFactory;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService extends GenericCrudService<User, String>{

    private final IdpClientFactory idpClientFactory;
    private final UserSyncService userSyncService;

    @Autowired
    public UserService(
            UserRepository repository,
            @Lazy IdpClientFactory idpClientFactory,
            @Lazy UserSyncService userSyncService
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

        super.update(Optional.of(user));
        return newPassword;
    }

    public String updatePassword(
            String userId,
            String newPassword,
            boolean sync
    ) {
        User user = findById(userId);
        user.setPassword(newPassword);

        if (sync) {
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
        }

        super.update(Optional.of(user));
        return newPassword;
    }
}
