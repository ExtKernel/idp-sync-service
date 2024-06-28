package com.iliauni.usersyncglobalservice.unit.ipa;

import com.iliauni.usersyncglobalservice.exception.ClientHasNoFqdnOrIpAndPortException;
import com.iliauni.usersyncglobalservice.ipa.IpaIdpRequestBuilder;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.service.CookieClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IpaIdpRequestBuilderTest {

    @Mock
    private CookieClientService<IpaClient> clientService;

    @InjectMocks
    private IpaIdpRequestBuilder ipaIdpRequestBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buildRequestUrl_WithFqdn_Success() {
        // Given
        IpaClient client = new IpaClient();
        client.setId("1");
        client.setFqdn("example.com");
        client.setPort("8443");
        String protocol = "https";
        String endpoint = "/api/resource";

        // When
        String result = ipaIdpRequestBuilder.buildRequestUrl(client, protocol, endpoint);

        // Then
        assertEquals("https://example.com:8443/api/resource", result);
    }

    @Test
    void buildRequestUrl_WithIp_Success() {
        // Given
        IpaClient client = new IpaClient();
        client.setId("1");
        client.setIp("192.168.0.1");
        client.setPort("8443");
        String protocol = "https";
        String endpoint = "/api/resource";

        // When
        String result = ipaIdpRequestBuilder.buildRequestUrl(client, protocol, endpoint);

        // Then
        assertEquals("https://192.168.0.1:8443/api/resource", result);
    }

    @Test
    void buildRequestUrl_NoFqdnOrIp_ThrowsException() {
        // Given
        IpaClient client = new IpaClient();
        client.setId("1");
        String protocol = "https";
        String endpoint = "/api/resource";

        // When / Then
        assertThrows(ClientHasNoFqdnOrIpAndPortException.class, () -> {
            ipaIdpRequestBuilder.buildRequestUrl(client, protocol, endpoint);
        });
    }

    @Test
    void buildAuthRequestUrl_WithFqdn_Success() {
        // Given
        IpaClient client = new IpaClient();
        client.setId("1");
        client.setFqdn("example.com");
        client.setPort("8443");
        String protocol = "https";

        // When
        String result = ipaIdpRequestBuilder.buildAuthRequestUrl(client, protocol);

        // Then
        assertEquals("https://example.com:8443/ipa/session/login_password", result);
    }

    @Test
    void buildAuthRequestUrl_WithIp_Success() {
        // Given
        IpaClient client = new IpaClient();
        client.setId("1");
        client.setIp("192.168.0.1");
        client.setPort("8443");
        String protocol = "https";

        // When
        String result = ipaIdpRequestBuilder.buildAuthRequestUrl(client, protocol);

        // Then
        assertEquals("https://192.168.0.1:8443/ipa/session/login_password", result);
    }

    @Test
    void buildAuthRequestUrl_NoFqdnOrIp_ThrowsException() {
        // Given
        IpaClient client = new IpaClient();
        client.setId("1");
        String protocol = "https";

        // When / Then
        assertThrows(ClientHasNoFqdnOrIpAndPortException.class, () -> {
            ipaIdpRequestBuilder.buildAuthRequestUrl(client, protocol);
        });
    }
}
