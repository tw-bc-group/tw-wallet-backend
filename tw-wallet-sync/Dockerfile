FROM openjdk:8-jre-slim
MAINTAINER yin

ENV PARAMS=""


ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD build/libs/tw-wallet-sync-*-SNAPSHOT.jar /app.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /app.jar $PARAMS"]