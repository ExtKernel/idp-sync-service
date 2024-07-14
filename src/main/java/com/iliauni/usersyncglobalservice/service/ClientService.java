package com.iliauni.usersyncglobalservice.service;

import com.iliauni.usersyncglobalservice.model.Client;

public interface ClientService<T extends Client> extends CrudService<T, String> {
}
