package com.thoughtworks.wallet.scheduler.eth;


import com.thoughtworks.wallet.scheduler.base.BaseSync;
import com.thoughtworks.wallet.scheduler.eth.strategy.BaseEventStrategy;
import com.thoughtworks.wallet.scheduler.eth.strategy.BcEvent;
import com.thoughtworks.wallet.scheduler.util.DBAdptor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EthSync extends BaseSync {

    @Autowired
    private EthClientAdaptor ethClientAdaptor;

    @Autowired
    private DBAdptor dbAdptor;

    @Override
    protected long geRemoteBlockNum() {
        return ethClientAdaptor.getRemoteBlockNum();
    }

    /**
     * 区块链里面是从1开始，数据库是从0开始
     *
     * @return
     */
    @Override
    protected long getLocalBlockNum() {
        return dbAdptor.getLocalBlockHeight();
    }

    @Override
    public void switchNode() {
        ethClientAdaptor.switchNode();
    }

    private static Map<String, BaseEventStrategy> eventStrategyMap = new HashMap<>();

    public EthSync(ApplicationContext applicationContext) {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(BcEvent.class);
        if (serviceBeanMap.size() > 0) {
            for (Object serviceBean : serviceBeanMap.values()) {
                if (serviceBean instanceof BaseEventStrategy) {
                    String name = serviceBean.getClass().getName();
                    BaseEventStrategy baseEventStrategy = (BaseEventStrategy) serviceBean;
                    if (eventStrategyMap.get(name) == null) {
                        eventStrategyMap.put(name, baseEventStrategy);
                    }
                    log.info("EthSync::EthSync - name:{}, eventStrategyMap:{}", name, baseEventStrategy);
                }
            }
        }
    }

    @Override
    public void parseTx(String txHash) throws Exception {
        TransactionReceipt receipt = ethClientAdaptor.getTransactionReceipt(txHash);
        for (Map.Entry<String, BaseEventStrategy> syncJobEntry : eventStrategyMap.entrySet()) {
            BaseEventStrategy baseEventStrategy = syncJobEntry.getValue();
            baseEventStrategy.execute(receipt);
        }
    }


    @SneakyThrows
    @Override
    public void parseBlock(Long height) {
        EthBlock.Block block = ethClientAdaptor.getBlockByNumber(height, true);
        List<EthBlock.TransactionObject> transactionObjects = block.getTransactions().stream()
                .map(txResult -> (EthBlock.TransactionObject) txResult.get())
                .collect(Collectors.toList());

        boolean hasTx = CollectionUtils.isNotEmpty(transactionObjects);
        log.info("parseBlock - height:{}, has Txs: {}", height, hasTx);

        if (hasTx) {
            SYNC_EXECUTOR.submit(
                    () -> transactionObjects.stream().parallel().forEach(tx -> {
                        try {
                            this.parseTx(tx.getHash());
                        } catch (Exception e) {
                            log.error("parseTx - tx.getHash()s: {}", height, tx.getHash());
                        }
                    })
            );
        }
        // save block
        dbAdptor.SaveBlock(height, block.getHash());
    }
}
