package com.thoughtworks.wallet.scheduler;


import com.thoughtworks.wallet.asset.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

import static com.thoughtworks.wallet.gen.Tables.TBL_BLOCKS;

@Slf4j
@Service
public class DbAdptor {

    private final DSLContext dslContext;

    public DbAdptor(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public long getLocalBlockHeight() {
        return dslContext
                .select(DSL.max(TBL_BLOCKS.HEIGHT))
                .from(TBL_BLOCKS)
                .fetchOne(Record1::value1).longValue();
    }
}
