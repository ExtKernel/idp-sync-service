package com.iliauni.idpsyncservice.controller;

import com.iliauni.idpsyncservice.exception.ClientIsNullException;
import com.iliauni.idpsyncservice.exception.ClientNotFoundException;
import com.iliauni.idpsyncservice.exception.NoRecordOfClientsException;
import com.iliauni.idpsyncservice.model.ApiAccessKcClient;
import com.iliauni.idpsyncservice.service.Oauth2ClientService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/secured/client/API/KC")
@RestController
public class ApiAccessKcClientController {
    private final Oauth2ClientService<ApiAccessKcClient> clientService;

    @Autowired
    public ApiAccessKcClientController(Oauth2ClientService<ApiAccessKcClient> clientService) {
        this.clientService = clientService;
    }

    @PostMapping()
    public ApiAccessKcClient save(@RequestBody ApiAccessKcClient client) {
        try {
            return clientService.save(Optional.ofNullable(client));
        } catch (ClientIsNullException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping()
    public List<ApiAccessKcClient> findAll() {
        try {
            return clientService.findAll();
        } catch (NoRecordOfClientsException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ApiAccessKcClient findById(@PathVariable String id) {
        try {
            return clientService.findById(id);
        } catch (ClientNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping()
    public ApiAccessKcClient update(@RequestBody ApiAccessKcClient client) throws ClientIsNullException {
        return clientService.update(Optional.ofNullable(client));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        clientService.deleteById(id);
    }
}
