package com.iliauni.idpsyncservice.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a regular Keycloak (KC) client, that is supposed to be synchronized.
 */
@Data
@NoArgsConstructor
@Entity
public class SyncKcClient extends KcClient {
    public SyncKcClient(String id) {
        super(id);
    }

    public SyncKcClient(
            String id,
            String clientSecret,
            String fqdn
    ) {
        super(id, clientSecret, fqdn);
    }

    public SyncKcClient(
            String id,
            String clientSecret,
            String ip,
            String port
    ) {
        super(id, clientSecret, ip, port);
    }
}
