package com.iliauni.idpsyncservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
public class UserSyncStatus extends SyncStatus {

    public UserSyncStatus(
            UserSyncEvent syncEvent,
            User user,
            SyncStatus status
    ) {
        this.syncEvent = syncEvent;
        this.user = user;
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "sync_event_id")
    @JsonBackReference
    @ToString.Exclude
    private UserSyncEvent syncEvent;

    @ManyToOne
    @JoinColumn(name = "user_username")
    private User user;

    @Enumerated(EnumType.STRING)
    private SyncStatus status;

    public enum SyncStatus {
        NEW, MISSING
    }
}
