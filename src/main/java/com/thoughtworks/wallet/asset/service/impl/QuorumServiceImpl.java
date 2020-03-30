package com.thoughtworks.wallet.asset.service.impl;

import com.thoughtworks.wallet.asset.model.TWPoint;
import com.thoughtworks.wallet.asset.service.IQuorumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QuorumServiceImpl implements IQuorumService {
    @Override
    public TWPoint getTWPointBalanceBy(String address) {
        return null;
    }
}
