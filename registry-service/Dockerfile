FROM openjdk:17
EXPOSE 8761
ADD ./registry-service/target/registry-service-1.0.0.jar registry-service
ENTRYPOINT ["java", "-jar", "/registry-service"]