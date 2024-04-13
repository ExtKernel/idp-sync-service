package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;

import java.util.List;

public interface IdpUsergroupRequestSender<T extends Client> {
    Usergroup createUsergroup(T client, Usergroup usergroup);
    Usergroup getUsergroup(T client, String usergroupName);
    List<Usergroup> getUsergroups(T client);
    void deleteUsergroup(T client, String usergroupName);
}
