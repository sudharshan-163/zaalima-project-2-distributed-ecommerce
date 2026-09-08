# Zaalima Project 2 - Distributed E-Commerce Microservices

A distributed e-commerce backend built using a microservices architecture with event-driven communication, Apache Kafka, Apache Avro, Choreography Saga, Resilience4j, Micrometer/Prometheus monitoring, distributed tracing with Zipkin, Docker, and Kubernetes.

## Architecture

Services:
- Service Registry (Eureka Server) - 8761
- Config Server - 8888
- API Gateway - 8090
- Order Service - 8081
- Inventory Service - 8082
- Payment Service - 8083
- Notification Service - 8084

Infrastructure:
- Apache Kafka
- PostgreSQL
- Prometheus
- Zipkin
- Docker
- Kubernetes

## Technology Stack

- Java 17
- Spring Boot
- Spring Cloud
- Eureka
- Spring Cloud Config
- Spring Cloud Gateway
- Apache Kafka
- Apache Avro
- Resilience4j
- Micrometer
- Prometheus
- Zipkin
- Docker
- Kubernetes
- Maven
- PostgreSQL

## Event-Driven Communication

Kafka is used for asynchronous communication between microservices.

Avro is used for the main business events.

Main order flow:

Client -> API Gateway -> Order Service -> Order Created Event -> Inventory Service -> Stock Reserved Event -> Payment Service -> Payment Success Event -> Order PAID

## Choreography Saga

The project uses a choreography-based Saga pattern.

Successful flow:

Order Created
-> Inventory reserves stock
-> Payment succeeds
-> Order becomes PAID

Payment failure compensation:

Order Created
-> Inventory reserves stock
-> Payment fails
-> Payment Failed Event
-> Inventory releases stock
-> Stock Released Event
-> Order becomes CANCELLED

The compensation flow restores reserved inventory when payment fails.

## Resilience

Resilience4j is used for:

- Circuit Breaker
- Retry
- Time Limiter
- Fallback

## Monitoring

Micrometer and Spring Boot Actuator provide application metrics.

Prometheus is used for metrics monitoring.

## Distributed Tracing

Micrometer Tracing with Brave and Zipkin Reporter is configured.

Default Zipkin endpoint:

http://localhost:9411/api/v2/spans

Zipkin UI:

http://localhost:9411

Zipkin runtime availability depends on a running Zipkin instance.

## Security

The API Gateway contains JWT-based resource-server security configuration.

The JWT secret is supplied through external configuration and is not committed to the repository.

## API Gateway Routes

/orders/** -> ORDER-SERVICE
/inventory/** -> INVENTORY-SERVICE
/payments/** -> PAYMENT-SERVICE

The Gateway uses Eureka service discovery and load-balanced routes.

## Testing

Service-level tests are available for:

- Order Service
- Inventory Service
- Payment Service
- Notification Service

Verified distributed workflows include:

- Successful payment -> Order becomes PAID
- Payment failure -> Stock is released and Order becomes CANCELLED

The payment-failure compensation flow was verified in Kubernetes, including inventory restoration.

## Docker

Dockerfiles are provided for:

- API Gateway
- Config Server
- Service Registry
- Order Service
- Inventory Service
- Payment Service
- Notification Service

The services use Java 17 runtime containers.

## Kubernetes

Kubernetes manifests are available under:

k8s/

Manifests include:

- service-registry.yaml
- config-server.yaml
- kafka.yaml
- order-service.yaml
- inventory-service.yaml
- payment-service.yaml
- notification-service.yaml

Database credentials are supplied through a Kubernetes Secret.

## Project Structure

api-gateway/
config-server/
service-registry/
order-service/
inventory-service/
payment-service/
notification-service/
config-repository/
k8s/
README.md

## Running Locally

Ensure the required infrastructure is running before starting dependent services.

Recommended order:

1. PostgreSQL
2. Kafka
3. Service Registry
4. Config Server
5. Application Services
6. API Gateway
7. Prometheus
8. Zipkin

Build:

.\mvnw.cmd clean package

Run tests:

.\mvnw.cmd clean test

## Verification

Eureka:

http://localhost:8761

API Gateway:

http://localhost:8090

Prometheus:

http://localhost:9090

Zipkin:

http://localhost:9411

## Kubernetes Verification

Useful commands:

kubectl get pods
kubectl get services
kubectl get deployments

The Kubernetes environment was used to verify the distributed order workflow, including successful payment and payment-failure compensation.

## Project Status

The project includes:

- Microservices architecture
- Service discovery
- Centralized configuration
- API Gateway
- Event-driven Kafka communication
- Avro event serialization
- Choreography Saga
- Compensation flow
- Resilience4j fault tolerance
- Micrometer/Prometheus monitoring
- Zipkin distributed tracing configuration
- Docker containerization
- Kubernetes deployment manifests
- Service-level tests
- End-to-end order workflow verification
