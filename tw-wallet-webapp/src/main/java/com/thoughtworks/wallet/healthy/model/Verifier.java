package com.thoughtworks.wallet.healthy.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Verifier {
    String id;
    String name;
    List<String> vcTypes;
}
