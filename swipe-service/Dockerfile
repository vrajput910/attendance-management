FROM openjdk:17
EXPOSE 8082
ADD ./swipe-service/target/swipe-service-1.0.0.jar swipe-service
ENTRYPOINT ["java", "-jar", "/swipe-service"]