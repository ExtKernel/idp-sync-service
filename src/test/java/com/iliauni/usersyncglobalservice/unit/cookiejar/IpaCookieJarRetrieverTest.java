package com.iliauni.usersyncglobalservice.unit.cookiejar;

import com.iliauni.usersyncglobalservice.cookiejar.IpaCookieJarRequestSender;
import com.iliauni.usersyncglobalservice.cookiejar.IpaCookieJarRetriever;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IpaCookieJarRetrieverTest {
    @Mock
    private IpaCookieJarRequestSender<IpaClient> ipaIdpUserRequestSender;

    @InjectMocks
    private IpaCookieJarRetriever<IpaClient> ipaCookieJarRetriever;

    @Test
    public void retrieveCookieJar_WhenGivenClient_ShouldReturnCookie()
            throws Exception {
        IpaClient client = new IpaClient();
        client.setId("test-id");

        String cookie = "test-cookie";

        when(ipaIdpUserRequestSender.getCookie(client)).thenReturn(cookie);

        assertEquals(cookie, ipaCookieJarRetriever.retrieveCookieJar(client));
    }
}
