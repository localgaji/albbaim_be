FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", \
"-Xms512m", "-Xmx512m", \
"-Dspring.profiles.active=test", \
"-javaagent:scouter/agent.java/scouter.agent.jar" \
"-Dscouter.config=scouter/agent.java/conf/scouter.conf"
"app.jar"]