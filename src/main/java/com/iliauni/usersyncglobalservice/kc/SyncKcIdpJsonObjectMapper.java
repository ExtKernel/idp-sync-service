package com.iliauni.usersyncglobalservice.kc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.exception.CredentialsRepresentationBuildingException;
import com.iliauni.usersyncglobalservice.exception.UserToJsonMappingException;
import com.iliauni.usersyncglobalservice.exception.UsergroupToJsonMappingException;
import com.iliauni.usersyncglobalservice.idp.IdpJsonObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpMapObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SyncKcIdpJsonObjectMapper implements IdpJsonObjectMapper {
    private final IdpMapObjectMapper mapObjectMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public SyncKcIdpJsonObjectMapper(
            @Qualifier("syncKcIdpMapObjectMapper") IdpMapObjectMapper mapObjectMapper,
            ObjectMapper objectMapper
    ) {
        this.mapObjectMapper = mapObjectMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public String mapUserToJsonString(User user) {
        try {
            return objectMapper.writeValueAsString(mapObjectMapper.mapUserToMap(user));
        } catch (JsonProcessingException exception) {
            throw new UserToJsonMappingException(
                    "An exception occurred while mapping a Keycloak user to a JSON string: "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public String mapUsergroupToJsonString(Usergroup usergroup) {
        try {
            return objectMapper.writeValueAsString(mapObjectMapper.mapUsergroupToMap(usergroup));
        } catch (JsonProcessingException exception) {
            throw new UsergroupToJsonMappingException(
                    "An exception occurred while mapping a Keycloak user group to a JSON string: "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    /**
     * @throws CredentialsRepresentationBuildingException if there was a problem while writing
     *                                                    a user credentials map
     *                                                    with the given password as a JSON.
     */
    @Override
    public String buildCredentialsRepresentation(String password) {
        try {
            return objectMapper.writeValueAsString(
                    mapObjectMapper.buildUserCredentialsMap(password)
            );
        } catch (JsonProcessingException exception) {
            throw new CredentialsRepresentationBuildingException(
                    "An exception occurred while building credentials representation: "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public User mapUserJsonNodeToUser(JsonNode userJsonNode) {
        return new User(
                userJsonNode.path("username").asText(),
                userJsonNode.path("firstName").asText(),
                userJsonNode.path("lastName").asText(),
                userJsonNode.path("email").asText()
        );
    }

    @Override
    public Usergroup mapUsergroupJsonNodeToUsergroup(JsonNode usergroupJsonNode) {
        return new Usergroup(
                usergroupJsonNode.path("name").asText()
        );
    }
}
