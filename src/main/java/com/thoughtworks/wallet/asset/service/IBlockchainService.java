package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.wallet.asset.exception.InvalidAddressErrorException;
import com.thoughtworks.wallet.asset.exception.QuorumConnectionErrorException;
import com.thoughtworks.wallet.asset.response.TWPointBalanceResponse;
import com.thoughtworks.wallet.asset.response.TransactionResponse;

import java.util.List;

public interface IBlockchainService {

    TWPointBalanceResponse getTWPointBalanceBy(String address)
        throws InvalidAddressErrorException, QuorumConnectionErrorException;

    List<TransactionResponse> getTransactionsBy(String address, int limit);

    void createIdentity(String signedTransactionData);
}
