package com.iliauni.idpsyncservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class UsergroupSyncEvent extends SyncEvent {

    public UsergroupSyncEvent(
            String message,
            List<UsergroupSyncStatus> usergroupSyncStatuses
    ) {
        super(message);
        this.usergroupSyncStatuses = usergroupSyncStatuses;
    }

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(
            mappedBy = "syncEvent",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UsergroupSyncStatus> usergroupSyncStatuses;

    public List<Usergroup> getNewUsergroups() {
        return usergroupSyncStatuses.stream()
                .filter(status -> status.getStatus() == UsergroupSyncStatus.SyncStatus.NEW)
                .map(UsergroupSyncStatus::getUsergroup)
                .toList();
    }

    public List<Usergroup> getAlteredUsergroups() {
        return usergroupSyncStatuses.stream()
                .filter(status -> status.getStatus() == UsergroupSyncStatus.SyncStatus.ALTERED)
                .map(UsergroupSyncStatus::getUsergroup)
                .toList();
    }

    public List<Usergroup> getMissingUsergroups() {
        return usergroupSyncStatuses.stream()
                .filter(status -> status.getStatus() == UsergroupSyncStatus.SyncStatus.MISSING)
                .map(UsergroupSyncStatus::getUsergroup)
                .toList();
    }
}
