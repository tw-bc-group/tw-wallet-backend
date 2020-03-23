FROM openjdk:8-jdk-alpine

EXPOSE 8080

ADD build/libs/tw-wallet-0.0.1-SNAPSHOT.jar /app/tw-wallet.jar

WORKDIR /app

RUN apk update
RUN apk add openssl
RUN apk add curl

USER 1000

CMD ["java", "-jar", "tw-wallet.jar"]