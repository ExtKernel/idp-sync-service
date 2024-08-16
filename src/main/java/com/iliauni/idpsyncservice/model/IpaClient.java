package com.iliauni.idpsyncservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class IpaClient extends CookieClient {
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o)
                .getHibernateLazyInitializer()
                .getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this)
                .getHibernateLazyInitializer()
                .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        IpaClient ipaClient = (IpaClient) o;
        return getId() != null && Objects.equals(getId(), ipaClient.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this)
                .getHibernateLazyInitializer()
                .getPersistentClass()
                .hashCode() : getClass().hashCode();
    }
}
