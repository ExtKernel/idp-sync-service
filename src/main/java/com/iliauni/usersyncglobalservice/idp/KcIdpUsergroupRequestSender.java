package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.Client;
import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.model.User;
import com.iliauni.usersyncglobalservice.model.Usergroup;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class KcIdpUsergroupRequestSender<T extends KcClient> implements IdpUsergroupRequestSender<T> {
    @Value("${kcRealm}")
    private String kcRealm;

    @Value("${KC_BASE_URI}")
    private String kcBaseUri;

    @Override
    public Usergroup createUsergroup(T client, User user) {
        return null;
    }

    @Override
    public Usergroup getUsergroup(T client, String username) {
        return null;
    }

    @Override
    public List<Usergroup> getUsergroups(T client) {
        return null;
    }

    @Override
    public Usergroup updateUsergroupPassword(Client client, String username, String newPassword) {
        return null;
    }

    @Override
    public void deleteUsergroup(T client, String username) {

    }
}
