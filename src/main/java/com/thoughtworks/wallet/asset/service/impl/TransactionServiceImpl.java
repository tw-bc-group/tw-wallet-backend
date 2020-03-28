package com.thoughtworks.wallet.asset.service.impl;


import com.thoughtworks.wallet.asset.service.ITransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class TransactionServiceImpl implements ITransactionService {

    private static final BigDecimal ZERO = new BigDecimal("0");

    @Autowired
    public TransactionServiceImpl() {
    }

}
