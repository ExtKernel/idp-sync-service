package com.iliauni.idpsyncservice.kc;

import com.iliauni.idpsyncservice.idp.GenericUserIdpRequestSenderResultBlackListFilter;
import com.iliauni.idpsyncservice.model.SyncKcClient;
import org.springframework.stereotype.Component;

@Component
public class SyncKcUserIdpRequestSenderResultBlackListFilter extends GenericUserIdpRequestSenderResultBlackListFilter<SyncKcClient> {
}
