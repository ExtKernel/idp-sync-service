package com.iliauni.idpsyncservice.unit.win;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.iliauni.idpsyncservice.model.Usergroup;
import com.iliauni.idpsyncservice.model.WinClient;
import com.iliauni.idpsyncservice.win.WinUsergroupIdpRequestSenderResultBlackListFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WinUsergroupIdpRequestSenderResultBlackListFilterTest {

    private WinUsergroupIdpRequestSenderResultBlackListFilter filter;
    private WinClient client;

    // Initialize the WinUsergroupIdpRequestSenderResultBlackListFilter and WinClient instances before each test
    @BeforeEach
    void setUp() {
        filter = new WinUsergroupIdpRequestSenderResultBlackListFilter();
        client = new WinClient();
    }

    // Test filtering a single usergroup that is not in the blacklist
    @Test
    void testFilterSingleUsergroupNotInBlacklist() {
        // Set up the client's blacklist with one usergroup name
        client.setUsergroupBlacklist(Collections.singletonList("blacklistedGroup"));

        // Create a Usergroup object with a name not in the blacklist
        Usergroup usergroup = new Usergroup();
        usergroup.setName("validGroup");

        // Call the filter method
        Usergroup result = filter.filter(client, usergroup);

        // Verify that the usergroup is not filtered out (should be returned as is)
        assertEquals(usergroup, result);
    }

    // Test filtering a single usergroup that is in the blacklist
    @Test
    void testFilterSingleUsergroupInBlacklist() {
        // Set up the client's blacklist with one usergroup name
        client.setUsergroupBlacklist(Collections.singletonList("blacklistedGroup"));

        // Create a Usergroup object with a name in the blacklist
        Usergroup usergroup = new Usergroup();
        usergroup.setName("blacklistedGroup");

        // Call the filter method
        Usergroup result = filter.filter(client, usergroup);

        // Verify that the usergroup is filtered out (should return null)
        assertNull(result);
    }

    // Test filtering a list of usergroups with one usergroup in the blacklist
    @Test
    void testFilterUsergroupList() {
        // Set up the client's blacklist with one usergroup name
        client.setUsergroupBlacklist(Collections.singletonList("blacklistedGroup"));

        // Create a list of usergroups with one valid and one blacklisted usergroup
        Usergroup validUsergroup = new Usergroup();
        validUsergroup.setName("validGroup");

        Usergroup blacklistedUsergroup = new Usergroup();
        blacklistedUsergroup.setName("blacklistedGroup");

        List<Usergroup> usergroups = Arrays.asList(validUsergroup, blacklistedUsergroup);

        // Call the filter method
        List<Usergroup> result = filter.filter(client, usergroups);

        // Verify that the list contains only the valid usergroup
        assertEquals(1, result.size());
        assertEquals(validUsergroup, result.get(0));
    }

    // Test filtering a list of usergroups with no blacklist
    @Test
    void testFilterUsergroupListNoBlacklist() {
        // Set up the client's blacklist as empty
        client.setUsergroupBlacklist(Collections.emptyList());

        // Create a list of usergroups
        Usergroup usergroup1 = new Usergroup();
        usergroup1.setName("group1");

        Usergroup usergroup2 = new Usergroup();
        usergroup2.setName("group2");

        List<Usergroup> usergroups = Arrays.asList(usergroup1, usergroup2);

        // Call the filter method
        List<Usergroup> result = filter.filter(client, usergroups);

        // Verify that the list contains all usergroups
        assertEquals(2, result.size());
        assertEquals(usergroup1, result.get(0));
        assertEquals(usergroup2, result.get(1));
    }
}
