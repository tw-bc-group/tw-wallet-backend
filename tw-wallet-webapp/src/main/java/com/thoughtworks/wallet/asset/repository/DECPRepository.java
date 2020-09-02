package com.thoughtworks.wallet.asset.repository;

import com.thoughtworks.common.util.dcep.MoneyType;
import com.thoughtworks.wallet.gen.Tables;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.thoughtworks.wallet.gen.tables.TblDcep.TBL_DCEP;

@Component
public class DECPRepository {
    private final DSLContext dslContext;

    public DECPRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public void insert(String number, MoneyType moneyType, String address, String signature) {
        dslContext.insertInto(
                Tables.TBL_DCEP
                , TBL_DCEP.MONEY_TYPE
                , TBL_DCEP.OWNER
                , TBL_DCEP.SERIAL_NUMBER
                , TBL_DCEP.SIGNATURE
        ).values(
                moneyType.getMoneyTypeString()
                , address
                , number
                , signature
        )
                .execute();
    }
}
