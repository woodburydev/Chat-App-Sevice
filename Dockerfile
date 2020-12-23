FROM openjdk:8-jre-alpine3.9

# copy the packaged jar file into our docker image
COPY target/chat-application-0.0.1-SNAPSHOT.jar chat-application-0.0.1-SNAPSHOT.jar

# set the startup command to execute the jar
CMD ["java", "-jar", "/chat-application-0.0.1-SNAPSHOT.jar"]