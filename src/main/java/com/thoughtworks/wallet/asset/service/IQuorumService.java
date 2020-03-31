package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.wallet.asset.response.TWPointBalanceResponse;

public interface IQuorumService {
    TWPointBalanceResponse getTWPointBalanceBy(String address);
}
