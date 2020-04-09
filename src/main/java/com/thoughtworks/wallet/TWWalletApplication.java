package com.thoughtworks.wallet;

import com.thoughtworks.wallet.asset.annotation.QuorumRPCUrl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.ipc.UnixIpcService;
import org.web3j.protocol.ipc.WindowsIpcService;

@SpringBootApplication
public class TWWalletApplication {

    @QuorumRPCUrl
    private String rpcUrl;

    public static void main(String[] args) {
        SpringApplication.run(TWWalletApplication.class, args);
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

}
