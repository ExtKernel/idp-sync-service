package com.iliauni.usersyncglobalservice.ipa;

import com.iliauni.usersyncglobalservice.idp.GenericUserIdpRequestSenderResultBlackListFilter;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import org.springframework.stereotype.Component;

@Component
public class IpaUserIdpRequestSenderResultBlackListFilter extends GenericUserIdpRequestSenderResultBlackListFilter<IpaClient> {
}
