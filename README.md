# Attendance-Management

## Features:
- Admin login
- User registration
- User login
- Some user management APIs
- Swipe In/Out.
- EOD total hours calculation for all employees.
- Attendance calculation based on total number of hours present in office.
- Notification of absenteeism (Desired).
- GraphQL API for fetching the attendance records.

## Services:
1. [Gateway-Service](#gateway-service)
2. [Registry-Service](#registry-service)
3. [User-Service](#user-service)
4. [Swipe-Service](#swipe-service)
5. [Attendance-Service](#attendance-service)
6. [Notification-Service](#notification-service)
7. [Security-Service](#security-service)
8. [Common-Service](#common-service)

### [Gateway-Service](/gateway-service):
This is API Gateway of application. This is responsible to redirect all the requests to respective services. In future we can implement the security here and can integrate the authentication and authorization as well.
### [Registry-Service](/registry-service):
This is a eureka registry service. All the services will be registered here, and we can monitor them.
### [User-Service](/user-service):
This service is responsible for user management with authentication and authorization.
### [Swipe-Service](/swipe-service):
This service is responsible to store the swipe in and swipe out events and will perform some calculation to find out the working hours of the employees. This service will also publish the attendance records to Kafka for attendance service.
### [Attendance-Service](/attendance-service):
This service will listen the data from Kafka to store the working and total hours of the employee in a day and then will provide the attendance reports. GraphQL is integrated with this service to enhance the endpoints.
### [Notification-Service](/notification-service):
This service will listen the events from Kafka and is responsible to send the notification or mails to the employees.
### [Security-Service](/security-service):
This is not a web service. This is having the common code related to spring security that will be used in multiple services. This service is getting used as a dependency in other services.
### [Common-Service](/common-service):
This is not a web or rest service. This is just to have the common code that will be used in multiple services. This service is getting used as a dependency in other services.
## Databases:
- User-Service is using relation database that is MySQL.
- Swipe-Service is using the relational database that is MySQL.
- Attendance-Service is using MongoDB.
- All the services are using Redis to load the user for authorization.
## Kafka-Topics:
- swipe-events-topic
- notification-events-topic
## Junit Test Cases:
Junit testcases are written for swipe-service. Code coverage is 95% as of now that can be found here: **swipe-service/target/coverage-reports/jacoco/index.html**
## GraphQL:
**GraphiQL Url:** http://localhost:8765/attendance-service/graphiql?path=/attendance-service/graphql <br>
**GraphQL Postman Url:** http://localhost:8765/attendance-service/graphql?path=/graphql <br><br>
**Note:** Authorization Bearer token is needed in header to access above endpoints.
## API:
[Click Here](/postman-json) for the postman collection
