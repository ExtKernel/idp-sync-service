package com.iliauni.idpsyncservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@MappedSuperclass
public class Client {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "fqdn")
    private String fqdn;

    @Column(name = "ip")
    private String ip;

    @Column(name = "port")
    private String port;

    @Column(name = "principal_username")
    private String principalUsername;

    @Column(name = "principal_password")
    private String principalPassword;

    // use an EAGER FetchType to not cause multithreading problems
    // the collection should be loaded before parallel processing starts
    // so EntityManager won't be used in parallel
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "usergroup_blacklist")
    private List<String> usergroupBlacklist;

    // use an EAGER FetchType to not cause multithreading problems
    // the collection should be loaded before parallel processing starts
    // so EntityManager won't be used in parallel
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "user_blacklist")
    private List<String> userBlacklist;

    public Client(String id) {
        this.id = id;
    }

    public Client(
            String id,
            String fqdn
    ) {
        this.id = id;
        this.fqdn = fqdn;
    }

    public Client(
            String id,
            String ip,
            String port
    ) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }
}
