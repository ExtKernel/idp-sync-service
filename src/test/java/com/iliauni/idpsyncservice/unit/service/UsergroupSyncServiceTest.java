package com.iliauni.idpsyncservice.unit.service;

import com.iliauni.idpsyncservice.difference.DifferenceCalculator;
import com.iliauni.idpsyncservice.exception.NoRecordOfUsergroupsException;
import com.iliauni.idpsyncservice.idp.IdpUsergroupManager;
import com.iliauni.idpsyncservice.idp.UsergroupDbSyncHandler;
import com.iliauni.idpsyncservice.idp.UsergroupIdpSyncHandler;
import com.iliauni.idpsyncservice.model.Client;
import com.iliauni.idpsyncservice.model.IdpClient;
import com.iliauni.idpsyncservice.model.IdpClientFactory;
import com.iliauni.idpsyncservice.model.Usergroup;
import com.iliauni.idpsyncservice.service.UsergroupService;
import com.iliauni.idpsyncservice.service.UsergroupSyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class UsergroupSyncServiceTest {

    @Mock
    private UsergroupService usergroupService;

    @Mock
    private DifferenceCalculator<Usergroup> usergroupDifferenceCalculator;

    @Mock
    private UsergroupIdpSyncHandler usergroupIdpSyncHandler;

    @Mock
    private UsergroupDbSyncHandler usergroupDbSyncHandler;

    @Mock
    private IdpUsergroupManager usergroupManager;

    @Mock
    private IdpClientFactory idpClientFactory;

    @InjectMocks
    private UsergroupSyncService usergroupSyncService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sync_Success() {
        // Given
        List<Usergroup> usergroups = List.of(new Usergroup("group1"), new Usergroup("group2"));
        List<Usergroup> dbUsergroups = List.of(new Usergroup("group1"));
        Map<String, List<Usergroup>> differenceMap = new HashMap<>();
        differenceMap.put("toAdd", List.of(new Usergroup("group2")));
        differenceMap.put("toRemove", new ArrayList<>());

        when(usergroupService.findAll()).thenReturn(dbUsergroups);
        when(usergroupDifferenceCalculator.calculate(dbUsergroups, usergroups)).thenReturn(differenceMap);
        doNothing().when(usergroupDbSyncHandler).sync(differenceMap);

        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
        when(idpClientFactory.getAllClients()).thenReturn(new ArrayList<>());

        // When
        assertDoesNotThrow(() -> usergroupSyncService.sync(usergroups));

        // Then
        verify(usergroupService).findAll();
        verify(usergroupDifferenceCalculator).calculate(dbUsergroups, usergroups);
        verify(usergroupDbSyncHandler).sync(differenceMap);
    }

    @Test
    void sync_NoRecordOfUsergroupsException() {
        // Given
        List<Usergroup> usergroups = List.of(new Usergroup("group1"));
        when(usergroupService.findAll()).thenThrow(new NoRecordOfUsergroupsException("No usergroups found"));

        // When
        assertDoesNotThrow(() -> usergroupSyncService.sync(usergroups));

        // Then
        verify(usergroupService).findAll();
    }

    @Test
    void syncFromAllIdps_Success() {
        // Given
        IdpClient<Client> idpClient = mock(IdpClient.class);
        Client client = mock(Client.class);
        List<IdpClient<? extends Client>> idpClients = List.of(idpClient);
        List<Client> clients = List.of(client);
        when(idpClientFactory.getAllClients()).thenReturn(idpClients);
        when(idpClient.getClients()).thenReturn(clients);
        when(idpClient.getUsergroupManager()).thenReturn(usergroupManager);
        when(idpClient.getUsergroupManager().getUsergroups(client)).thenReturn(new ArrayList<>());
        when(idpClient.getUsergroupSyncHandler()).thenReturn(usergroupIdpSyncHandler);

        // When
        CompletableFuture<Void> future = usergroupSyncService.syncFromAllIdps();
        future.join();

        // Then
        verify(idpClientFactory, times(2)).getAllClients();
        verify(idpClient, times(2)).getClients();
        verify(idpClient.getUsergroupManager()).getUsergroups(client);
    }
}
