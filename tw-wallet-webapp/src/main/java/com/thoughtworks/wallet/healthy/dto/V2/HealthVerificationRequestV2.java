package com.thoughtworks.wallet.healthy.dto.V2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.wallet.healthy.model.Result;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 申请VC的API
1. 输入为 issuer 类型；VC 类型；VC需要填写的信息，姓名/地域是必填的信息，DID
2. 返回申请状态：pending, rejected；VC申请DID
3. VC的种类为健康码，核酸检测，其他信息参考ui设计
4. 需要验证输入信息
*/

public class HealthVerificationRequestV2 {
    @NotBlank
    @Getter
    String did;

    @NotBlank
    @Getter
    String address;

    @NotBlank
    @Getter
    String name;
}
