package com.iliauni.idpsyncservice.ipa;

import com.iliauni.idpsyncservice.idp.GenericUserIdpRequestSenderResultBlackListFilter;
import com.iliauni.idpsyncservice.model.IpaClient;
import org.springframework.stereotype.Component;

@Component
public class IpaUserIdpRequestSenderResultBlackListFilter extends GenericUserIdpRequestSenderResultBlackListFilter<IpaClient> {
}
