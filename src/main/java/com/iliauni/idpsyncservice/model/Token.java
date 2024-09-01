package com.iliauni.idpsyncservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public class Token extends AuthModel {

    public Token(
            Long id,
            String token,
            int expiresIn
    ) {
        super(id);
        this.token = token;
        this.expiresIn = expiresIn;
    }

    public Token(Long id, String token, int expiresIn, Date creationDate) {
        super(id);
        this.token = token;
        this.expiresIn = expiresIn;
        this.creationDate = creationDate;
    }

    @Column(name = "token", length = 2560)
    private String token;

    @Column(name = "expires_in")
    private int expiresIn;

    @CreationTimestamp
    private Date creationDate;

    public Token(String token, int expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
