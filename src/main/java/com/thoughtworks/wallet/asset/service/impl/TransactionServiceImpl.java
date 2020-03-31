package com.thoughtworks.wallet.asset.service.impl;


import com.thoughtworks.wallet.asset.model.Transaction;
import com.thoughtworks.wallet.asset.service.ITransactionService;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static com.thoughtworks.wallet.gen.Tables.TBL_TRANSACTIONS;


@Slf4j
@Service
public class TransactionServiceImpl implements ITransactionService {

    private final DSLContext dslContext;

    public TransactionServiceImpl(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public Transaction fetchByTxnHash(String hash) {
        return dslContext
            .selectFrom(TBL_TRANSACTIONS)
            .where(TBL_TRANSACTIONS.HASH.eq(hash))
            .fetchOneInto(Transaction.class);
    }

}
