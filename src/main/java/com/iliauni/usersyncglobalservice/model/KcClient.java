package com.iliauni.usersyncglobalservice.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class KcClient extends Oauth2Client {
}
