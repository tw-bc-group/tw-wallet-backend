package com.thoughtworks.wallet.asset.service.impl;

import com.thoughtworks.wallet.asset.annotation.Node1PrivateKey;
import com.thoughtworks.wallet.asset.annotation.QuorumRPCUrl;
import com.thoughtworks.wallet.asset.annotation.TWPointContractAddress;
import com.thoughtworks.wallet.asset.exception.InvalidAddressErrorException;
import com.thoughtworks.wallet.asset.exception.QuorumConnectionErrorException;
import com.thoughtworks.wallet.asset.model.TWPoint;
import com.thoughtworks.wallet.asset.response.TWPointBalanceResponse;
import com.thoughtworks.wallet.asset.service.IBlockchainService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.Integer.parseInt;

@Slf4j
@Service
public class QuorumServiceImpl implements IBlockchainService {

    @QuorumRPCUrl
    private String rpcUrl;

    @Node1PrivateKey
    private String privateKey;

    @TWPointContractAddress
    private String TWPointContractAddress;

    @Override
    public TWPointBalanceResponse getTWPointBalanceBy(String address) {
        boolean isAddress = isChecksumAddress(address);
        if (!isAddress) {
            throw new InvalidAddressErrorException(address);
        }

        Web3j web3j = Web3j.build(new HttpService(rpcUrl));

        Web3ClientVersion web3ClientVersion;
        try {
            web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
            log.info("Connected to Quorum client with version: " + web3ClientVersion.getWeb3ClientVersion());
        } catch (InterruptedException | ExecutionException e) {
            throw new QuorumConnectionErrorException(rpcUrl);
        }

        final ERC20 twPoint = ERC20.load(TWPointContractAddress, web3j, Credentials.create(privateKey), new DefaultGasProvider());

        final CompletableFuture<String> twPointSymbol = twPoint.symbol().sendAsync();
        final CompletableFuture<String> twPointName = twPoint.name().sendAsync();
        final CompletableFuture<BigInteger> twPointDecimal = twPoint.decimals().sendAsync();
        final CompletableFuture<BigInteger> twPointBalance = twPoint.balanceOf(address).sendAsync();

        web3j.shutdown();
        try {
            return TWPointBalanceResponse.of(address, TWPoint.create(twPointName.get(), twPointSymbol.get(), twPointDecimal.get()), new BigDecimal(twPointBalance.get()));
        } catch (InterruptedException | ExecutionException e) {
            throw new QuorumConnectionErrorException(rpcUrl);
        }
    }

    public static boolean isChecksumAddress(String addr) {
        //Print for testing purpose and more verbose output
        log.info("Incoming Address " + addr);

        // First we need to check the address has the value between 0-9a-fA-F
        String regex = "^0x[0-9a-fA-F]{40}$";
        if (!addr.matches(regex)) {
            return false;
        }

        //to fetch the part after 0x
        String subAddr = addr.substring(2);
        //Make it to original lower case address
        String subAddrLower = subAddr.toLowerCase();

        //Print for testing purpose and more verbose output
        log.info("Fetched Original Address " + subAddrLower);

        // if the previous step validates then we will test the checksum part

        // Create a SHA3256 hash (Keccak-256)
        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest256();
        digestSHA3.update(subAddrLower.getBytes());
        String digestMessage = Hex.toHexString(digestSHA3.digest());

        //Print for testing purpose and more verbose output
        log.info("Hex String " + digestMessage);

        /* Check each letter is upper case or not
         * if it is upper case then the corresponding binary position of the hashed address
         * should be 1 i.e the message digest letter should be getter than 7
         * as 7 is the last Hex digit which starts with 0 in binary
         * rest of all 8 to f starts with 1
         */

        for (short i = 0; i < subAddr.length(); i++) {
            if (subAddr.charAt(i) >= 65 && subAddr.charAt(i) <= 91) {

                log.info("Position Upper " + (subAddr.charAt(i)));
                log.info("Position digest " + (digestMessage.charAt(i)));

                String ss = Character.toString(digestMessage.charAt(i));
                if (!(parseInt(ss, 16) > 7)) {
                    return false;
                }
            }
        }
        return true;
    }
}