package com.thoughtworks.common.util;

import com.thoughtworks.common.crypto.Base64;
import com.thoughtworks.common.crypto.CryptoFacade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class Jwt {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Header {
        String alg;
        String typ;
    }

    CryptoFacade sign;
    CryptoFacade verify;

    Header header;

    Object payLoad;

    String signature;

    String token;

    public String genJwtString() throws Exception {
        String header  = Base64.encode(JacksonUtil.beanToJSonStr(this.getHeader()));
        String payload = Base64.encode(JacksonUtil.beanToJSonStr(this.getPayLoad()));
        String headerPayload = String.format("%s.%s", header, payload);
        String generateSignature = sign.generateSignature(headerPayload);
        this.signature         = Base64.encode(generateSignature);
        this.token = String.format("%s.%s", headerPayload, signature);
        return token;
    }
}
