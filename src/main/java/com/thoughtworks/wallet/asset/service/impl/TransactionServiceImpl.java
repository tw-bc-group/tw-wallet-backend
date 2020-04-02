package com.thoughtworks.wallet.asset.service.impl;

import static com.thoughtworks.wallet.gen.Tables.TBL_TRANSACTIONS;

import com.thoughtworks.wallet.asset.model.Transaction;
import com.thoughtworks.wallet.asset.service.ITransactionService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

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

    public List<Transaction> listByFromAddress(String addr, int limit) {
        return dslContext.selectFrom(TBL_TRANSACTIONS)
            .where(TBL_TRANSACTIONS.FROM_ADDRESS.eq(addr))
            .orderBy(TBL_TRANSACTIONS.CREATE_TIME.desc())
            .limit(10)
            .fetch().into(Transaction.class);
    }
}
