package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.wallet.asset.model.TWPoint;

public interface IQuorumService {
    TWPoint getTWPointBalanceBy(String address);
}
