package com.iliauni.usersyncglobalservice.idp;

import com.iliauni.usersyncglobalservice.model.IpaClient;
import com.iliauni.usersyncglobalservice.service.CookieJarService;
import com.iliauni.usersyncglobalservice.service.IpaClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
public class IpaIdpRequestBuilder<T extends IpaClient> implements IdpRequestBuilder<T> {
    @Value("${ipaHostname}")
    private String ipaHostname;

    CookieJarService<T> cookieJarService;
    IpaClientService<T> ipaClientService;

    @Autowired
    public IpaIdpRequestBuilder(CookieJarService<T> cookieJarService, IpaClientService<T> ipaClientService) {
        this.cookieJarService = cookieJarService;
        this.ipaClientService = ipaClientService;
    }

    @Override
    public HttpEntity<MultiValueMap<String, Object>> buildHttpRequestEntity(String clientId, MultiValueMap<String, Object> requestBody) {
        return new HttpEntity<>(
                requestBody,
                buildHeaders(cookieJarService.generateAndSave(ipaClientService.findById(clientId)).getCookie())
        );
    }

    @Override
    public String buildApiBaseUrl(String baseUrl, String endpoint) {
        return "https://" + baseUrl + endpoint;
    }

    private HttpHeaders buildHeaders(String cookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        headers.add("Referer", "https://" + ipaHostname + "/ipa");
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}
