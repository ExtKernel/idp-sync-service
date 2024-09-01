package com.iliauni.idpsyncservice.service;

import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.UserSyncEvent;
import com.iliauni.idpsyncservice.model.UserSyncStatus;
import com.iliauni.idpsyncservice.repository.UserSyncEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserSyncEventService {
    private final UserSyncEventRepository repository;

    public UserSyncEventService(UserSyncEventRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new {@link UserSyncEvent} based on the provided message and {@link User} difference map.
     *
     * @param message The message describing the sync event.
     * @param userDifferenceMap A map containing lists of {@link User} categorized by their sync status.
     * @return A new {@link UserSyncEvent} object populated with the provided data.
     */
    public UserSyncEvent create(String message, Map<String, List<User>> userDifferenceMap) {
        UserSyncEvent userSyncEvent = new UserSyncEvent();
        userSyncEvent.setMessage(message);
        List<UserSyncStatus> statuses = createUserSyncStatuses(userDifferenceMap);
        userSyncEvent.setUserSyncStatuses(statuses);

        // Set the bidirectional relationship
        statuses.forEach(status -> status.setSyncEvent(userSyncEvent));

        return userSyncEvent;
    }

    /**
     * Persists the given {@link UserSyncEvent} to the database.
     *
     * @param event The {@link UserSyncEvent} to be saved.
     * @return The saved {@link UserSyncEvent}, potentially with updated information (e.g., generated ID).
     */
    public UserSyncEvent save(UserSyncEvent event) {
        return repository.save(event);
    }

    /**
     * Creates {@link UserSyncStatus} objects based on the provided user difference map.
     *
     * @param userDifferenceMap A map containing lists of {@link User} categorized by their sync status.
     * @return A list of {@link UserSyncStatus} objects.
     */
    private List<UserSyncStatus> createUserSyncStatuses(Map<String, List<User>> userDifferenceMap) {
        return userDifferenceMap.entrySet().stream()
                .flatMap(entry -> createUserSyncStatusesForCategory(entry.getValue(), mapStatus(entry.getKey())).stream())
                .collect(Collectors.toList());
    }

    /**
     * Creates {@link UserSyncStatus} objects for a specific category of users.
     *
     * @param users The list of {@link User} in the category.
     * @param status The sync status for this category.
     * @return A list of {@link UserSyncStatus} objects for the given users and status.
     */
    private List<UserSyncStatus> createUserSyncStatusesForCategory(List<User> users, UserSyncStatus.SyncStatus status) {
        return users.stream()
                .map(user -> createUserSyncStatus(user, status))
                .collect(Collectors.toList());
    }

    /**
     * Creates a single {@link UserSyncStatus} object for a given {@link User} and status.
     *
     * @param user The {@link User} for which to create the sync status.
     * @param status The sync status of the {@link User}.
     * @return A new {@link UserSyncStatus} object.
     */
    private UserSyncStatus createUserSyncStatus(User user, UserSyncStatus.SyncStatus status) {
        UserSyncStatus userSyncStatus = new UserSyncStatus();
        userSyncStatus.setUser(user);
        userSyncStatus.setStatus(status);
        return userSyncStatus;
    }

    /**
     * Maps a string key to the corresponding {@link UserSyncStatus.SyncStatus} enum value.
     *
     * @param key The string key representing the status.
     * @return The corresponding {@link UserSyncStatus.SyncStatus} enum value.
     * @throws IllegalArgumentException if the key is not recognized.
     */
    private UserSyncStatus.SyncStatus mapStatus(String key) {
        return switch (key) {
            case "new" -> UserSyncStatus.SyncStatus.NEW;
            case "missing" -> UserSyncStatus.SyncStatus.MISSING;
            default -> throw new IllegalArgumentException("Unknown status key: " + key);
        };
    }
}
