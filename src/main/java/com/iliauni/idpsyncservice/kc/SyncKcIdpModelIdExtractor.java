package com.iliauni.idpsyncservice.kc;

import com.fasterxml.jackson.databind.JsonNode;
import com.iliauni.idpsyncservice.exception.KcUserWithUsernameNotFoundException;
import com.iliauni.idpsyncservice.exception.KcUsergroupWithNameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SyncKcIdpModelIdExtractor {

    /**
     * Retrieves an ID of the user group with the specified name among groups in the given JSON node.
     *
     * @param usergroups a JSON node of user group list.
     * @param usergroupName a name of the user group.
     * @return the ID of the user group.
     * @throws KcUsergroupWithNameNotFoundException if a user group with the specified name is not found.
     */
    public String getUsergroupId(
            JsonNode usergroups,
            String usergroupName
    ) {
        for (JsonNode usergroup : usergroups) {
            if (usergroup.path("name").asText().equals(usergroupName)) {
                return usergroup.path("id").asText();
            }
        }
        throw new KcUsergroupWithNameNotFoundException(
                "Keycloak user group with name "
                        + usergroupName
                        + " was not found"
        );
    }

    /**
     * Retrieves an ID of a user with the specified name among users in the given JSON node.
     *
     * @param users a JSON node of user list.
     * @param username the username of the user.
     * @return the ID of the user.
     * @throws KcUserWithUsernameNotFoundException if a user with the specified username is not found.
     */
    public String getUserId(
            JsonNode users,
            String username
    ) {
        for (JsonNode user : users) {
            if (user.path("username").asText().equals(username)) {
                return user.path("id").asText();
            }
        }
        throw new KcUserWithUsernameNotFoundException(
                "Keycloak user with username "
                        + username
                        + " was not found"
        );
    }
}
