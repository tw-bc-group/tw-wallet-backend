package com.thoughtworks.wallet.scheduler.eth.strategy;

import com.thoughtworks.common.annotation.CenterBankPrivateKey;
import com.thoughtworks.common.util.dcep.DCEPUtil;
import com.thoughtworks.common.util.dcep.MoneyType;
import com.thoughtworks.common.util.dcep.StringBytesConvert;
import com.thoughtworks.common.wrapper.DCEPContract;
import com.thoughtworks.wallet.scheduler.util.DBAdptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@BcEvent
public class TransferSingle extends BaseEventStrategy {
    static String ADDRESS0 = "0x0000000000000000000000000000000000000000";
    @Autowired
    private DBAdptor dbAdptor;

    @CenterBankPrivateKey
    private String privateKey;

    @Override
    public Event getEventDefinition() {
        return DCEPContract.TRANSFERSINGLE_EVENT;
    }

    /**
     * event TransferSingle(address indexed operator, address indexed from, address indexed to, uint256 id, uint256 value);
     */
    @Transactional
    @Override
    protected void readBcLogs(TransactionReceipt receipt) throws Exception {
        List<Log> logs = receipt.getLogs();
        Log logTx = logs.get(0);
        List<String> topics = logTx.getTopics();
        Address operator = new Address(topics.get(1));
        Address fromAddr = new Address(topics.get(2));
        Address toAddr = new Address(topics.get(3));

        List<Type> results = FunctionReturnDecoder.decode(logTx.getData(), getEvent().getNonIndexedParameters());
        BigInteger serialNumber = (BigInteger) results.get(0).getValue();
        String serialNumberStr = StringBytesConvert.hexToAscii(serialNumber.toString(16));
        BigInteger value = (BigInteger) results.get(1).getValue();

        String moneyTypeStr = "RMB" + serialNumberStr.substring(3, 9);
        MoneyType moneyType = MoneyType.valueOf(moneyTypeStr);

        // 创建银行签名
        String bankSign = DCEPUtil.signWithSHA256RSA(serialNumberStr, privateKey);
        log.info("readBcLogs - operator:{}, fromAddr:{}, toAddr:{}, serialNumber:{}, value:{}", operator.getValue(), fromAddr.getValue(), toAddr.getValue(), serialNumber, value);
        //transfer,更新owner, operator, fromAddr
        dbAdptor.insertOrUpdateDCEP(serialNumberStr, operator.getValue(), fromAddr.getValue(), toAddr.getValue(), moneyType, bankSign);

    }
}
