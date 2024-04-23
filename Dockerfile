FROM openjdk:17
COPY ./build/libs/remind-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]