package com.iliauni.usersyncglobalservice.idp;

import java.util.List;

/**
 * An interface for blacklisting specific results from a request sender.
 *
 * @param <ClientType> a type of client from which the blacklist will be obtained.
 * @param <ResultType> a type of object that will be subjected to filtering.
 */
public interface IdpRequestSenderResultBlackListFilter<ClientType, ResultType> {
    ResultType filter(
            ClientType client,
            ResultType resultToFilter
    );
    List<ResultType> filter(
            ClientType client,
            List<ResultType> resultsToFilter
    );
}
