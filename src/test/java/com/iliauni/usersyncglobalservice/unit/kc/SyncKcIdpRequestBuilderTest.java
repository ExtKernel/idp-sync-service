package com.iliauni.usersyncglobalservice.unit.kc;

import com.iliauni.usersyncglobalservice.exception.ClientHasNoFqdnOrIpAndPortException;
import com.iliauni.usersyncglobalservice.exception.KcClientHasNoKcFqdnOrIpAndPortException;
import com.iliauni.usersyncglobalservice.kc.SyncKcIdpRequestBuilder;
import com.iliauni.usersyncglobalservice.model.SyncKcClient;
import com.iliauni.usersyncglobalservice.service.SyncKcClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SyncKcIdpRequestBuilderTest {

    @Mock
    private SyncKcClientService clientService;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @InjectMocks
    private SyncKcIdpRequestBuilder syncKcIdpRequestBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buildRequestUrl_WithFqdn_Success() {
        // Given
        SyncKcClient client = new SyncKcClient();
        client.setId("1");
        client.setFqdn("example.com");
        client.setPort("8443");
        client.setRealm("testRealm");
        String protocol = "https";
        String endpoint = "/api/resource";

        // When
        String result = syncKcIdpRequestBuilder.buildRequestUrl(client, protocol, endpoint);

        // Then
        assertEquals("https://example.com:8443/admin/realms/testRealm/api/resource", result);
    }

    @Test
    void buildRequestUrl_WithIp_Success() {
        // Given
        SyncKcClient client = new SyncKcClient();
        client.setId("1");
        client.setIp("192.168.0.1");
        client.setPort("8443");
        client.setRealm("testRealm");
        String protocol = "https";
        String endpoint = "/api/resource";

        // When
        String result = syncKcIdpRequestBuilder.buildRequestUrl(client, protocol, endpoint);

        // Then
        assertEquals("https://192.168.0.1:8443/admin/realms/testRealm/api/resource", result);
    }

    @Test
    void buildRequestUrl_NoFqdnOrIp_ThrowsException() {
        // Given
        SyncKcClient client = new SyncKcClient();
        client.setId("1");
        String protocol = "https";
        String endpoint = "/api/resource";

        // When / Then
        assertThrows(ClientHasNoFqdnOrIpAndPortException.class, () -> {
            syncKcIdpRequestBuilder.buildRequestUrl(client, protocol, endpoint);
        });
    }

    @Test
    void buildAuthRequestUrl_WithFqdn_Success() {
        // Given
        SyncKcClient client = new SyncKcClient();
        client.setId("1");
        client.setKcFqdn("example.com");
        client.setPort("8443");
        String protocol = "https";

        // When
        String result = syncKcIdpRequestBuilder.buildAuthRequestUrl(client, protocol);

        // Then
        assertEquals("https://example.com:8443/realms/master/protocol/openid-connect/token", result);
    }

    @Test
    void buildAuthRequestUrl_WithIp_Success() {
        // Given
        SyncKcClient client = new SyncKcClient();
        client.setId("1");
        client.setKcIp("192.168.0.1");
        client.setPort("8443");
        String protocol = "https";

        // When
        String result = syncKcIdpRequestBuilder.buildAuthRequestUrl(client, protocol);

        // Then
        assertEquals("https://192.168.0.1:8443/realms/master/protocol/openid-connect/token", result);
    }

    @Test
    void buildAuthRequestUrl_NoFqdnOrIp_ThrowsException() {
        // Given
        SyncKcClient client = new SyncKcClient();
        client.setId("1");
        String protocol = "https";

        // When / Then
        assertThrows(KcClientHasNoKcFqdnOrIpAndPortException.class, () -> {
            syncKcIdpRequestBuilder.buildAuthRequestUrl(client, protocol);
        });
    }
}
