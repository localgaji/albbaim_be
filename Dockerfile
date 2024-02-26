FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ADD https://github.com/pinpoint-apm/pinpoint/releases/download/v2.5.3/pinpoint-agent-2.5.3.tar.gz /pinpoint
RUN tar -zxvf /pinpoint/pinpoint-agent-2.5.3tar.gz -C /pinpoint

ENTRYPOINT ["java", "-jar", "-javaagent:/pinpoint/pinpoint-agent-2.5.3/pinpoint-bootstrap-2.5.3.jar", "-Dpinpoint.agentId=albbaim", "-Dpinpoint.applicationName=albbaim", "-Xms512m", "-Xmx512m", "-Dspring.profiles.active=test", "app.jar"]