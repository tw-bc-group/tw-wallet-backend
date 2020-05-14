package com.thoughtworks.wallet.healthyVerifier.service;

import com.thoughtworks.wallet.healthyVerifier.model.SuspectedPatientInfo;

public interface ISuspectedPatientService {
    SuspectedPatientInfo addSuspectedPatient(String phone);

    boolean isSuspectedPatient(String phone);

    SuspectedPatientInfo deleteSuspectedPatient(String phone);
}
