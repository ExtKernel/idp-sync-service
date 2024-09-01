package com.iliauni.idpsyncservice.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents Keycloak (KC) client, that is supposed to be used to access an API
 * and is not meant to be synchronized.
 * A pure example is the Admin CLI client.
 */
@Data
@NoArgsConstructor
@Entity
public class ApiAccessKcClient extends KcClient {
    public ApiAccessKcClient(String id) {
        super(id);
    }

    public ApiAccessKcClient(
            String id,
            String clientSecret,
            String fqdn
    ) {
        super(id, clientSecret, fqdn);
    }

    public ApiAccessKcClient(
            String id,
            String clientSecret,
            String ip,
            String port
    ) {
        super(id, clientSecret, ip, port);
    }
}
