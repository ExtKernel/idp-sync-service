package com.iliauni.usersyncglobalservice.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

/**
 * This class represents a regular Keycloak (KC) client, that is supposed to be synchronized.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class SyncKcClient extends KcClient {
    public SyncKcClient(String id) {
        super(id);
    }

    public SyncKcClient(
            String id,
            String fqdn,
            String clientSecret
    ) {
        super(id, fqdn, clientSecret);
    }

    public SyncKcClient(
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
        SyncKcClient that = (SyncKcClient) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
