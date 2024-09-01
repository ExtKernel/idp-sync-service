package com.iliauni.idpsyncservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Cookie extends AuthModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cookie", length = 512)
    private String cookie;

    @CreationTimestamp
    private Date creationDate;
}
