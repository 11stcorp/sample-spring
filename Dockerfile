# Dockerfile

FROM openjdk:8-jre-alpine

RUN apk add --no-cache bash curl

EXPOSE 8080
WORKDIR /data

ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"

ENV TRACER_OPTS=""

# Datadog
ENV TRACER_OPTS="$TRACER_OPTS -javaagent:dd-java-agent.jar -Ddd.jmxfetch.enabled=true"
COPY ./tracer/dd-java-agent-0.29.1.jar.zip /data/dd-java-agent.jar


COPY ./target/*.jar /data/ROOT.jar

ENTRYPOINT exec java $JAVA_OPTS -jar ROOT.jar
