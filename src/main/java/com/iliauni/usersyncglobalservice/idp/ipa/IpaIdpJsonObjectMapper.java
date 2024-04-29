package com.iliauni.usersyncglobalservice.idp.ipa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpJsonObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpMapObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
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
    public String mapUserToJsonString(User user)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(mapObjectMapper.mapUserToMap(user));
    }

    @Override
    public String mapUsergroupToJsonString(Usergroup usergroup)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(mapObjectMapper.mapUsergroupToMap(usergroup));
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
