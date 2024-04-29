package com.iliauni.usersyncglobalservice.idp.win;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpJsonObjectMapper;
import com.iliauni.usersyncglobalservice.idp.IdpMapObjectMapper;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

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
                userJsonNode.path("username").asText()
        );
    }

//    mapUsersJsonNodeToUserList(JsonNode usersJsonNode) {
//
//    }

    @Override
    public Usergroup mapUsergroupJsonNodeToUsergroup(JsonNode usergroupJsonNode) {
        return new Usergroup(
                usergroupJsonNode.path("name").asText(),
                usergroupJsonNode.path("description").asText(),
                extractUserListFromUsergroupJson(usergroupJsonNode.path("users"))
        );
    }

//    @Override
//    public List<Usergroup> mapUsergroupsJsonNodeToUsergroupList(JsonNode usergroupsJsonNode) {
//        List<Usergroup> usergroupList = new ArrayList<>();
//
//        for (JsonNode usergroup : usergroupsJsonNode) {
//            usergroupList.add(mapUsergroupJsonNodeToUsergroup(usergroup));
//        }
//
//        return usergroupList;
//    }

    private List<User> extractUserListFromUsergroupJson(JsonNode usersNode) {
        List<User> users = new ArrayList<>();

        for (JsonNode user : usersNode) {
            users.add(new User(user.asText()));
        }

        return users;
    }
}
