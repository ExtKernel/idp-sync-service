package com.iliauni.idpsyncservice.idp;

import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.User;
import java.util.List;

public abstract class GenericIdpModelExistenceValidator<T extends Client> implements IdpModelExistenceValidator<T> {
    private final IdpUsergroupManager<T> usergroupManager;
    private final IdpUserManager<T> userManager;

    public GenericIdpModelExistenceValidator(
            IdpUsergroupManager<T> usergroupManager,
            IdpUserManager<T> userManager
    ) {
        this.usergroupManager = usergroupManager;
        this.userManager = userManager;
    }

    @Override
    public boolean validateUsergroupExistence(
            T client,
            String usergroupName
    ) {
        return usergroupManager.getUsergroups(client).stream()
                .anyMatch(usergroup -> usergroup.getName().equals(usergroupName));
    }

    @Override
    public boolean validateUserExistence(
            T client,
            String username
    ) {
        return validateUserExistenceInList(
                userManager.getUsers(client),
                username
        );
    }

    @Override
    public boolean validateUsergroupMemberExistence(
            T client,
            String usergroupName,
            String username
    ) {
        // turn off the validation, because it's unnecessary and causes stackoverflow
        return validateUserExistenceInList(
                usergroupManager.getUsergroupMembers(
                        client,
                        usergroupName,
                        false
                ),
                username
        );
    }

    /**
     * Validates that user exists in the provided lists using username.
     *
     * @param users the list of users.
     * @param username the username of the user to check the existence of.
     * @return true, if the user exists in the list. False, if it doesn't.
     */
    private boolean validateUserExistenceInList(
            List<User> users,
            String username
    ) {
        return users.stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }
}
