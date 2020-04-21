package com.thoughtworks.wallet;

import com.thoughtworks.wallet.asset.model.Transaction;
import com.thoughtworks.wallet.common.ResponseBean;
import com.thoughtworks.wallet.scheduler.eth.EthSync;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static com.thoughtworks.wallet.util.Identity.verifySignature;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@ActiveProfiles("integ")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class decodeTxTest {

    @Autowired
    EthSync ethSync;

    @Test
    public void test_decode_tx() {

        long blockNumber = 15566;
        try {
            ethSync.parseBlock(blockNumber);
        }catch (Exception e){
            Assertions.assertThat(e).hasMessage("");
        }
    }
}