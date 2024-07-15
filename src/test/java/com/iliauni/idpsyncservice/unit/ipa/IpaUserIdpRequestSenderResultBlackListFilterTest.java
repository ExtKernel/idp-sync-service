package com.iliauni.idpsyncservice.unit.ipa;

import com.iliauni.idpsyncservice.ipa.IpaUserIdpRequestSenderResultBlackListFilter;
import com.iliauni.idpsyncservice.model.IpaClient;
import com.iliauni.idpsyncservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IpaUserIdpRequestSenderResultBlackListFilterTest {

    @InjectMocks
    private IpaUserIdpRequestSenderResultBlackListFilter ipaUserIdpRequestSenderResultBlackListFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void filter_SingleUser_NotInBlacklist_Success() {
        // Given
        IpaClient client = new IpaClient();
        client.setUserBlacklist(Collections.singletonList("blacklistedUser"));
        User userToFilter = new User();
        userToFilter.setUsername("validUser");

        // When
        User result = ipaUserIdpRequestSenderResultBlackListFilter.filter(client, userToFilter);

        // Then
        assertEquals("validUser", result.getUsername());
    }

    @Test
    void filter_SingleUser_InBlacklist_ReturnsNull() {
        // Given
        IpaClient client = new IpaClient();
        client.setUserBlacklist(Collections.singletonList("blacklistedUser"));
        User userToFilter = new User();
        userToFilter.setUsername("blacklistedUser");

        // When
        User result = ipaUserIdpRequestSenderResultBlackListFilter.filter(client, userToFilter);

        // Then
        assertNull(result);
    }

    @Test
    void filter_UserList_WithSomeInBlacklist_Success() {
        // Given
        IpaClient client = new IpaClient();
        client.setUserBlacklist(Arrays.asList("blacklistedUser1", "blacklistedUser2"));
        User user1 = new User();
        user1.setUsername("validUser1");
        User user2 = new User();
        user2.setUsername("blacklistedUser1");
        User user3 = new User();
        user3.setUsername("validUser2");
        List<User> usersToFilter = Arrays.asList(user1, user2, user3);

        // When
        List<User> result = ipaUserIdpRequestSenderResultBlackListFilter.filter(client, usersToFilter);

        // Then
        assertEquals(2, result.size());
        assertEquals("validUser1", result.get(0).getUsername());
        assertEquals("validUser2", result.get(1).getUsername());
    }

    @Test
    void filter_UserList_AllInBlacklist_ReturnsEmptyList() {
        // Given
        IpaClient client = new IpaClient();
        client.setUserBlacklist(Arrays.asList("blacklistedUser1", "blacklistedUser2"));
        User user1 = new User();
        user1.setUsername("blacklistedUser1");
        User user2 = new User();
        user2.setUsername("blacklistedUser2");
        List<User> usersToFilter = Arrays.asList(user1, user2);

        // When
        List<User> result = ipaUserIdpRequestSenderResultBlackListFilter.filter(client, usersToFilter);

        // Then
        assertEquals(0, result.size());
    }
}
