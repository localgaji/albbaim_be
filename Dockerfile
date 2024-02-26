FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ADD https://github.com/pinpoint-apm/pinpoint/releases/download/v2.5.3/pinpoint-agent-2.5.3.tar.gz /
RUN tar -zxvf /pinpoint-agent-2.5.3.tar.gz -C

ARG PINPOINT_VERSION=2.5.3

ENTRYPOINT ["java", "-jar", "-javaagent:/pinpoint-agent-${PINPOINT_VERSION}/pinpoint-bootstrap-${PINPOINT_VERSION}.jar", "-Dpinpoint.agentId=albbaim", "-Dpinpoint.applicationName=albbaim", "-Xms512m", "-Xmx512m", "-Dspring.profiles.active=test", "app.jar"]