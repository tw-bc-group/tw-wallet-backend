package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.wallet.asset.response.TWPointBalanceResponse;

import java.util.concurrent.ExecutionException;

public interface IQuorumService {
    TWPointBalanceResponse getTWPointBalanceBy(String address) throws ExecutionException, InterruptedException;
}
