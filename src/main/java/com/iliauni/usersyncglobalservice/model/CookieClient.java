package com.iliauni.usersyncglobalservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public class CookieClient extends Client {
    @OneToMany(fetch = FetchType.EAGER)
    @Column(name = "cookie_jar", nullable = true)
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
