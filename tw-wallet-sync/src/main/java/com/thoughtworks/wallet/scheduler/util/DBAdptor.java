package com.thoughtworks.wallet.scheduler.util;


import com.thoughtworks.wallet.scheduler.eth.pojo.Identity;
import com.thoughtworks.wallet.scheduler.eth.pojo.TWPoint;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.thoughtworks.wallet.gen.Tables.*;


@Slf4j
@Service
public class DBAdptor {

    final static String ASSET_NAME = "TWP";
    private final DSLContext dslContext;

    public DBAdptor(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public long getLocalBlockHeight() {
        return Optional.ofNullable(dslContext
                .select(DSL.max(TBL_BLOCKS.HEIGHT))
                .from(TBL_BLOCKS)
                .fetchOne(Record1::value1)).orElse((long) 0);
    }

    public void SaveDIDCreateTransaction(Identity identity) {
        dslContext.insertInto(TBL_IDENTITIES,
                TBL_IDENTITIES.INITIATOR,
                TBL_IDENTITIES.OWNER_ADDRESS,
                TBL_IDENTITIES.DID,
                TBL_IDENTITIES.PUBLIC_KEY,
                TBL_IDENTITIES.NAME,
                TBL_IDENTITIES.HASH,
                TBL_IDENTITIES.HEIGHT,
                TBL_IDENTITIES.TX_INDEX,
                TBL_IDENTITIES.TX_TYPE
        ).values(
                identity.getInitiator(),
                identity.getOwnerAddress(),
                identity.getDid(),
                identity.getPublicKey(),
                identity.getName(),
                identity.getHash(),
                identity.getHeight().longValueExact(),
                identity.getTxIndex().longValueExact(),
                identity.getTxType()
        )
                .onDuplicateKeyIgnore()
                .execute();
    }

    /**
     * AMOUNT has * 10^18
     *
     * @param TWPoint
     */
    public void SaveTWPTransaction(TWPoint TWPoint) {

        dslContext.insertInto(TBL_TRANSACTIONS,
                TBL_TRANSACTIONS.ASSET_NAME,
                TBL_TRANSACTIONS.FROM_ADDRESS,
                TBL_TRANSACTIONS.TO_ADDRESS,
                TBL_TRANSACTIONS.HASH,
                TBL_TRANSACTIONS.HEIGHT,
                TBL_TRANSACTIONS.TX_INDEX,
                TBL_TRANSACTIONS.AMOUNT,
                TBL_TRANSACTIONS.TX_TYPE)
                .values(ASSET_NAME,
                        TWPoint.getFromAddr(),
                        TWPoint.getToAddr(),
                        TWPoint.getTxHash(),
                        TWPoint.getHeight().longValueExact(),
                        TWPoint.getIndex().longValueExact(),
                        TWPoint.getValue(),
                        TWPoint.getTxType())
                .onDuplicateKeyIgnore()
                .execute();
    }

    public void SaveBlock(long height, String hash) {
        dslContext.insertInto(TBL_BLOCKS,
                TBL_BLOCKS.HEIGHT,
                TBL_BLOCKS.HASH)
                .values(height, hash)
                .onDuplicateKeyIgnore()
                .execute();

    }
}
