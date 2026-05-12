package com.iliauni.idpsyncservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@Table(name = "sync_user")
@Entity
public class User implements Serializable {

    public User(String username) {
        this.username = username;
    }

    public User(
            String username,
            String firstname,
            String lastname,
            String email
    ) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    @Id
    @Column(
            name = "username",
            unique = true,
            nullable = false
    )
    private String username;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "users", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @Column(name = "usergroups")
    @JsonIgnore
    @ToString.Exclude
    private List<Usergroup> usergroups;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    @ToString.Exclude
    private List<UserSyncStatus> syncStatuses;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}
