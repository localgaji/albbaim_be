FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ARG AGENT_FILE=scouter/agent.java/scouter.agent.jar
ARG AGENT_CONF=scouter/agent.java/conf/scouter.conf
COPY ${AGENT_FILE} agent.jar
COPY ${AGENT_CONF} agent.conf

ENTRYPOINT ["java", \
"-javaagent:agent.jar", \
"-Dscouter.config=agent.conf", \
"-jar", "-Xms512m", "-Xmx512m", \
"-Dspring.profiles.active=test", \
"app.jar"]