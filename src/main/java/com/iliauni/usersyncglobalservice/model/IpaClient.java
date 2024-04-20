package com.iliauni.usersyncglobalservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class IpaClient extends CookieClient {
}
