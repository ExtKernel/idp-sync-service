package com.iliauni.usersyncglobalservice.unit.win;

import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import com.iliauni.usersyncglobalservice.model.WinClient;
import com.iliauni.usersyncglobalservice.win.WinIdpModelExistenceValidator;
import com.iliauni.usersyncglobalservice.win.WinIdpUserManager;
import com.iliauni.usersyncglobalservice.win.WinIdpUsergroupManager;
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
class WinIdpModelExistenceValidatorTest {

    @Mock
    private WinIdpUsergroupManager usergroupManager;

    @Mock
    private WinIdpUserManager userManager;

    @InjectMocks
    private WinIdpModelExistenceValidator validator;

    private WinClient client;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        client = new WinClient();
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
        when(usergroupManager.getUsergroupMembers(client, "group1", false)).thenReturn(Arrays.asList(user1, user2));

        assertTrue(validator.validateUsergroupMemberExistence(client, "group1", "user1"));
        assertFalse(validator.validateUsergroupMemberExistence(client, "group1", "user3"));
    }
}
