package com.thoughtworks.wallet.healthyVerifier.service.impl;

import com.thoughtworks.wallet.gen.tables.records.TblSuspectedPatientsPhoneListRecord;
import com.thoughtworks.wallet.healthyVerifier.model.SuspectedPatientInfo;
import com.thoughtworks.wallet.healthyVerifier.service.ISuspectedPatientService;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Service;

import static com.thoughtworks.wallet.gen.Tables.TBL_SUSPECTED_PATIENTS_PHONE_LIST;

@Slf4j
@Service
public class SuspectedPatientService implements ISuspectedPatientService {
    private final DSLContext dslContext;

    public SuspectedPatientService(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public SuspectedPatientInfo addSuspectedPatient(String phone) {
        final Result<TblSuspectedPatientsPhoneListRecord> suspectedPatientRecord = getSuspectedPatientRecordByPhone(phone);

        if (suspectedPatientRecord.isEmpty()) {
            dslContext
                .insertInto(TBL_SUSPECTED_PATIENTS_PHONE_LIST)
                .set(TBL_SUSPECTED_PATIENTS_PHONE_LIST.PHONE, phone)
                .execute();
        }

        return SuspectedPatientInfo.of(phone);
    }

    @Override
    public boolean isSuspectedPatient(String phone) {
        final Result<TblSuspectedPatientsPhoneListRecord> suspectedPatientRecord = getSuspectedPatientRecordByPhone(phone);
        return !suspectedPatientRecord.isEmpty();
    }

    @Override
    public SuspectedPatientInfo deleteSuspectedPatient(String phone) {
        dslContext
            .deleteFrom(TBL_SUSPECTED_PATIENTS_PHONE_LIST)
            .where(TBL_SUSPECTED_PATIENTS_PHONE_LIST.PHONE.eq(phone))
            .execute();

        return SuspectedPatientInfo.of(phone);
    }

    private Result<TblSuspectedPatientsPhoneListRecord> getSuspectedPatientRecordByPhone(String phone) {
        return dslContext.selectFrom(TBL_SUSPECTED_PATIENTS_PHONE_LIST)
                         .where(TBL_SUSPECTED_PATIENTS_PHONE_LIST.PHONE.eq(phone))
                         .fetch();
    }
}
