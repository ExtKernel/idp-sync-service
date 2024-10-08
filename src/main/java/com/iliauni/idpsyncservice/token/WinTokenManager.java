package com.iliauni.idpsyncservice.token;

import com.iliauni.idpsyncservice.model.WinClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class WinTokenManager extends GenericTokenManager<WinClient> {

    @Autowired
    public WinTokenManager(
            @Qualifier("winTokenJsonObjectMapper") TokenJsonObjectMapper jsonObjectMapper,
            TokenRequestSender<WinClient> requestSender
    ) {
        super(jsonObjectMapper, requestSender);
    }
}
