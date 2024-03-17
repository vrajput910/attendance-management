FROM openjdk:17
EXPOSE 8765
ADD target/gateway-service-1.0.0.jar gateway-service
ENTRYPOINT ["java", "-jar", "/gateway-service"]