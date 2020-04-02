package com.thoughtworks.wallet.asset.controller;

import com.thoughtworks.wallet.asset.model.Transaction;
import com.thoughtworks.wallet.asset.service.ITransactionService;
import com.thoughtworks.wallet.common.ResponseBean;
import com.thoughtworks.wallet.common.RspCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@Validated
@RequestMapping(value = "/v1/transactions")
@RestController
@Api(tags = "交易信息相关的查询")
public class TransactionController {

    // TODO(xudongdong): use instance?
    private final ITransactionService transactionService;

    @Autowired
    public TransactionController(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @ApiOperation(value = "查询交易历史")
    @GetMapping
    public List<Transaction> getTxHistory(@RequestParam(name = "from_addr") String fromAddr,
        @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return transactionService.listByFromAddress(fromAddr, limit);
    }


    @ApiOperation(value = "根据交易hash查询交易")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", name = "jwt", dataType = "String", required = false, value = "jwt"),
    })
    @RequestMapping(value = "/{tx_hash}", method = RequestMethod.GET)
    public ResponseBean getTx(@PathVariable(name = "tx_hash", required = true) String txHash,
        HttpServletRequest request, HttpServletResponse response) {

        log.info("txHash:{}", txHash);
        Transaction transaction = transactionService.fetchByTxnHash(txHash);
        return new ResponseBean(RspCode.SUCCESS.code(), RspCode.SUCCESS.descEN(), transaction);
    }
}
