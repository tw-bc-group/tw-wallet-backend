package com.thoughtworks.wallet.healthy.service;

import com.thoughtworks.wallet.healthy.model.SuspectedPatientInfo;

public interface ISuspectedPatientService {
    SuspectedPatientInfo addSuspectedPatient(String phone);

    boolean isSuspectedPatient(String phone);

    SuspectedPatientInfo deleteSuspectedPatient(String phone);
}
