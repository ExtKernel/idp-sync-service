package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.util.MultiValueMap;

public interface IdpObjectMapper {
    MultiValueMap<String, Object> mapUserToMap(User user);
    MultiValueMap<String, Object> mapUsergroupToMap(Usergroup usergroup);
    User mapUserMapToUser(MultiValueMap<String, Object> userMap);
    Usergroup mapUsergroupMapToUsergroup(MultiValueMap<String, Object> usergroupMap);
    MultiValueMap<String, Object> buildUserCredentialsMap(String password);
}
