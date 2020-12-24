package com.thoughtworks.wallet.healthy.model;

import lombok.*;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Issuer {
    Integer id;
    String name;
}
