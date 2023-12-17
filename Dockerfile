FROM openjdk:21-jdk-slim-buster
WORKDIR /payment-provider

COPY build/libs/payment-provider-1.0-SNAPSHOT.jar /payment-provider/build/

WORKDIR /payment-provider/build

EXPOSE 8082

ENTRYPOINT java -jar payment-provider-1.0-SNAPSHOT.jar