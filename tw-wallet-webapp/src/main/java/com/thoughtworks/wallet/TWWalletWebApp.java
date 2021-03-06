package com.thoughtworks.wallet;

import com.thoughtworks.common.annotation.Node1PrivateKey;
import com.thoughtworks.common.annotation.QuorumRPCUrl;
import com.thoughtworks.common.wrapper.DCEPContract;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.ipc.UnixIpcService;
import org.web3j.protocol.ipc.WindowsIpcService;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com.thoughtworks.common", "com.thoughtworks.wallet"})
public class TWWalletWebApp {

    @QuorumRPCUrl
    private String rpcUrl;

    @Node1PrivateKey
    private String privateKey;

    @com.thoughtworks.common.annotation.DCEPContractAddress
    private String DCEPContractAddress;

    @com.thoughtworks.common.annotation.DCEPContractAddressNew
    private String DCEPContractAddressNew;

    public static void main(String[] args) {
        SpringApplication.run(TWWalletWebApp.class, args);
    }

    @Bean
    Web3j quorum() {
        String nodeEndpoint = rpcUrl;
        Web3jService web3jService;

        if (nodeEndpoint == null || nodeEndpoint.equals("")) {
            web3jService = new HttpService();
        } else if (nodeEndpoint.startsWith("http")) {
            web3jService = new HttpService(nodeEndpoint);
        } else if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            web3jService = new WindowsIpcService(nodeEndpoint);
        } else {
            web3jService = new UnixIpcService(nodeEndpoint);
        }

        return Web3j.build(web3jService);
    }

    @Bean
    ERC20 erc20(Web3j web3j) {
        return ERC20.load(
                DCEPContractAddress,
                web3j,
                Credentials.create(privateKey),
                new StaticGasProvider(BigInteger.ZERO, BigInteger.valueOf(4_300_000)));
    }

    @Bean
    DCEPContract decpContract(Web3j web3j) {
        return DCEPContract.load(
                DCEPContractAddressNew,
                web3j,
                Credentials.create(privateKey),
                new StaticGasProvider(BigInteger.ZERO, BigInteger.valueOf(4_300_000)));
    }
}
