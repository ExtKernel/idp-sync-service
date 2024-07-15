package com.iliauni.idpsyncservice.win;

import com.iliauni.idpsyncservice.idp.GenericUserIdpRequestSenderResultBlackListFilter;
import com.iliauni.idpsyncservice.model.WinClient;
import org.springframework.stereotype.Component;

@Component
public class WinUserIdpRequestSenderResultBlackListFilter extends GenericUserIdpRequestSenderResultBlackListFilter<WinClient> {
}
