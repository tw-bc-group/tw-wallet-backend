#!/bin/bash

addIssuer() {
  name=$1
  curl -i \
  -H "Accept: application/json" \
  -H "Content-Type:application/json" \
  -X POST --data '{"name":"'"$name"'"}' "localhost:8080/v2/vc-market/issuer"
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
  -X POST --data '{"id":"'"$id"'","name":"'"$name"'","issuerId":'"$issuerId"',"content":'"$content"',"url":"'"$url"'"}' "localhost:8080/v2/vc-market/vc-type"
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
addIssuer "海关"

addVcType "ItineraryHealthCode" "健康行程码" "1" '["姓名","地域"]' "localhost:8080/v2/vc-market/health-certifications"
addVcType "ImmunoglobulinDetection" "核酸检测" "2" '["姓名","检测结果"]' "localhost:8080/v2/vc-market/health-certifications/immunoglobulin-detection"
addVcType "Passport" "出入境证明" "3" '["姓名","出入境记录"]' "localhost:8080/v2/vc-market/health-certifications/passport"

addVerifier "地坛公园" '["ItineraryHealthCode"]'
addVerifier "地坛医院" '["ItineraryHealthCode","ImmunoglobulinDetection"]'