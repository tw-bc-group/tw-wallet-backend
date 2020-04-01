#!/usr/bin/env bash

docker run --rm --mount src=gradle-cache,target=/root/.gradle -v $(pwd):/project -w /project openjdk:8u212-jdk-alpine3.9 ./gradlew --no-daemon clean
