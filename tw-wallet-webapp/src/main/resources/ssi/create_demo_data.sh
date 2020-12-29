#!/bin/bash

addIssuer() {
  name=$1
  curl -i \
  -H "Accept: application/json" \
  -H "Content-Type:application/json" \
  -X POST --data '{"name":"'"$name"'"}' "localhost:8080/v2/issuers"
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
  -X POST --data '{"id":"'"$id"'","name":"'"$name"'","issuerId":'"$issuerId"',"content":'"$content"',"url":"'"$url"'"}' "localhost:8080/v2/vctypes"
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

addVcType "ItineraryHealthCode" "健康行程码" "1" '["姓名","地域"]' "localhost:8080/v2/health-certifications"
addVcType "ImmunoglobulinDetection" "核酸检测" "2" '["姓名","检测结果"]' "localhost:8080/v2/health-certifications/immunoglobulin-detection"

addVerifier "地坛公园" '["ItineraryHealthCode"]'
addVerifier "地坛医院" '["ItineraryHealthCode","ImmunoglobulinDetection"]'