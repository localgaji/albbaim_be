FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "-javaagent:./pinpoint-agent/pinpoint-bootstrap-2.5.3.jar", "-Dpinpoint.agentId=albbaim","-Dpinpoint.applicationName=albbaim", "-Dpinpoint.config=./pinpoint-agent/pinpoint-root.config","-Xms512m", "-Xmx512m", "-Dspring.profiles.active=test", "app.jar"]