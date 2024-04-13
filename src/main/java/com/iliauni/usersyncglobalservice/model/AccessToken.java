package com.iliauni.usersyncglobalservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AccessToken extends Token {
    public AccessToken(Long id, String token, int expiresIn, Date creationDate) {
        super(id, token, expiresIn, creationDate);
    }

    public AccessToken(String token, int expiresIn) {
        super(token, expiresIn);
    }
}
