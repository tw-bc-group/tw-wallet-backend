package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.wallet.asset.response.TWPointBalanceResponse;

public interface IBlockchainService {
    TWPointBalanceResponse getTWPointBalanceBy(String address);
}
