package com.iliauni.usersyncglobalservice.win;

import com.iliauni.usersyncglobalservice.idp.GenericUserIdpRequestSenderResultBlackListFilter;
import com.iliauni.usersyncglobalservice.model.WinClient;
import org.springframework.stereotype.Component;

@Component
public class WinUserIdpRequestSenderResultBlackListFilter extends GenericUserIdpRequestSenderResultBlackListFilter<WinClient> {
}
