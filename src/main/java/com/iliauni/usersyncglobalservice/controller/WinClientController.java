package com.iliauni.usersyncglobalservice.controller;

import com.iliauni.usersyncglobalservice.model.WinClient;
import com.iliauni.usersyncglobalservice.service.Oauth2ClientService;
import com.iliauni.usersyncglobalservice.service.WinClientService;
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
    public WinClient save(@RequestBody WinClient client) {
        return clientService.save(Optional.ofNullable(client));
    }

    @GetMapping()
    public List<WinClient> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public WinClient findById(@PathVariable String id) {
        return clientService.findById(id);
    }

    @PutMapping()
    public WinClient update(@RequestBody WinClient client) {
        return clientService.update(Optional.ofNullable(client));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        clientService.deleteById(id);
    }
}
