package com.thoughtworks.wallet.healthy.dto.v2;

import com.thoughtworks.wallet.healthy.utils.ConstCoV2RapidTestCredential;
import com.thoughtworks.wallet.healthy.utils.ConstImmunoglobulinDetectionVC;
import com.thoughtworks.wallet.healthy.utils.ConstPassportVC;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 申请VC的API
 */

public class CreateVCGeneralRequest extends CreateVCRequest {
    @NotBlank
    @Getter
    @Size(min = 5, max = 255, message = "The length of issueId is  between 5 and 255")
    String issueId;

    @NotBlank
    @Getter
    @Size(min = 5, max = 255, message = "The length of vcType is  between 5 and 255")
    @ApiModelProperty(
            value = "vc type",
            example = ConstCoV2RapidTestCredential.TEST_TYPE + " | " +
                    ConstImmunoglobulinDetectionVC.TEST_TYPE + " | " +
                    ConstPassportVC.TEST_TYPE)
    String vcType;
}
