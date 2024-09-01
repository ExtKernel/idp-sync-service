package com.iliauni.idpsyncservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A superclass for any Keycloak (KC) client.
 * Including, for example, ones, that aren't supposed to be synchronized.
 * Provides all necessary fields to represent a basic KC client
 * and to make a request using data from fields.
 */
@Data
@NoArgsConstructor
@MappedSuperclass
public class KcClient extends Oauth2Client {
    public KcClient(String id) {
        super(id);
    }

    public KcClient(
            String id,
            String clientSecret,
            String fqdn
    ) {
        super(id, clientSecret, fqdn);
    }

    public KcClient(
            String id,
            String clientSecret,
            String ip,
            String port
    ) {
        super(id, clientSecret, ip, port);
    }

    // the realm to which the client belongs to
    @NotNull
    @Column(name = "realm")
    private String realm;

    // the FQDN of the machine on which KC is running
    // will be preferred over IP, if exists
    @Column(name = "kc_fqdn")
    private String kcFqdn;

    // the IP of the machine on which KC is running
    @Column(name = "kc_ip")
    private String kcIp;

    // the port of the KC webserver
    @Column(name = "kc_port")
    private String kcPort;
}
