FROM openjdk:8-jre-slim

RUN mkdir /app

WORKDIR /app

ADD ./uporabniskiProfil-api/target/uporabniskiProfil-api-1.0-SNAPSHOT.jar /app

EXPOSE 8082

CMD java -jar uporabniskiProfil-api-1.0-SNAPSHOT.jar