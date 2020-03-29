package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.wallet.asset.model.Transaction;


public interface ITransactionService {
    Transaction fetchByTxnHash(String hash);
}
