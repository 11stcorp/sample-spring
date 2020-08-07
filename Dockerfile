# Dockerfile

FROM openjdk:8-jre-alpine

RUN apk add --no-cache bash curl

EXPOSE 8080
WORKDIR /data

ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"


COPY ./target/*.jar /data/ROOT.jar

ENTRYPOINT exec java $JAVA_OPTS -jar ROOT.jar
