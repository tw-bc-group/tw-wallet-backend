package com.thoughtworks.wallet.healthy.repository;

import com.thoughtworks.wallet.healthy.exception.IssuerNotFoundException;
import com.thoughtworks.wallet.healthy.model.Issuer;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.thoughtworks.wallet.gen.Tables.TBL_ISSUERS;

@Slf4j
@Service
public class IssuerDAO {
    private final DSLContext dslContext;

    public IssuerDAO(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public int insertIssuer(Issuer issuer) {
        return dslContext
                .insertInto(TBL_ISSUERS)
                .set(TBL_ISSUERS.NAME, issuer.getName())
                .execute();
    }

    public Issuer getIssuerById(Integer id) {
        return Optional.ofNullable(dslContext
                .select(TBL_ISSUERS.ID
                        , TBL_ISSUERS.NAME)
                .from(TBL_ISSUERS)
                .where(TBL_ISSUERS.ID.eq(String.valueOf(id)))
                .fetchOneInto(Issuer.class)).orElseThrow(() -> new IssuerNotFoundException(id));
    }

    public List<Issuer> getAllIssuers() {
        return Optional.ofNullable(dslContext
                .select(TBL_ISSUERS.ID
                        , TBL_ISSUERS.NAME)
                .from(TBL_ISSUERS)
                .fetchInto(Issuer.class)).orElseThrow(IssuerNotFoundException::new);
    }
}
