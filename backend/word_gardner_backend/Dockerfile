FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY build/libs/word_gardner_backend-1.0.0.jar /app/server.jar
ENTRYPOINT ["java","-jar","/app/server.jar"]