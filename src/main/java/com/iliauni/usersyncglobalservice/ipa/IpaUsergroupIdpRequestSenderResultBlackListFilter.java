package com.iliauni.usersyncglobalservice.ipa;

import com.iliauni.usersyncglobalservice.idp.GenericUsergroupIdpRequestSenderResultBlackListFilter;
import com.iliauni.usersyncglobalservice.model.IpaClient;
import org.springframework.stereotype.Component;

@Component
public class IpaUsergroupIdpRequestSenderResultBlackListFilter extends GenericUsergroupIdpRequestSenderResultBlackListFilter<IpaClient> {
}
