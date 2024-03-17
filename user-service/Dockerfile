FROM openjdk:17
EXPOSE 8081
ADD ./user-service/target/user-service-1.0.0.jar user-service
ENTRYPOINT ["java", "-jar", "/user-service"]