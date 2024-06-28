package com.iliauni.usersyncglobalservice.unit.ipa;

import com.iliauni.usersyncglobalservice.ipa.IpaIdpModelExistenceValidator;
import com.iliauni.usersyncglobalservice.ipa.IpaIdpUserManager;
import com.iliauni.usersyncglobalservice.ipa.IpaIdpUsergroupManager;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IpaIdpModelExistenceValidatorTest {

    @Mock
    private IpaIdpUsergroupManager usergroupManager;

    @Mock
    private IpaIdpUserManager userManager;

    @InjectMocks
    private IpaIdpModelExistenceValidator validator;

    private IpaClient client;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        client = new IpaClient();
        user1 = new User();
        user1.setUsername("user1");
        user2 = new User();
        user2.setUsername("user2");
    }

    @Test
    void testValidateUsergroupExistence_UsergroupExists() {
        when(usergroupManager.getUsergroups(client)).thenReturn(Arrays.asList(
                new Usergroup("group1"),
                new Usergroup("group2")
        ));

        assertTrue(validator.validateUsergroupExistence(client, "group1"));
        assertFalse(validator.validateUsergroupExistence(client, "group3"));
    }

    @Test
    void testValidateUserExistence_UserExists() {
        when(userManager.getUsers(client)).thenReturn(Arrays.asList(user1, user2));

        assertTrue(validator.validateUserExistence(client, "user1"));
        assertFalse(validator.validateUserExistence(client, "user3"));
    }

    @Test
    void testValidateUsergroupMemberExistence_UserExistsInUsergroup() {
        when(usergroupManager.getUsergroupMembers(client, "group1")).thenReturn(Arrays.asList(user1, user2));

        assertTrue(validator.validateUsergroupMemberExistence(client, "group1", "user1"));
        assertFalse(validator.validateUsergroupMemberExistence(client, "group1", "user3"));
    }
}
