package com.thoughtworks.wallet.healthy.repository;

import com.thoughtworks.wallet.healthy.exception.VerifierNotFoundException;
import com.thoughtworks.wallet.healthy.model.Verifier;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.thoughtworks.wallet.gen.Tables.TBL_VERIFIERS;

@Slf4j
@Service
public class VerifierDAO {
    private final DSLContext dslContext;

    public VerifierDAO(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public int insertVerifier(Verifier verifier) {
        return dslContext
                .insertInto(TBL_VERIFIERS)
                .set(TBL_VERIFIERS.NAME, verifier.getName())
                .set(TBL_VERIFIERS.PRIVATE_KEY, verifier.getPrivateKey())
                .set(TBL_VERIFIERS.VC_TYPES, verifier.getVcTypes().toArray(new String[0]))
                .returning(TBL_VERIFIERS.ID)
                .fetchOne().getId();
    }

    public Verifier getVerifierById(Integer id) {
        return Optional.ofNullable(dslContext
                .select(TBL_VERIFIERS.ID
                        , TBL_VERIFIERS.NAME
                        , TBL_VERIFIERS.PRIVATE_KEY
                        , TBL_VERIFIERS.VC_TYPES)
                .from(TBL_VERIFIERS)
                .where(TBL_VERIFIERS.ID.eq(id))
                .fetchOneInto(Verifier.class)).orElseThrow(() -> new VerifierNotFoundException(id));
    }

    public Verifier updateVerifier(Verifier verifier) {
        Integer id = dslContext
                .update(TBL_VERIFIERS)
                .set(TBL_VERIFIERS.NAME, verifier.getName())
                .set(TBL_VERIFIERS.PRIVATE_KEY, verifier.getPrivateKey())
                .set(TBL_VERIFIERS.VC_TYPES, verifier.getVcTypes().toArray(new String[0]))
                .where(TBL_VERIFIERS.ID.eq(verifier.getId()))
                .returning(TBL_VERIFIERS.ID)
                .fetchOne().getId();
        return getVerifierById(id);
    }
}
