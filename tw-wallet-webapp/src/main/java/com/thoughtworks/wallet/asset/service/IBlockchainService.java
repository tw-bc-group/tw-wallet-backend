package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.common.exception.InvalidAddressErrorException;
import com.thoughtworks.common.exception.QuorumConnectionErrorException;
import com.thoughtworks.wallet.asset.response.*;

import java.util.List;

public interface IBlockchainService {

    /**
     * get token balance
     * @param address
     * @return
     * @throws InvalidAddressErrorException
     * @throws QuorumConnectionErrorException
     */
    DECPBalanceResponse getDCEPBalanceBy(String address)
            throws InvalidAddressErrorException, QuorumConnectionErrorException;

    /**
     * get get transactions by address from blockchain，has performance issue
     * @param address
     * @param limit
     * @return
     */
    List<TransactionResponse> getTransactionsBy(String address, int limit);

    /**
     * send transaction to blockchain
     * TODO: save status to db
     * @param signedTransactionData
     * @param address
     */
    String sendRawTransaction(String signedTransactionData, String address);

    /**
     * get DC/EP Infos
     * @return
     */
    DECPInfoResponse getDCEPInfo();

    /**
     * get Identity Registry Infos
     * @return
     */
    IdentityRegistryInfoResponse getIdentityRegistryInfo();

    /**
     * assign point to address which just create DID
     * @param address
     * @param amount
     */
    void assignInitPoint(String address, int amount);

    IdentitiesContractInfoRepresentation getIdentityInfo();
}
