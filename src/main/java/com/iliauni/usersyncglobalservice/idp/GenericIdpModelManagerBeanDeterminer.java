package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.exception.ClientNameDoesntFollowConventionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * Determines and returns beans corresponding to the model's (Client's) type.
 * Utilizes reflection to handle generic classes that use autowiring of non-generic
 * classes. This solves the issue where classes operate with the provided model but
 * can't be autowired directly due to the absence of generics.
 */
@Slf4j
@Component
public class GenericIdpModelManagerBeanDeterminer { ;
    private final ApplicationContext context;

    @Autowired
    public GenericIdpModelManagerBeanDeterminer(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Determines and returns an IdpJsonObjectMapper bean of corresponding to the specified client's type.
     *
     * @return an IdpJsonObjectMapper implementation for {@link T} client.
     */
    public IdpJsonObjectMapper determineJsonObjectMapper(Type clientType) {
        try {
            String beanName = getClientName(clientType) + "IdpJsonObjectMapper";

            // check if the bean exists
            if (context.containsBean(beanName)) {
                return context.getBean(beanName, IdpJsonObjectMapper.class);
            } else {
                throw new IllegalArgumentException(
                        "No matching IdpJsonObjectMapper bean found for client type: "
                                + clientType.getClass().getSimpleName()
                );
            }
        } catch (ClientNameDoesntFollowConventionException exception) {
            log.error("ClientNameDoesntFollowConventionException occurred: " + exception.getMessage());
            return null;
        }
    }

    /**
     * Determines and returns an IdpUsergroupRequestSender bean of corresponding to the specified client's type.
     *
     * @param clientType the type of client to find the bean for.
     * @return an IdpUsergroupRequestSender implementation for the client.
     */
    public IdpUsergroupRequestSender determineIdpUsergroupRequestSender(Type clientType) {
        try {
            String beanName = getClientName(clientType) + "IdpUsergroupRequestSender";

            // check if the bean exists
            if (context.containsBean(beanName)) {
                return context.getBean(beanName, IdpUsergroupRequestSender.class);
            } else {
                throw new IllegalArgumentException(
                        "No matching IdpUsergroupRequestSender bean found for client class: "
                                + clientType.getClass().getSimpleName()
                );
            }
        } catch (ClientNameDoesntFollowConventionException exception) {
            log.error("ClientNameDoesntFollowConventionException occurred: " + exception.getMessage());
            return null;
        }
    }

    /**
     * Determines and returns an IdpUserRequestSender bean of corresponding to the specified client's type.

     * @param clientType the type of client to find the bean for.
     * @return an IdpUserRequestSender implementation for the client.
     */
    public IdpUserRequestSender determineIdpUserRequestSender(Type clientType) {
        try {
            String beanName = getClientName(clientType) + "IdpUserRequestSender";

            // check if the bean exists
            if (context.containsBean(beanName)) {
                return context.getBean(beanName, IdpUserRequestSender.class);
            } else {
                throw new IllegalArgumentException(
                        "No matching IdpUserRequestSender bean found for client class: "
                                + clientType.getClass().getSimpleName()
                );
            }
        } catch (ClientNameDoesntFollowConventionException exception) {
            log.error("ClientNameDoesntFollowConventionException occurred: " + exception.getMessage());
            return null;
        }
    }

    /**
     * Returns a name of the specified client, based on getting class name until "Client" substring appears.
     * Works only if the convention of naming a client model was followed correctly: exampleClient,
     * where "example" can be replaced with any desirable name.
     *
     * @return name of the client's class in lowercase until "Class" substring.
     * @throws ClientNameDoesntFollowConventionException if client's class name doesn't follow the convention.
     *                                                   Simply, because it'd be impossible to know what to extract.
     */
    private String getClientName(Type clientType) throws ClientNameDoesntFollowConventionException {
        // construct bean name based on client type
        // assuming client object's class name ends with "Client"
        // if not, it'll break
        String clientName = clientType.getClass().getSimpleName().toLowerCase();

        if (clientName.contains("client")) {
            return clientName.split("client")[0];
        } else {
            throw new ClientNameDoesntFollowConventionException(
                    "Client class with name " + clientType.getClass().getSimpleName()
                            + " has invalid class name, which doesn't follow the convention of exampleClient"
            );
        }
    }
}
