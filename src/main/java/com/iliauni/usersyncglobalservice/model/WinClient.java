package com.iliauni.usersyncglobalservice.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

/**
 * This class represents a Windows (Win) service registered as a Keycloak (KC) client,
 * which is, basically, a regular KC client.
 * Exists for the sake of differentiating between clients when sending requests.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class WinClient extends KcClient {
    public WinClient(String id) {
        super(id);
    }

    public WinClient(
            String id,
            String fqdn,
            String clientSecret
    ) {
        super(id, fqdn, clientSecret);
    }

    public WinClient(
            String id,
            String ip,
            String port,
            String clientSecret
    ) {
        super(id, ip, port, clientSecret);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        WinClient client = (WinClient) o;
        return getId() != null && Objects.equals(getId(), client.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
