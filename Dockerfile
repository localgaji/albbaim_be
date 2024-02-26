FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ARG PINPOINT_VERSION=2.5.3
ENV JAVA_OPTS="-javaagent:/pinpoint-agent/pinpoint-bootstrap-${PINPOINT_VERSION}.jar -Dpinpoint.agentId=albbaim -Dpinpoint.applicationName=albbaim"

ENTRYPOINT ["java", "-jar", ${JAVA_OPTS}, "-Xms512m", "-Xmx512m", "-Dspring.profiles.active=test", "app.jar"]