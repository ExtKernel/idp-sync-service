package com.iliauni.usersyncglobalservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Usergroup implements Serializable {

    public Usergroup(String name) {
        this.name = name;
    }

    public Usergroup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Id
    @Column(name = "name", nullable = false, unique = true)
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
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @Column(name = "users", nullable = true)
    @ToString.Exclude
    private List<User> users;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Usergroup usergroup = (Usergroup) o;
        return getName() != null && Objects.equals(getName(), usergroup.getName());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
