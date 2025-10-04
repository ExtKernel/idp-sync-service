package com.iliauni.idpsyncservice.integration;

import com.iliauni.idpsyncservice.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestFactory {
    @Value("${keycloakSimulatorIp}")
    private String kcSimulatorIp;

    @Value("${keycloakSimulatorPort}")
    private String kcSimulatorPort;

    @Value("${keycloakSimulatorRealm}")
    private String kcSimulatorRealm;

    public UsergroupSyncEvent buildUsergroupSyncEvent(
            List<Usergroup> usergroups,
            UsergroupSyncStatus.SyncStatus syncStatus
    ) {
        List<UsergroupSyncStatus> syncStatuses = new ArrayList<>();
        usergroups.stream().forEach(usergroup -> {
            syncStatuses.add(new UsergroupSyncStatus(usergroup, syncStatus));
        });

        return new UsergroupSyncEvent(
                "test-message",
                syncStatuses
        );
    }

    public ApiAccessKcClient buildApiAccessKcClient() {
        ApiAccessKcClient apiAccessKcClient = new ApiAccessKcClient(
                "admin-cli",
                "test-client-secret",
                kcSimulatorIp,
                kcSimulatorPort
        );
        apiAccessKcClient.setName("admin-cli");
        apiAccessKcClient.setRealm(kcSimulatorRealm);
        apiAccessKcClient.setKcIp(kcSimulatorIp);
        apiAccessKcClient.setKcPort(kcSimulatorPort);

        return apiAccessKcClient;
    }

    public SyncKcClient buildSyncKcClient() {
        SyncKcClient syncClient = new SyncKcClient(
                "test-client-id",
                "test-client-secret",
                kcSimulatorIp,
                kcSimulatorPort
        );
        syncClient.setName("test-client");
        syncClient.setRealm(kcSimulatorRealm);
        syncClient.setKcIp(kcSimulatorIp);
        syncClient.setKcPort(kcSimulatorPort);

        return syncClient;
    }

    public User buildUser() {
        return new User(
                "test-user",
                "test-firstname",
                "test-lastname",
                "test-email"
        );
    }

    public Usergroup buildUsergroup() {
        return new Usergroup("test-usergroup", "test-description");
    }
}
