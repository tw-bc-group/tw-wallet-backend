package com.thoughtworks.wallet.healthy.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Verifier {
    Integer id;
    String name;
    String privateKey;
    List<Integer> vcTypes;
}
