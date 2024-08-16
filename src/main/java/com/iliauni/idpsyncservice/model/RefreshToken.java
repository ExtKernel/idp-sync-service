package com.iliauni.idpsyncservice.model;

import jakarta.persistence.Entity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class RefreshToken extends Token {
    public RefreshToken(Long id, String token, int expiresIn, Date creationDate) {
        super(id, token, expiresIn, creationDate);
    }

    public RefreshToken(String token, int expiresIn) {
        super(token, expiresIn);
    }
}
