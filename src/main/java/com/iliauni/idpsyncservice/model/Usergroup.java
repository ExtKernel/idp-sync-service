package com.iliauni.idpsyncservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usergroup implements Serializable {

    public Usergroup(String name) {
        this.name = name;
    }

    public Usergroup(
            String name,
            String description
    ) {
        this.name = name;
        this.description = description;
    }

    public Usergroup(
            String name,
            String description,
            List<User> users
    ) {
        this.name = name;
        this.description = description;
        this.users = users;
    }

    @Id
    @Column(
            name = "name",
            unique = true,
            nullable = false
    )
    private String name;

    @Column(name = "description")
    private String description;

    @JoinTable(
            name = "usergroup_user",
            joinColumns = {
                    @JoinColumn(
                            name = "usergroup_name",
                            referencedColumnName = "name"
                    )
            },
            inverseJoinColumns = {
                    @JoinColumn(
                            name = "user_username",
                            referencedColumnName = "username"
                    )
            }
    )
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @Column(name = "users")
    @ToString.Exclude
    private List<User> users;

    @OneToMany(
            mappedBy = "usergroup",
            cascade = CascadeType.ALL
//            orphanRemoval = true
    )
    @ToString.Exclude
    private List<UsergroupSyncStatus> syncStatuses;
}
