package com.iliauni.idpsyncservice.controller;

import com.iliauni.idpsyncservice.exception.ClientIsNullException;
import com.iliauni.idpsyncservice.exception.ClientNotFoundException;
import com.iliauni.idpsyncservice.exception.NoRecordOfClientsException;
import com.iliauni.idpsyncservice.model.IpaClient;
import com.iliauni.idpsyncservice.service.CookieClientService;
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

@RequestMapping("/secured/client/IPA")
@RestController
public class IpaClientController {
    private final CookieClientService<IpaClient> clientService;

    @Autowired
    public IpaClientController(CookieClientService<IpaClient> clientService) {
        this.clientService = clientService;
    }

    @PostMapping()
    public IpaClient save(@RequestBody IpaClient client) throws ClientIsNullException {
        return clientService.save(Optional.ofNullable(client));
    }

    @GetMapping()
    public List<IpaClient> findAll() throws NoRecordOfClientsException {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public IpaClient findById(@PathVariable String id) throws ClientNotFoundException {
        return clientService.findById(id);
    }

    @PutMapping()
    public IpaClient update(@RequestBody IpaClient client) throws ClientIsNullException {
        return clientService.update(Optional.ofNullable(client));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        clientService.deleteById(id);
    }
}
