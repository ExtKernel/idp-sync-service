package com.iliauni.idpsyncservice.win;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.idpsyncservice.exception.UserToJsonMappingException;
import com.iliauni.idpsyncservice.exception.UsergroupToJsonMappingException;
import com.iliauni.idpsyncservice.idp.IdpJsonObjectMapper;
import com.iliauni.idpsyncservice.idp.IdpMapObjectMapper;
import com.iliauni.idpsyncservice.model.User;
import com.iliauni.idpsyncservice.model.Usergroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WinIdpJsonObjectMapper implements IdpJsonObjectMapper {
    private final IdpMapObjectMapper mapObjectMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public WinIdpJsonObjectMapper(
            @Qualifier("winIdpMapObjectMapper") IdpMapObjectMapper mapObjectMapper,
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
                    "An exception occurred while mapping a Win user to a JSON string: "
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
                    "An exception occurred while mapping a Win user group to a JSON string: "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public String buildCredentialsRepresentation(String password) {
        try {
            return objectMapper.writeValueAsString(mapObjectMapper.buildUserCredentialsMap(password));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User mapUserJsonNodeToUser(JsonNode userJsonNode) {
        return new User(
                userJsonNode.path("username").asText()
        );
    }

    @Override
    public Usergroup mapUsergroupJsonNodeToUsergroup(JsonNode usergroupJsonNode) {
        return new Usergroup(
                usergroupJsonNode.path("name").asText(),
                usergroupJsonNode.path("description").asText(),
                extractUserListFromUsergroupJson(usergroupJsonNode.path("users"))
        );
    }

    private List<User> extractUserListFromUsergroupJson(JsonNode usersNode) {
        List<User> users = new ArrayList<>();

        for (JsonNode user : usersNode) {
            users.add(new User(user.asText()));
        }

        return users;
    }
}
