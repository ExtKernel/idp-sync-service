package com.iliauni.usersyncglobalservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class WinClient extends Oauth2Client {
    @Column(name = "ip")
    private String ip;

    @Column(name = "port")
    private String port;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        WinClient winClient = (WinClient) o;
        return getId() != null && Objects.equals(getId(), winClient.getId());
    }

    @Override
    public int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
