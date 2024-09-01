package com.iliauni.idpsyncservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AccessToken extends Token {
    public AccessToken(
            Long id,
            String token,
            int expiresIn,
            Date creationDate
    ) {
        super(id, token, expiresIn, creationDate);
    }

    public AccessToken(
            String token,
            int expiresIn
    ) {
        super(token, expiresIn);
    }
}
