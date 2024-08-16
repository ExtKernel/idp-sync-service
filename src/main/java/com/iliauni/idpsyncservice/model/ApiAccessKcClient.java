package com.iliauni.idpsyncservice.model;

import jakarta.persistence.Entity;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

/**
 * This class represents Keycloak (KC) client, that is supposed to be used to access an API
 * and is not meant to be synchronized.
 * A pure example is the Admin CLI client.
 */
@Getter
@Setter
@ToString
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ApiAccessKcClient that = (ApiAccessKcClient) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
