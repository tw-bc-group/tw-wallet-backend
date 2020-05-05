package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.common.exception.InvalidAddressErrorException;
import com.thoughtworks.common.exception.QuorumConnectionErrorException;
import com.thoughtworks.wallet.asset.response.IdentityRegistryInfoResponse;
import com.thoughtworks.wallet.asset.response.TWPointBalanceResponse;
import com.thoughtworks.wallet.asset.response.TWPointInfoResponse;
import com.thoughtworks.wallet.asset.response.TransactionResponse;

import java.util.List;

public interface IBlockchainService {

    TWPointBalanceResponse getTWPointBalanceBy(String address)
        throws InvalidAddressErrorException, QuorumConnectionErrorException;

    List<TransactionResponse> getTransactionsBy(String address, int limit);

    void sendRawTransaction(String signedTransactionData, String address);

    TWPointInfoResponse getTWPointInfo();

    IdentityRegistryInfoResponse getIdentityRegistryInfo();
}
