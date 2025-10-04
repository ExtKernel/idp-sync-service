package com.iliauni.idpsyncservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
public class UsergroupSyncStatus extends SyncStatus {

    public UsergroupSyncStatus(
            Usergroup usergroup,
            SyncStatus status
    ) {
        this.status = status;
        this.usergroup = usergroup;
    }

    @ManyToOne
    @JoinColumn(name = "sync_event_id")
    @JsonBackReference
    @ToString.Exclude
    private UsergroupSyncEvent syncEvent;

    @ManyToOne
    @JoinColumn(name = "usergroup_name")
    private Usergroup usergroup;

    @Enumerated(EnumType.STRING)
    private SyncStatus status;

    public enum SyncStatus {
        NEW, ALTERED, MISSING
    }
}
