package com.iliauni.idpsyncservice.controller;

import com.iliauni.idpsyncservice.exception.ClientIsNullException;
import com.iliauni.idpsyncservice.exception.ClientNotFoundException;
import com.iliauni.idpsyncservice.exception.NoRecordOfClientsException;
import com.iliauni.idpsyncservice.model.WinClient;
import com.iliauni.idpsyncservice.service.Oauth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/secured/client/Win")
@RestController
public class WinClientController {
    Oauth2ClientService<WinClient> clientService;

    @Autowired
    public WinClientController(Oauth2ClientService<WinClient> clientService) {
        this.clientService = clientService;
    }

    @PostMapping()
    public WinClient save(@RequestBody WinClient client) throws ClientIsNullException {
        return clientService.save(Optional.ofNullable(client));
    }

    @GetMapping()
    public List<WinClient> findAll() throws NoRecordOfClientsException {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public WinClient findById(@PathVariable String id) throws ClientNotFoundException {
        return clientService.findById(id);
    }

    @PutMapping()
    public WinClient update(@RequestBody WinClient client) throws ClientIsNullException {
        return clientService.update(Optional.ofNullable(client));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        clientService.deleteById(id);
    }
}
