package com.iliauni.usersyncglobalservice.controller;

import com.iliauni.usersyncglobalservice.model.ApiAccessKcClient;
import com.iliauni.usersyncglobalservice.service.Oauth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/secured/client/API/KC")
@RestController
public class ApiAccessKcClientController {
    Oauth2ClientService<ApiAccessKcClient> clientService;

    @Autowired
    public ApiAccessKcClientController(Oauth2ClientService<ApiAccessKcClient> clientService) {
        this.clientService = clientService;
    }

    @PostMapping()
    public ApiAccessKcClient save(@RequestBody ApiAccessKcClient client) {
        return clientService.save(Optional.ofNullable(client));
    }

    @GetMapping()
    public List<ApiAccessKcClient> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public ApiAccessKcClient findById(@PathVariable String id) {
        return clientService.findById(id);
    }

    @PutMapping()
    public ApiAccessKcClient update(@RequestBody ApiAccessKcClient client) {
        return clientService.update(Optional.ofNullable(client));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        clientService.deleteById(id);
    }
}
