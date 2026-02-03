package com.iliauni.idpsyncservice.integration;

import com.iliauni.idpsyncservice.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Profile("test")
public class SyncControllerIT extends AbstractIntegrationTest {
    @Value("${waitSync}")
    private int waitSync;

    @Autowired
    private TestFactory testFactory;

    @BeforeEach
    public void setupClients() throws Exception {
        ApiAccessKcClient apiAccessClient = testFactory.buildApiAccessKcClient();
        performPostRequestExpectedSuccess(
                "/secured/client/API/KC",
                apiAccessClient,
                ApiAccessKcClient.class
        );

        SyncKcClient syncClient = testFactory.buildSyncKcClient();
        performPostRequestExpectedSuccess(
                "/secured/client/KC",
                syncClient,
                SyncKcClient.class
        );
    }

    @Test
    public void givenCorrectUsergroups_whenSyncUsergroups_thenReturnSyncEvent()
            throws Exception {
        Usergroup usergroup1 = testFactory.buildUsergroup();
        usergroup1.setName("test-usergroup1");
        Usergroup usergroup2 = testFactory.buildUsergroup();
        usergroup2.setName("test-usergroup2");
        Usergroup usergroup3 = testFactory.buildUsergroup();
        usergroup3.setName("test-usergroup3");

        List<Usergroup> usergroups = Arrays.asList(usergroup1, usergroup2, usergroup3);

        UsergroupSyncEvent syncEvent = performUsergroupSync(usergroups);
        UsergroupSyncEvent expectedSyncEvent = testFactory.buildUsergroupSyncEvent(
                usergroups,
                UsergroupSyncStatus.SyncStatus.NEW
        );

        // Sleep until async operations are done
        // Application context will shutdown without sync done otherwise
        Thread.sleep(waitSync);

        assertEquals(expectedSyncEvent.getNewUsergroups().size(), syncEvent.getNewUsergroups().size());
    }

    @Test
    public void givenCorrectUsergroups_whenSyncAlteredUsergroups_thenReturnSyncEvent()
            throws Exception {
        Usergroup usergroup1 = testFactory.buildUsergroup();
        usergroup1.setName("test-usergroup1");
        Usergroup usergroup2 = testFactory.buildUsergroup();
        usergroup2.setName("test-usergroup2");
        Usergroup usergroup3 = testFactory.buildUsergroup();
        usergroup3.setName("test-usergroup3");
        List<Usergroup> usergroups = Arrays.asList(usergroup1, usergroup2, usergroup3);

        performUsergroupSync(usergroups);
        // Wait for the initial usergroup sync
        Thread.sleep(waitSync);

        User user1 = testFactory.buildUser();
        user1.setUsername("test-user1");
        User user2 = testFactory.buildUser();
        user2.setUsername("test-user2");
        User user3 = testFactory.buildUser();
        user3.setUsername("test-user3");

        // Users are the defining factor when the service
        // Will be deciding whether a usergroup is altered or not
        usergroup1.setDescription("altered-description1");
        usergroup1.setUsers(List.of(user1));
        usergroup2.setDescription("altered-description2");
        usergroup2.setUsers(List.of(user2));
        usergroup3.setDescription("altered-description3");
        usergroup3.setUsers(List.of(user3));
        // Update the list with altered usergroups
        usergroups = Arrays.asList(usergroup1, usergroup2, usergroup3);

        UsergroupSyncEvent alteredSyncEvent = performPostRequestExpectedSuccess(
                "/secured/sync/groups",
                usergroups,
                UsergroupSyncEvent.class
        );
        UsergroupSyncEvent expectedAlteredSyncEvent = testFactory.buildUsergroupSyncEvent(
                usergroups,
                UsergroupSyncStatus.SyncStatus.ALTERED
        );

        // Wait for altered usergroup sync
        Thread.sleep(waitSync);

        assertEquals(
                alteredSyncEvent.getAlteredUsergroups().size(),
                expectedAlteredSyncEvent.getAlteredUsergroups().size()
        );
        assertEquals(alteredSyncEvent.getAlteredUsergroups().size(), usergroups.size());
        assertTrue(alteredSyncEvent.getAlteredUsergroups().containsAll(usergroups));
    }

    private UsergroupSyncEvent performUsergroupSync(List<Usergroup> usergroups)
            throws Exception {
        return performPostRequestExpectedSuccess(
                "/secured/sync/groups",
                usergroups,
                UsergroupSyncEvent.class
        );
    }
}
