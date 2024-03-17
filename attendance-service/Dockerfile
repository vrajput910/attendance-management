FROM openjdk:17
EXPOSE 8083
ADD ./attendance-service/target/attendance-service-1.0.0.jar attendance-service
ENTRYPOINT ["java", "-jar", "/attendance-service"]