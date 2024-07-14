package com.iliauni.usersyncglobalservice.model;

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

@Getter
@Setter
@ToString
@NoArgsConstructor
@MappedSuperclass
public class Oauth2Client extends Client {
    public Oauth2Client(String id) {
        super(id);
    }

    public Oauth2Client(
            String id,
            String clientSecret,
            String fqdn
    ) {
        super(id, fqdn);
        this.clientSecret = clientSecret;
    }

    public Oauth2Client(
            String id,
            String clientSecret,
            String ip,
            String port
    ) {
        super(id, ip, port);
        this.clientSecret = clientSecret;
    }

    @Column(name = "client_secret")
    private String clientSecret;

    @OneToMany(fetch = FetchType.EAGER)
    @Column(name = "refresh_token", nullable = true)
    @ToString.Exclude
    private List<RefreshToken> refreshTokens;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Oauth2Client that = (Oauth2Client) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
