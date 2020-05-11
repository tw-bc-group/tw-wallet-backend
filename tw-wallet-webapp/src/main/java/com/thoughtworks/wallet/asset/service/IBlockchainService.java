package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.common.exception.InvalidAddressErrorException;
import com.thoughtworks.common.exception.QuorumConnectionErrorException;
import com.thoughtworks.wallet.asset.response.IdentityRegistryInfoResponse;
import com.thoughtworks.wallet.asset.response.DECPBalanceResponse;
import com.thoughtworks.wallet.asset.response.DECPInfoResponse;
import com.thoughtworks.wallet.asset.response.TransactionResponse;

import java.util.List;

public interface IBlockchainService {

    DECPBalanceResponse getDCEPBalanceBy(String address)
        throws InvalidAddressErrorException, QuorumConnectionErrorException;

    List<TransactionResponse> getTransactionsBy(String address, int limit);

    void sendRawTransaction(String signedTransactionData, String address);

    DECPInfoResponse getDCEPInfo();

    IdentityRegistryInfoResponse getIdentityRegistryInfo();
}
