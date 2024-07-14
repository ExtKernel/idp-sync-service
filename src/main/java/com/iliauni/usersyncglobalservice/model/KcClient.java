package com.iliauni.usersyncglobalservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

/**
 * A superclass for any Keycloak (KC) client.
 * Including, for example, ones, that aren't supposed to be synchronized.
 * Provides all necessary fields to represent a basic KC client
 * and to make a request using data from fields.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@MappedSuperclass
public class KcClient extends Oauth2Client {
    public KcClient(String id) {
        super(id);
    }

    public KcClient(
            String id,
            String clientSecret,
            String fqdn
    ) {
        super(id, clientSecret, fqdn);
    }

    public KcClient(
            String id,
            String clientSecret,
            String ip,
            String port
    ) {
        super(id, clientSecret, ip, port);
    }

    // the realm to which the client belongs to
    @Column(name = "realm")
    private String realm;

    // the FQDN of the machine on which KC is running
    // will be preferred over IP, if exists
    @Column(name = "kc_fqdn")
    private String kcFqdn;

    // the IP of the machine on which KC is running
    @Column(name = "kc_ip")
    private String kcIp;

    // the port of the KC webserver
    @Column(name = "kc_port")
    private String kcPort;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        KcClient client = (KcClient) o;
        return getId() != null && Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
