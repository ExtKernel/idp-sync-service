package com.iliauni.usersyncglobalservice.kc;

import com.iliauni.usersyncglobalservice.idp.GenericUserIdpRequestSenderResultBlackListFilter;
import com.iliauni.usersyncglobalservice.model.SyncKcClient;
import org.springframework.stereotype.Component;

@Component
public class SyncKcUserIdpRequestSenderResultBlackListFilter extends GenericUserIdpRequestSenderResultBlackListFilter<SyncKcClient> {
}
