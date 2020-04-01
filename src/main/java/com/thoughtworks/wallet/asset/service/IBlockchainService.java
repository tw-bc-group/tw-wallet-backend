package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.wallet.asset.exception.InvalidAddressErrorException;
import com.thoughtworks.wallet.asset.exception.QuorumConnectionErrorException;
import com.thoughtworks.wallet.asset.response.TWPointBalanceResponse;

public interface IBlockchainService {
    TWPointBalanceResponse getTWPointBalanceBy(String address) throws InvalidAddressErrorException, QuorumConnectionErrorException;
}
