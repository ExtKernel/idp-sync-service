package com.iliauni.idpsyncservice.ipa;

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

@Component
public class IpaIdpJsonObjectMapper implements IdpJsonObjectMapper {
    private final IdpMapObjectMapper mapObjectMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public IpaIdpJsonObjectMapper(
            @Qualifier("ipaIdpMapObjectMapper") IdpMapObjectMapper mapObjectMapper,
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
                    "An exception occurred while mapping a FreeIPA user to a JSON string: "
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
                    "An exception occurred while mapping a FreeIPA user group to a JSON string: "
                            + exception.getMessage(),
                    exception
            );
        }
    }

    @Override
    public String buildCredentialsRepresentation(String password) {
        return password;
    }

    @Override
    public User mapUserJsonNodeToUser(JsonNode userJsonNode) {
        return new User(
                userJsonNode.path("uid").asText(),
                userJsonNode.path("givenname").asText(),
                userJsonNode.path("sn").asText(),
                userJsonNode.path("mail").asText()
        );
    }

    @Override
    public Usergroup mapUsergroupJsonNodeToUsergroup(JsonNode usergroupJsonNode) {
        return new Usergroup(
                usergroupJsonNode.path("cn").asText(),
                usergroupJsonNode.path("description").asText()
        );
    }
}
