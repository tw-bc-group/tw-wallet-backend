package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.wallet.asset.model.Transaction;
import java.util.List;


public interface ITransactionService {
    Transaction fetchByTxnHash(String hash);

    List<Transaction> listByFromAddress(String addr);
}
