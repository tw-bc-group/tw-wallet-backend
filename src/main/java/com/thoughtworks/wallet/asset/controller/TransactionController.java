package com.thoughtworks.wallet.asset.controller;


import com.thoughtworks.wallet.asset.model.TransactionDetail;
import com.thoughtworks.wallet.asset.service.ITransactionService;
import com.thoughtworks.wallet.common.ResponseBean;
import com.thoughtworks.wallet.util.JacksonUtil;
import com.thoughtworks.wallet.util.Utilities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Slf4j
@Validated
@RequestMapping(value = "/v1/transactions")
@RestController
@Api(tags = "交易信息相关的查询")
public class TransactionController {

    private static final String CLASS_NAME = TransactionController.class.getSimpleName();

    private final ITransactionService transactionService;
    private String txID;

    @Autowired
    public TransactionController(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }


    /**
     * 查询交易历史
     *
     * @param pageIndex
     * @param pageOffset
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "查询交易历史")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "jwt", dataType = "String", required = true, value = "jwt"),
    })
    @GetMapping()
    public ResponseBean getTxHistory(@RequestParam("page_index") @Min(value = 1) int pageIndex,
                                     @RequestParam("page_offset") @Min(value = 1) @Max(value = 100) int pageOffset,
                                     @RequestParam(name = "start_date", required = false) Long startDateTimestamp,
                                     @RequestParam(name = "end_date", required = false) Long endDateTimestamp,
                                     @RequestParam(name = "tx_id", required = false) String txID,
                                     HttpServletRequest request, HttpServletResponse response) {


        log.info("{}.{} - pageIndex:{},pageOffset:{} start_date:{}, end_date:{} tx_id:{}",
                CLASS_NAME, Utilities.currentMethod(), pageIndex, pageOffset,
                startDateTimestamp, endDateTimestamp, txID);

        return new ResponseBean();
    }

}
