package com.thoughtworks.wallet.asset.service.impl;


import com.thoughtworks.wallet.asset.model.Transaction;
import com.thoughtworks.wallet.asset.service.ITransactionService;
import com.thoughtworks.wallet.gen.tables.records.TblTransactionsRecord;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jooq.RecordValueReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.thoughtworks.wallet.gen.Tables.TBL_TRANSACTIONS;


@Slf4j
@Service
public class TransactionServiceImpl implements ITransactionService {

    @Autowired
    private DSLContext dslContext;

    @Autowired
    private ModelMapper modelMapper;


    public TransactionServiceImpl() {
    }

    public Transaction fetchByTxnHash(String hash) {
        Transaction transaction = dslContext
                .selectFrom(TBL_TRANSACTIONS)
                .where(TBL_TRANSACTIONS.HASH.eq(hash))
                .fetchOneInto(Transaction.class);
        return transaction;
    }

}
