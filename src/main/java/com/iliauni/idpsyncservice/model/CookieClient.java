package com.iliauni.idpsyncservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * A superclass for any client, that uses cookies for auth.
 * Provides all necessary fields to represent a basic client, which requires auth by cookies
 * and to make a request using data from fields.
 */
@Data
@NoArgsConstructor
//@MappedSuperclass
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
    @Column(name = "cookie")
    @ToString.Exclude
    private List<Cookie> cookies;
}
