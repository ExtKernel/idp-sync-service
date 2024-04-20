package com.iliauni.usersyncglobalservice.controller;

import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.service.Oauth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/secured/client/KC")
@RestController
public class KcClientController {
    Oauth2ClientService<KcClient> clientService;

    @Autowired
    public KcClientController(Oauth2ClientService<KcClient> clientService) {
        this.clientService = clientService;
    }

    @PostMapping()
    public KcClient save(@RequestBody KcClient client) {
        return clientService.save(Optional.ofNullable(client));
    }

    @GetMapping()
    public List<KcClient> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public KcClient findById(@PathVariable String id) {
        return clientService.findById(id);
    }

    @PutMapping()
    public KcClient update(@RequestBody KcClient client) {
        return clientService.update(Optional.ofNullable(client));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        clientService.deleteById(id);
    }
}
