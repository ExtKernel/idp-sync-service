package com.iliauni.idpsyncservice.win;

import com.iliauni.idpsyncservice.idp.*;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.WinClient;
import com.iliauni.idpsyncservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * A component class implementing the {@link IdpUserManager} interface
 * for managing User operations in an Identity Provider (IDP) context specific to Windows.
 */
@Component
public class WinIdpUserManager extends GenericIdpUserManager<WinClient> {

    private final IdpUsergroupManager<WinClient> winIdpUsergroupManager;

    @Autowired
    public WinIdpUserManager(
            ClientService<WinClient> clientService,
            @Qualifier("winIdpJsonObjectMapper") IdpJsonObjectMapper jsonObjectMapper,
            IdpUserRequestSender<WinClient> requestSender,
            @Lazy IdpModelExistenceValidator<WinClient> modelExistenceValidator,
            UserIdpRequestSenderResultBlackListFilter<WinClient> blackListFilter,
            IdpUsergroupManager<WinClient> winIdpUsergroupManager
    ) {
        super(
                clientService,
                jsonObjectMapper,
                requestSender,
                modelExistenceValidator,
                blackListFilter
        );
        this.winIdpUsergroupManager = winIdpUsergroupManager;
    }

    @Override
    public synchronized User createUser(
            WinClient client,
            User user,
            boolean validate
    ) {
        validateUserDoesNotExists(
                client,
                user.getUsername()
        );

        User createdUser = super.getRequestSender().sendCreateUserRequest(
                client,
                user
        );

        // clear the cache after modifying users on the client
        // if not cleared,
        // ModelExistenceValidator and other classes
        // that depend on the output of getUsers are likely to break
        super.getClientService().clearClientUserCache(client);

        // add the user to the default "Users" group
        // otherwise it's impossible to log in
        // turn off the validation, bc it'll likely blacklist the "Users" group and throw an exception
        // while the group exists anyway and there's no need to validate
        winIdpUsergroupManager.addUsergroupMember(
                client,
                "Users",
                user.getUsername(),
                false
        );

        return createdUser;
    }
}
