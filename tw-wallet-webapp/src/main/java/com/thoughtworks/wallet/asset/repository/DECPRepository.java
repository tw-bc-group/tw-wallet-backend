package com.thoughtworks.wallet.asset.repository;

import com.thoughtworks.common.util.dcep.MoneyType;
import com.thoughtworks.wallet.asset.exception.DCEPNotFoundException;
import com.thoughtworks.wallet.asset.response.DCEPNFTInfoV2Response;
import com.thoughtworks.wallet.gen.Tables;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.thoughtworks.wallet.gen.tables.TblDcep.TBL_DCEP;

@Component
public class DECPRepository {
    private final DSLContext dslContext;

    public DECPRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public void insert(String number, MoneyType moneyType, String address, String signature, LocalDateTime createTime) {
        dslContext.insertInto(
                Tables.TBL_DCEP
                , TBL_DCEP.MONEY_TYPE
                , TBL_DCEP.OWNER
                , TBL_DCEP.SERIAL_NUMBER
                , TBL_DCEP.SIGNATURE
                , TBL_DCEP.CREATE_TIME
                , TBL_DCEP.OPERATOR
                , TBL_DCEP.FROM_ADDRESS

        ).values(
                moneyType
                , address
                , number
                , signature
                , createTime
                , ""
                , ""
        )
                .execute();
    }

    public DCEPNFTInfoV2Response getDCEPBySerialNumber(String serialNumber) {
        return Optional.ofNullable(dslContext
                .select(TBL_DCEP.SERIAL_NUMBER
                        , TBL_DCEP.OWNER
                        , TBL_DCEP.SIGNATURE
                        , TBL_DCEP.MONEY_TYPE
                        , TBL_DCEP.CREATE_TIME)
                .from(TBL_DCEP)
                .where(TBL_DCEP.SERIAL_NUMBER.eq(serialNumber))
                .fetchOneInto(DCEPNFTInfoV2Response.class)).orElseThrow(() -> new DCEPNotFoundException(serialNumber));
    }

    public List<DCEPNFTInfoV2Response> getDCEPByAddress(String address, int limit, int offset) {
        return Optional.ofNullable(dslContext
                .select(TBL_DCEP.SERIAL_NUMBER
                        , TBL_DCEP.OWNER
                        , TBL_DCEP.SIGNATURE
                        , TBL_DCEP.MONEY_TYPE
                        , TBL_DCEP.CREATE_TIME)
                .from(TBL_DCEP)
                .where(TBL_DCEP.OWNER.eq(address))
                .limit(limit)
                .offset(offset)
                .fetchInto(DCEPNFTInfoV2Response.class)).orElseThrow(() -> new DCEPNotFoundException(address));
    }
}
