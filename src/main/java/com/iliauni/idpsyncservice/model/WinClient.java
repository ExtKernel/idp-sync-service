package com.iliauni.idpsyncservice.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a Windows (Win) service registered as a Keycloak (KC) client,
 * which is, basically, a regular KC client.
 * Exists for the sake of differentiating between clients when sending requests.
 */
@Data
@NoArgsConstructor
@Entity
public class WinClient extends KcClient {
    public WinClient(String id) {
        super(id);
    }

    public WinClient(
            String id,
            String clientSecret,
            String fqdn
    ) {
        super(id, clientSecret, fqdn);
    }

    public WinClient(
            String id,
            String clientSecret,
            String ip,
            String port
    ) {
        super(id, clientSecret, ip, port);
    }
}
