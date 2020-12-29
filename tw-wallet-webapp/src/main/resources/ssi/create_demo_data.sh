#!/bin/bash

addIssuer() {
  name=$1
  curl -i \
  -H "Accept: application/json" \
  -H "Content-Type:application/json" \
  -X POST --data '{"name":"'"$name"'"}' "localhost:8080/v2/issuers"
}

addVcType() {
  name=$1
  issuerId=$2
  content=$3
  curl -i \
  -H "Accept: application/json" \
  -H "Content-Type:application/json" \
  -X POST --data '{"name":"'"$name"'","issuerId":'"$issuerId"',"content":'"$content"'}' "localhost:8080/v2/vctypes"
}

addVerifier() {
  name=$1
  vcTypes=$2
  curl -i \
  -H "Accept: application/json" \
  -H "Content-Type:application/json" \
  -X POST --data '{"name":"'"$name"'","privateKey":"4762e04d10832808a0aebdaa79c12de54afbe006bfffd228b3abcc494fe986f9","vcTypes":'"$vcTypes"'}' "localhost:8080/v2/verifier"
}

addIssuer "信通院"
addIssuer "北医三院"

addVcType "健康行程码" "1" '["姓名","地域"]'
addVcType "核酸检测" "2" '["姓名","检测结果"]'

addVerifier "地坛公园" '[1]'
addVerifier "地坛医院" '[1,2]'