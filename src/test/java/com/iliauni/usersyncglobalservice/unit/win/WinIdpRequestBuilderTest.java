package com.iliauni.usersyncglobalservice.unit.win;

import com.iliauni.usersyncglobalservice.exception.ClientHasNoFqdnOrIpAndPortException;
import com.iliauni.usersyncglobalservice.exception.KcClientHasNoKcFqdnOrIpAndPortException;
import com.iliauni.usersyncglobalservice.exception.KcClientHasNoKcRealmException;
import com.iliauni.usersyncglobalservice.model.WinClient;
import com.iliauni.usersyncglobalservice.service.WinClientService;
import com.iliauni.usersyncglobalservice.win.WinIdpRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WinIdpRequestBuilderTest {

    @Mock
    private WinClientService clientService;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @InjectMocks
    private WinIdpRequestBuilder winIdpRequestBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buildRequestUrl_WithFqdn_Success() {
        // Given
        WinClient client = new WinClient();
        client.setId("1");
        client.setFqdn("example.com");
        String protocol = "https";
        String endpoint = "/api/resource";

        // When
        String result = winIdpRequestBuilder.buildRequestUrl(client, protocol, endpoint);

        // Then
        assertEquals("https://example/api/resource", result);
    }

    @Test
    void buildRequestUrl_WithIp_Success() {
        // Given
        WinClient client = new WinClient();
        client.setId("1");
        client.setIp("192.168.0.1");
        client.setPort("8080");
        String protocol = "https";
        String endpoint = "/api/resource";

        // When
        String result = winIdpRequestBuilder.buildRequestUrl(client, protocol, endpoint);

        // Then
        assertEquals("https://192.168.0.1:8080/api/resource", result);
    }

    @Test
    void buildRequestUrl_NoFqdnOrIp_ThrowsException() {
        // Given
        WinClient client = new WinClient();
        client.setId("1");
        String protocol = "https";
        String endpoint = "/api/resource";

        // When / Then
        assertThrows(ClientHasNoFqdnOrIpAndPortException.class, () -> {
            winIdpRequestBuilder.buildRequestUrl(client, protocol, endpoint);
        });
    }

    @Test
    void buildAuthRequestUrl_WithFqdn_Success() {
        // Given
        WinClient client = new WinClient();
        client.setId("1");
        client.setKcFqdn("example.com");
        client.setPort("8443");
        client.setRealm("testRealm");
        String protocol = "https";

        // When
        String result = winIdpRequestBuilder.buildAuthRequestUrl(client, protocol);

        // Then
        assertEquals("https://example.com:8443/realms/testRealm/protocol/openid-connect/token", result);
    }

    @Test
    void buildAuthRequestUrl_WithIp_Success() {
        // Given
        WinClient client = new WinClient();
        client.setId("1");
        client.setKcIp("192.168.0.1");
        client.setPort("8443");
        client.setRealm("testRealm");
        String protocol = "https";

        // When
        String result = winIdpRequestBuilder.buildAuthRequestUrl(client, protocol);

        // Then
        assertEquals("https://192.168.0.1:8443/realms/testRealm/protocol/openid-connect/token", result);
    }

    @Test
    void buildAuthRequestUrl_NoFqdnOrIp_ThrowsException() {
        // Given
        WinClient client = new WinClient();
        client.setId("1");
        client.setRealm("testRealm"); // Set the realm
        String protocol = "https";

        // When / Then
        assertThrows(KcClientHasNoKcFqdnOrIpAndPortException.class, () -> {
            winIdpRequestBuilder.buildAuthRequestUrl(client, protocol);
        });
    }

    @Test
    void buildAuthRequestUrl_NoRealm_ThrowsException() {
        // Given
        WinClient client = new WinClient();
        client.setId("1");
        client.setKcFqdn("example.com");
        client.setPort("8443");
        String protocol = "https";

        // When / Then
        assertThrows(KcClientHasNoKcRealmException.class, () -> {
            winIdpRequestBuilder.buildAuthRequestUrl(client, protocol);
        });
    }
}
