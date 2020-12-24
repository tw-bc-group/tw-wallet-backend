package com.thoughtworks.wallet.healthy.repository;

import com.thoughtworks.wallet.healthy.exception.VcTypeNotFoundException;
import com.thoughtworks.wallet.healthy.model.Issuer;
import com.thoughtworks.wallet.healthy.model.VcType;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.thoughtworks.wallet.gen.Tables.TBL_VC_TYPES;

@Slf4j
@Service
public class VcTypeDAO {
    private final DSLContext dslContext;
    private final IssuerDAO issuerDAO;

    public VcTypeDAO(DSLContext dslContext, IssuerDAO issuerDAO) {
        this.dslContext = dslContext;
        this.issuerDAO = issuerDAO;
    }

    public int insertVcType(VcType vcType) {
        Issuer issuer = issuerDAO.getIssuerById(vcType.getId());
        return dslContext
                .insertInto(TBL_VC_TYPES)
                .set(TBL_VC_TYPES.NAME, vcType.getName())
                .set(TBL_VC_TYPES.ISSUER, String.valueOf(issuer.getId()))
                .set(TBL_VC_TYPES.CONTENT, vcType.getContent().toArray(new String[0]))
                .execute();
    }

    public VcType getVcTypeById(Integer id) {
        return Optional.ofNullable(dslContext
                .select(TBL_VC_TYPES.ID
                        , TBL_VC_TYPES.NAME
                        , TBL_VC_TYPES.ISSUER
                        , TBL_VC_TYPES.CONTENT)
                .from(TBL_VC_TYPES)
                .where(TBL_VC_TYPES.ID.eq(String.valueOf(id)))
                .fetchOneInto(VcType.class)).orElseThrow(() -> new VcTypeNotFoundException(id));
    }

    public List<VcType> getVcTypesByIssuer(Integer issuerId) {
        Issuer issuer = issuerDAO.getIssuerById(issuerId);
        return Optional.ofNullable(dslContext
                .select(TBL_VC_TYPES.ID
                        , TBL_VC_TYPES.NAME
                        , TBL_VC_TYPES.ISSUER
                        , TBL_VC_TYPES.CONTENT)
                .from(TBL_VC_TYPES)
                .where(TBL_VC_TYPES.ISSUER.eq(String.valueOf(issuer.getId())))
                .fetchInto(VcType.class)).orElseThrow(() -> new VcTypeNotFoundException(issuer.getId()));
    }
}
