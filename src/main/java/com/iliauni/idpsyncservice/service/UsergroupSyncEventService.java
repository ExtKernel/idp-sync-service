package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.model.Usergroup;
import com.iliauni.idpsyncservice.model.UsergroupSyncEvent;
import com.iliauni.idpsyncservice.model.UsergroupSyncStatus;
import com.iliauni.idpsyncservice.repository.UsergroupSyncEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsergroupSyncEventService {
    private final UsergroupSyncEventRepository repository;

    public UsergroupSyncEventService(UsergroupSyncEventRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new {@link UsergroupSyncEvent} based on the provided message and usergroup difference map.
     *
     * @param message The message describing the sync event.
     * @param usergroupDifferenceMap A map containing lists of {@link Usergroup} categorized by their sync status.
     * @return A new {@link UsergroupSyncEvent} object populated with the provided data.
     */
    public UsergroupSyncEvent create(String message, Map<String, List<Usergroup>> usergroupDifferenceMap) {
        UsergroupSyncEvent usergroupSyncEvent = new UsergroupSyncEvent();
        usergroupSyncEvent.setMessage(message);
        List<UsergroupSyncStatus> statuses = createUsergroupSyncStatuses(usergroupDifferenceMap);
        usergroupSyncEvent.setUsergroupSyncStatuses(statuses);

        // Set the bidirectional relationship
        statuses.forEach(status -> status.setSyncEvent(usergroupSyncEvent));

        return usergroupSyncEvent;
    }

    /**
     * Persists the given {@link UsergroupSyncEvent} to the database.
     *
     * @param event The {@link UsergroupSyncEvent} to be saved.
     * @return The saved {@link UsergroupSyncEvent}, potentially with updated information (e.g., generated ID).
     */
    public UsergroupSyncEvent save(UsergroupSyncEvent event) {
        return repository.save(event);
    }

    /**
     * Creates {@link UsergroupSyncStatus} objects based on the provided usergroup difference map.
     *
     * @param usergroupDifferenceMap A map containing lists of usergroups categorized by their sync status.
     * @return A list of {@link UsergroupSyncStatus} objects.
     */
    private List<UsergroupSyncStatus> createUsergroupSyncStatuses(Map<String, List<Usergroup>> usergroupDifferenceMap) {
        return usergroupDifferenceMap.entrySet().stream()
                .flatMap(entry -> createUsergroupSyncStatusesForCategory(entry.getValue(), mapStatus(entry.getKey())).stream())
                .collect(Collectors.toList());
    }

    /**
     * Creates {@link UsergroupSyncStatus} objects for a specific category of usergroups.
     *
     * @param usergroups The list of {@link Usergroup} in the category.
     * @param status The sync status for this category.
     * @return A list of {@link UsergroupSyncStatus} objects for the given usergroups and status.
     */
    private List<UsergroupSyncStatus> createUsergroupSyncStatusesForCategory(List<Usergroup> usergroups, UsergroupSyncStatus.SyncStatus status) {
        return usergroups.stream()
                .map(usergroup -> createUsergroupSyncStatus(usergroup, status))
                .collect(Collectors.toList());
    }

    /**
     * Creates a single {@link UsergroupSyncStatus} object for a given {@link Usergroup} and status.
     *
     * @param usergroup The {@link Usergroup} for which to create the sync status.
     * @param status The sync status of the {@link Usergroup}.
     * @return A new {@link UsergroupSyncStatus} object.
     */
    private UsergroupSyncStatus createUsergroupSyncStatus(Usergroup usergroup, UsergroupSyncStatus.SyncStatus status) {
        UsergroupSyncStatus usergroupSyncStatus = new UsergroupSyncStatus();
        usergroupSyncStatus.setUsergroup(usergroup);
        usergroupSyncStatus.setStatus(status);
        return usergroupSyncStatus;
    }

    /**
     * Maps a string key to the corresponding {@link UsergroupSyncStatus.SyncStatus} enum value.
     *
     * @param key The string key representing the status.
     * @return The corresponding {@link UsergroupSyncStatus.SyncStatus} enum value.
     * @throws IllegalArgumentException if the key is not recognized.
     */
    private UsergroupSyncStatus.SyncStatus mapStatus(String key) {
        return switch (key) {
            case "new" -> UsergroupSyncStatus.SyncStatus.NEW;
            case "altered" -> UsergroupSyncStatus.SyncStatus.ALTERED;
            case "missing" -> UsergroupSyncStatus.SyncStatus.MISSING;
            default -> throw new IllegalArgumentException("Unknown status key: " + key);
        };
    }
}
