package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface IdpUserRequestSender<T extends Client> {
    User createUser(T client, User user);
    User getUser(T client, String username);
    List<User> getUsers(T client);
    MultiValueMap<String, Object> updateUserPassword(T client, String username, String newPassword);
    void deleteUser(T client, String username);
}
