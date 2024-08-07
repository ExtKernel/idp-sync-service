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
                userJsonNode.path("uid").isArray() && !userJsonNode.path("uid").isEmpty()
                        ? userJsonNode.path("uid").get(0).asText()
                        : userJsonNode.asText(),
                userJsonNode.path("givenname").isArray() && !userJsonNode.path("givenname").isEmpty()
                        ? userJsonNode.path("givenname").get(0).asText(null)
                        : null,
                userJsonNode.path("sn").isArray() && !userJsonNode.path("sn").isEmpty()
                        ? userJsonNode.path("sn").get(0).asText(null)
                        : null,
                userJsonNode.path("mail").isArray() && !userJsonNode.path("mail").isEmpty()
                        ? userJsonNode.path("mail").get(0).asText(null)
                        : null
        );
    }

    @Override
    public Usergroup mapUsergroupJsonNodeToUsergroup(JsonNode usergroupJsonNode) {
        return new Usergroup(
                usergroupJsonNode.path("cn").get(0).asText(),
                usergroupJsonNode.path("description").isArray() && !usergroupJsonNode.path("description").isEmpty()
                        ? usergroupJsonNode.path("description").get(0).asText(null)
                        : null
        );
    }
}
