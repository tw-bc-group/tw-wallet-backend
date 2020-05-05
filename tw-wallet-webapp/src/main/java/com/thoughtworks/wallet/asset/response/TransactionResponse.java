package com.thoughtworks.wallet.asset.response;

import lombok.Value;

@Value
public class TransactionResponse {

    String hash;
    String from;
    String value;
    String creates;
}
