package com.thoughtworks.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.wallet.asset.model.Transaction;
import com.thoughtworks.wallet.common.ResponseBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("integ")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionMvcIntegrationTest {
    /**
     * @LocalServerPort 提供了 @Value("${local.server.port}") 的代替
     */
    @LocalServerPort
    private int port;

    private URL base;


    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        String url = String.format("http://localhost:%d/v1/transactions", port);
        System.out.println(String.format("port is : [%d]", port));
        try {
            this.base = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
//        restTemplate.setMessageConverters(Arrays.asList(new SecureHttpMessageConverter()));
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        restTemplateBuilder.configure(restTemplate);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);

        testRestTemplate.getRestTemplate().setInterceptors(
            Collections.singletonList((request, body, execution) ->
            {
                request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                return execution.execute(request, body);
            }));
    }

    @Test
    public void whenGetTransactionByHash_thenReturnTransaction() {
        String hash = "9eff6287e55ea56b2abcf8d84a1a151e8a00e0f482ea0ee0448fef9f5d3ebad4";
        String url = this.base.toString() + "/" + hash;
        final ResponseEntity<ResponseBean> response = testRestTemplate.getForEntity(url, ResponseBean.class);
        final ResponseBean responseBean = response.getBody();

        final Transaction transaction = mapper.convertValue(responseBean.getResult(), Transaction.class);
        assertThat(transaction.getHash()).isEqualTo(hash);
    }
}
