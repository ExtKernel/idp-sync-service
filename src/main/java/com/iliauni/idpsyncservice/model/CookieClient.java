package com.iliauni.idpsyncservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

/**
 * A superclass for any client, that uses cookies for auth.
 * Provides all necessary fields to represent a basic client, which requires auth by cookies
 * and to make a request using data from fields.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@MappedSuperclass
public class CookieClient extends Client {
    public CookieClient(String id) {
        super(id);
    }

    public CookieClient(
            String id,
            String fqdn
    ) {
        super(id, fqdn);
    }

    public CookieClient(
            String id,
            String ip,
            String port
    ) {
        super(id, ip, port);
    }

    @OneToMany(fetch = FetchType.EAGER)
    @Column(name = "cookie", nullable = true)
    @ToString.Exclude
    private List<Cookie> cookies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CookieClient that = (CookieClient) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
