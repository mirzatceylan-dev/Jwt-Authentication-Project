FROM openjdk:17-jdk-slim
WORKDIR / jwtSecurity
COPY target/*.jar jwtSecurity.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "jwtSecurity.jar"]
