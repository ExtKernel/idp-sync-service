package com.iliauni.idpsyncservice.idp;

import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.Usergroup;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericUsergroupIdpRequestSenderResultBlackListFilter<T extends Client> implements UsergroupIdpRequestSenderResultBlackListFilter<T> {

    @Override
    public Usergroup filter(
            T client,
            Usergroup usergroupToFilter
    ) {
        return client.getUsergroupBlacklist().stream()
                .anyMatch(usergroupName -> usergroupName.equals(usergroupToFilter.getName()))
                ? null : usergroupToFilter;
    }

    @Override
    public List<Usergroup> filter(
            T client,
            List<Usergroup> usergroupsToFilter
    ) {
        return usergroupsToFilter.stream()
                .filter(usergroup ->
                        client.getUsergroupBlacklist().stream()
                                .noneMatch(usergroupName -> usergroupName.equals(usergroup.getName())))
                .collect(Collectors.toList());
    }
}
