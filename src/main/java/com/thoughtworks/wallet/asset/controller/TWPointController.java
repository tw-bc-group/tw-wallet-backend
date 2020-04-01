package com.thoughtworks.wallet.asset.controller;

import com.thoughtworks.wallet.asset.exception.InvalidAddressErrorException;
import com.thoughtworks.wallet.asset.exception.QuorumConnectionErrorException;
import com.thoughtworks.wallet.asset.response.TWPointBalanceResponse;
import com.thoughtworks.wallet.asset.service.IBlockchainService;
import com.thoughtworks.wallet.common.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.thoughtworks.wallet.common.exception.ErrorCode.INVALID_ADDRESS;
import static com.thoughtworks.wallet.common.exception.ErrorCode.QUORUM_CONNECTION_ERROR;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/v1/tw-points")
@Api(tags = "TW Point 相关操作")
public class TWPointController {

    private final IBlockchainService quorumService;

    @Autowired
    public TWPointController(IBlockchainService quorumService) {
        this.quorumService = quorumService;
    }

    @GetMapping("/{address}")
    @ApiOperation(value = "根据用户的 quorum 地址获取 TW Point 余额")
    public ResponseBean getBalanceByAddress(@PathVariable("address") String address) {
        TWPointBalanceResponse twPointBalance;
        try {
            twPointBalance = quorumService.getTWPointBalanceBy(address);
        } catch (InvalidAddressErrorException e) {
            return new ResponseBean(INVALID_ADDRESS.getStatus().value(), INVALID_ADDRESS.getMessage(), null);
        } catch (QuorumConnectionErrorException e) {
            return new ResponseBean(QUORUM_CONNECTION_ERROR.getStatus().value(), QUORUM_CONNECTION_ERROR.getMessage(), null);
        }
        return new ResponseBean(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), twPointBalance);
    }
}
