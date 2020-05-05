package com.thoughtworks.wallet.asset.service.impl;

import com.thoughtworks.wallet.asset.service.IAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service("AddressBalanceServiceImpl")
public class AddressServiceImpl  implements IAddressService {

    private static final String CLASS_NAME = AddressServiceImpl.class.getSimpleName();
    private static final BigDecimal ZERO = new BigDecimal("0");
}
