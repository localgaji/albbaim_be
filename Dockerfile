FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ARG PINPOINT_VER=2.5.3
ARG ROOT=/usr/local
ADD https://github.com/pinpoint-apm/pinpoint/releases/download/v${PINPOINT_VER}/pinpoint-agent-${PINPOINT_VER}.tar.gz ${ROOT}
RUN tar -zxvf ${ROOT}/pinpoint-agent-${PINPOINT_VER}.tar.gz -C ${ROOT}

ENTRYPOINT ["java", "-jar", "-javaagent:${ROOT}/pinpoint-agent-${PINPOINT_VER}/pinpoint-bootstrap-${PINPOINT_VER}.jar", "-Dpinpoint.agentId=albbaim", "-Dpinpoint.applicationName=albbaim", "-Xms512m", "-Xmx512m", "-Dspring.profiles.active=test", "app.jar"]