FROM amazoncorretto:17-alpine-jdk
ARG JAR_FILE=/build/libs/remind-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} remind.jar
ENTRYPOINT ["java", "-jar", "remind.jar"]
