FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ADD http://repo.maven.apache.org/maven2/com/navercorp/pinpoint/pinpoint-bootstrap/2.5.3/pinpoint-bootstrap-2.5.3.jar /pinpoint/pinpoint-bootstrap-2.5.3.jar

ENTRYPOINT ["java", "-jar", "-javaagent:/pinpoint-agent-2.5.3/pinpoint-bootstrap-2.5.3.jar", "-Dpinpoint.agentId=albbaim", "-Dpinpoint.applicationName=albbaim", "-Xms512m", "-Xmx512m", "-Dspring.profiles.active=test", "app.jar"]