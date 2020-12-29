package com.thoughtworks.wallet.healthy.dto.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
*     "sub": {
        *        "id": "did:key:z6MkjRagNiMu91DduvCvgEsqLZDVzrJzFrwahc4tXLt9DoHd", //可信声明接收者的ID
        *       "type": [
        *         "qSARS-CoV-2-Rapid-Test-Credential"
        *       ],
        *       "catalogNumber": "5515C025, 5515C050, 5515C100",
        *       "ifu": "https://cellexcovid.com/wp-content/uploads/2020/04/Cellex-rapid-ifu.pdf",
        *       "assay": "Negative"
        *     }
 *     */


@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
public class Sub implements ISub{
    // 持有者 id
    private String            id;
    private List<String> typ;
    // catalogNumber
    private String num;
    private String ifu;
    private AssayStatus assay;
}
