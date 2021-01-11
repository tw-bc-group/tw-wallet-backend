#!/bin/bash

#baseUrl="https://wallet.cn.blockchain.thoughtworks.cn"
baseUrl="http://localhost:8080"

addIssuer() {
  name=$1
  curl -i \
  -H "Accept: application/json" \
  -H "Content-Type:application/json" \
  -X POST --data '{"name":"'"$name"'"}' "${baseUrl}/v2/vc-market/issuer"
}

addVcType() {
  id=$1
  name=$2
  issuerId=$3
  content=$4
  url=$5
  curl -i \
  -H "Accept: application/json" \
  -H "Content-Type:application/json" \
  -X POST --data '{"id":"'"$id"'","name":"'"$name"'","issuerId":'"$issuerId"',"content":'"$content"',"url":"'"$url"'"}' "${baseUrl}/v2/vc-market/vc-type"
}

addVerifier() {
  id=$1
  name=$2
  vcTypes=$3
  curl -i \
  -H "Accept: application/json" \
  -H "Content-Type:application/json" \
  -X POST --data '{"id":"'"$id"'","name":"'"$name"'","vcTypes":'"$vcTypes"'}' "${baseUrl}/v2/verifier"
}

addIssuer "信通院"
addIssuer "北医三院"
addIssuer "出入境管理局"

addVcType "qSARS-CoV-2-Rapid-Test-Credential" "健康行程码" "1" '["姓名","地域"]' "${baseUrl}/v2/vc-market/vcs"
addVcType "ImmunoglobulinDetectionTestCard" "核酸检测" "2" '["姓名","检测结果"]' "${baseUrl}/v2/vc-market/vcs"
addVcType "护照可验证证书" "出入境证明" "3" '["姓名","出入境记录"]' "${baseUrl}/v2/vc-market/vcs"

addVerifier "mockdid" "地坛医院" '["qSARS-CoV-2-Rapid-Test-Credential","ImmunoglobulinDetectionTestCard"]'