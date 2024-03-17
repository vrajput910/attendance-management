FROM openjdk:17
EXPOSE 8090
ADD target/notification-service-1.0.0.jar notification-service
ENTRYPOINT ["java", "-jar", "/notification-service"]