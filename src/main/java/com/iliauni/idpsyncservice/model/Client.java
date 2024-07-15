package com.iliauni.idpsyncservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@MappedSuperclass
public class Client {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

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

    @ElementCollection
    @Column(name = "usergroup_blacklist")
    private List<String> usergroupBlacklist;

    @ElementCollection
    @Column(name = "user_blacklist")
    private List<String> userBlacklist;

    public Client(String id) {
        this.id = id;
    }

    public Client(String id, String fqdn) {
        this.id = id;
        this.fqdn = fqdn;
    }

    public Client(String id, String ip, String port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Client client = (Client) o;
        return Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
