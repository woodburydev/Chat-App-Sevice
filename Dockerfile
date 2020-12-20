FROM openjdk:15

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} socket-application.jar

ENTRYPOINT ["java","-jar","socket-application.jar"]