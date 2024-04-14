package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.KcClient;
import com.iliauni.usersyncglobalservice.service.KcClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
public class KcIdpRequestBuilder<T extends KcClient> implements IdpRequestBuilder<T> {
    private final KcClientService<T> clientService;

    @Value("${kcAdminCliClientId}")
    private String kcAdminCliClientId;

    @Autowired
    public KcIdpRequestBuilder(KcClientService<T> clientService) {
        this.clientService = clientService;
    }

    @Override
    public HttpEntity<MultiValueMap<String, Object>> buildHttpRequestEntity(
            String clientId,
            MultiValueMap<String, Object> requestBody) {
        return new HttpEntity<>(
                requestBody,
                buildHeaders(clientService.generateAccessToken(kcAdminCliClientId).getToken())
        );
    }

    public String buildApiBaseUrl (
            String kcBaseUrl,
            String kcRealm) {
        return "http://" + kcBaseUrl + "/admin/realms/" + kcRealm;
    }

    private HttpHeaders buildHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}
