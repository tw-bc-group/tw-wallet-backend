package com.thoughtworks.wallet.scheduler.eth;


import com.thoughtworks.wallet.scheduler.BaseSync;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EthSync extends BaseSync {

    @Autowired
    private EthClientAdaptor ethClientAdaptor;


    @Override
    protected long geRemoteBlockNum() {
//        return ethClientAdaptor.getLatestBlockNum().longValueExact();
        return 100;
    }

    @Override
    protected long getLocalBlockNum() {
        return 0;
    }

    @SneakyThrows
    @Override
    public void parseBlock(Long height) {
        log.info("EthSync::parseBlock - height:{}", height);
//        EthBlock.Block block = ethClientAdaptor.getBlockByNumber(height, true);
//        List<EthBlock.TransactionObject> txs = block.getTransactions().stream()
//                .map(txResult -> (EthBlock.TransactionObject) txResult.get())
//                .collect(Collectors.toList());
    }
}
