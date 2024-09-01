package com.iliauni.idpsyncservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class IpaClient extends CookieClient {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @NotNull
    @Column(name = "cert_path")
    private String certPath;

    public IpaClient(String id) {
        super(id);
    }

    public IpaClient(
            String id,
            String fqdn
    ) {
        super(id, fqdn);
    }

    public IpaClient(
            String id,
            String ip,
            String port
    ) {
        super(id, ip, port);
    }
}
