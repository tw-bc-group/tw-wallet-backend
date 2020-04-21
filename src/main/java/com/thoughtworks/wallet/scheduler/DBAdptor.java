package com.thoughtworks.wallet.scheduler;


import com.thoughtworks.wallet.asset.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.thoughtworks.wallet.gen.Tables.TBL_BLOCKS;
import static com.thoughtworks.wallet.gen.Tables.TBL_TRANSACTIONS;

@Slf4j
@Service
public class DBAdptor {

    private final DSLContext dslContext;

    public DBAdptor(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public long getLocalBlockHeight() {
        return dslContext
                .select(DSL.max(TBL_BLOCKS.HEIGHT))
                .from(TBL_BLOCKS)
                .fetchOne(Record1::value1).longValue();
    }

    /**
     * AMOUNT has * 10^18
     * @param transactionObject
     * @param assetName
     */
    public void SaveTransaction(EthBlock.TransactionObject transactionObject, String assetName) {

        dslContext.insertInto(TBL_TRANSACTIONS,
                TBL_TRANSACTIONS.ASSET_NAME,
                TBL_TRANSACTIONS.FROM_ADDRESS,
                TBL_TRANSACTIONS.HASH,
                TBL_TRANSACTIONS.HEIGHT,
                TBL_TRANSACTIONS.TO_ADDRESS,
                TBL_TRANSACTIONS.TX_INDEX,
                TBL_TRANSACTIONS.AMOUNT)
                .values(assetName,
                        transactionObject.getFrom(),
                        transactionObject.getHash(),
                        transactionObject.getBlockNumber().intValue(),
                        transactionObject.getTo(),
                        transactionObject.getTransactionIndex().intValue(),
                        transactionObject.getValue()
                )
                .onDuplicateKeyIgnore()
                .execute();
    }
}
