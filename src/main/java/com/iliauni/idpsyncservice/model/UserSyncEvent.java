package com.iliauni.idpsyncservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class UserSyncEvent extends SyncEvent {

    public UserSyncEvent(
            String message,
            List<UserSyncStatus> userSyncStatuses
    ) {
        super(message);
        this.userSyncStatuses = userSyncStatuses;
    }

    @OneToMany(
            mappedBy = "syncEvent",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserSyncStatus> userSyncStatuses;

    public List<User> getNewUsers() {
        return userSyncStatuses.stream()
                .filter(status -> status.getStatus() == UserSyncStatus.SyncStatus.NEW)
                .map(UserSyncStatus::getUser)
                .toList();
    }

    public List<User> getMissingUsers() {
        return userSyncStatuses.stream()
                .filter(status -> status.getStatus() == UserSyncStatus.SyncStatus.MISSING)
                .map(UserSyncStatus::getUser)
                .toList();
    }
}
