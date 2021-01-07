package com.thoughtworks.wallet.healthy.service.impl.v2;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.common.util.JacksonUtil;
import com.thoughtworks.wallet.healthy.dto.v2.VerifierRequest;
import com.thoughtworks.wallet.healthy.dto.v2.VerifierResponse;
import com.thoughtworks.wallet.healthy.dto.v2.VerifierVcResponse;
import com.thoughtworks.wallet.healthy.dto.v2.VerifierVcTypesRequest;
import com.thoughtworks.wallet.healthy.model.Verifier;
import com.thoughtworks.wallet.healthy.repository.VcTypeDAO;
import com.thoughtworks.wallet.healthy.repository.VerifierDAO;
import com.thoughtworks.wallet.healthy.service.v2.IVerifierService;
import lombok.extern.slf4j.Slf4j;
import org.jooq.tools.json.JSONArray;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.text.StringSubstitutor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Service
public class VerifierService implements IVerifierService {
    private final VerifierDAO verifierDAO;
    private final VcTypeDAO vcTypeDAO;
    private final JacksonUtil jacksonUtil;

    private static final String VERIFIER_VC_TEMPLATE_PATH = "/v2/VerifierVc.json";
    private static final String VERIFIER_NAME_KEY = "VERIFIER_NAME_KEY";
    private static final String VC_TYPES_KEY = "VC_TYPES_KEY";

    public VerifierService(VerifierDAO verifierDAO, VcTypeDAO vcTypeDAO, JacksonUtil jacksonUtil) {
        this.verifierDAO = verifierDAO;
        this.vcTypeDAO = vcTypeDAO;
        this.jacksonUtil = jacksonUtil;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VerifierResponse createVerifier(VerifierRequest verifierRequest) {
        verifierRequest.getVcTypes().forEach(vcTypeDAO::getVcTypeById);
        Integer id = verifierDAO.insertVerifier(Verifier.builder()
                .name(verifierRequest.getName())
                .privateKey(verifierRequest.getPrivateKey())
                .vcTypes(verifierRequest.getVcTypes())
                .build());
        return getVerifierById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VerifierResponse getVerifierById(Integer id) {
        Verifier verifier = verifierDAO.getVerifierById(id);
        return VerifierResponse.builder()
                .id(verifier.getId())
                .name(verifier.getName())
                .vcTypes(verifier.getVcTypes())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VerifierResponse updateVerifierVcTypes(Integer id, VerifierVcTypesRequest request) {
        Verifier verifier = verifierDAO.getVerifierById(id);
        Verifier updatedVerifier = verifierDAO.updateVerifier(Verifier.builder()
                .id(id)
                .name(request.getName())
                .privateKey(verifier.getPrivateKey())
                .vcTypes(request.getVcTypes())
                .build()
        );
        return getVerifierById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VerifierVcResponse getVerifierVc(Integer id) throws IOException {
        Verifier verifier = verifierDAO.getVerifierById(id);

        String vcTemplate = jacksonUtil.readJsonFile(VERIFIER_VC_TEMPLATE_PATH);
        JSONArray vcTypes = new JSONArray();
        vcTypes.addAll(verifier.getVcTypes());
        Map<String, String> vcValues = new HashMap<String, String>() {{
            put(VERIFIER_NAME_KEY, verifier.getName());
            put(VC_TYPES_KEY, vcTypes.toString());
        }};
        String vc = new StringSubstitutor(vcValues).replace(vcTemplate);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode vcJsonNode = mapper.readTree(
                mapper.getFactory().createParser(vc)
        );

        return new VerifierVcResponse(vcJsonNode);
    }
}
