package com.iliauni.idpsyncservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
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
    @Column(name = "refresh_token")
    @ToString.Exclude
    private List<RefreshToken> refreshTokens;
}
