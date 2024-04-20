package com.iliauni.usersyncglobalservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class KcClient extends Oauth2Client {
}
