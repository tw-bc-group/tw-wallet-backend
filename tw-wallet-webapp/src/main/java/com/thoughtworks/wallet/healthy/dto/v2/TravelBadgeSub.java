package com.thoughtworks.wallet.healthy.dto.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 "credentialSubject": {
 "id": "did:key:z6MkjRagNiMu91DduvCvgEsqLZDVzrJzFrwahc4tXLt9DoHd",
 "type": [
 "Person"
 ],
 "image": "https://cdn.pixabay.com/photo/2014/10/26/21/42/man-504453_1280.jpg"
 }
 */

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
public class TravelBadgeSub extends ISub {
    // 持有者 id
    private String            id;
    private List<String> typ;
    private String image;
}
