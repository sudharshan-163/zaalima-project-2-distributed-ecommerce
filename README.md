\# Zaalima Project 2 — Distributed E-Commerce Microservices



A distributed e-commerce backend developed using a microservices architecture with event-driven communication, Kafka, Avro events, Choreography Saga, Resilience4j, distributed tracing, monitoring, Docker, and Kubernetes deployment manifests.



\## Architecture



The project contains the following services:



\* \*\*Service Registry (Eureka Server)\*\* — Port `8761`

\* \*\*Config Server\*\* — Port `8888`

\* \*\*API Gateway\*\* — Port `8090`

\* \*\*Order Service\*\* — Port `8081`

\* \*\*Inventory Service\*\* — Port `8082`

\* \*\*Payment Service\*\* — Port `8083`

\* \*\*Notification Service\*\* — Port `8084`



Infrastructure:



\* Apache Kafka — `localhost:9092`

\* Prometheus

\* Zipkin

\* Dockerfiles

\* Kubernetes manifests



\## Technology Stack



\* Java

\* Spring Boot

\* Spring Cloud

\* Spring Cloud Eureka

\* Spring Cloud Config

\* Spring Cloud Gateway

\* Apache Kafka

\* Apache Avro

\* Resilience4j

\* Micrometer

\* Prometheus

\* Zipkin

\* Docker

\* Kubernetes

\* Maven



\## Event-Driven Communication



Kafka is used for asynchronous communication between services.



The project uses Avro-based events for event serialization.



The main order flow is:



```text

Client

&#x20; |

&#x20; v

API Gateway

&#x20; |

&#x20; v

Order Service

&#x20; |

&#x20; | Order Created Event

&#x20; v

Inventory Service

&#x20; |

&#x20; | Stock Reserved

&#x20; v

Payment Service

&#x20; |

&#x20; | Payment Success

&#x20; v

Order Status = PAID

```



\## Choreography Saga



The order workflow follows a choreography-based Saga pattern.



\### Successful Flow



```text

Order Created

&#x20;     |

&#x20;     v

Inventory reserves stock

&#x20;     |

&#x20;     v

Payment succeeds

&#x20;     |

&#x20;     v

Order becomes PAID

```



\### Payment Failure / Compensation Flow



```text

Order Created

&#x20;     |

&#x20;     v

Inventory reserves stock

&#x20;     |

&#x20;     v

Payment fails

&#x20;     |

&#x20;     v

Stock Release

&#x20;     |

&#x20;     v

Order becomes CANCELLED

```



This provides compensation for the distributed transaction without using a centralized transaction coordinator.



\## Resilience



Resilience4j patterns are used in the services for fault tolerance.



The project includes resilience mechanisms such as:



\* Circuit Breaker

\* Retry

\* Time Limiter

\* Fallback handling



\## Monitoring



Micrometer and Prometheus are used for application metrics.



The services expose metrics through Spring Boot Actuator.



Prometheus can collect application metrics from the configured actuator endpoints.



\## Distributed Tracing



Micrometer Tracing with Brave and Zipkin Reporter is configured for distributed tracing.



Tracing is configured with:



```text

management.tracing.sampling.probability=1.0

```



Zipkin endpoint:



```text

http://localhost:9411/api/v2/spans

```



Zipkin UI is normally available at:



```text

http://localhost:9411

```



> Zipkin runtime availability depends on the local Zipkin instance being started.



\## Security



The API Gateway includes JWT-based security configuration.



Requests can be routed through the gateway to the appropriate microservice using service discovery.



\## API Gateway Routes



The gateway uses Eureka service discovery and load-balanced routes for the microservices.



Example logical routes include:



```text

/inventory/\*\*  -> INVENTORY-SERVICE

/payment/\*\*    -> PAYMENT-SERVICE

/order/\*\*      -> ORDER-SERVICE

```



\## Testing



The project contains service-level tests for the core services.



Verified areas include:



\* Order Service

\* Inventory Service

\* Payment Service

\* Notification Service



The distributed order workflow was also verified for:



1\. Successful payment → Order becomes `PAID`

2\. Payment failure → Stock is released and Order becomes `CANCELLED`



\## Docker



Dockerfiles are provided for:



\* Order Service

\* Inventory Service

\* Payment Service

\* Notification Service



The Dockerfiles package the generated Spring Boot JAR files into Java 17 runtime containers.



\## Kubernetes



Kubernetes manifests are provided under:



```text

k8s/

```



The manifests include Deployments and ClusterIP Services for the application microservices.



> Kubernetes deployment requires a working Docker/Kubernetes environment. The manifests are included as deployment artifacts; runtime deployment should be verified in an environment where Docker and kubectl are available.



\## Project Structure



```text

zaalima-project-2-distributed-ecommerce/

│

├── api-gateway/

├── config-server/

├── service-registry/

├── order-service/

├── inventory-service/

├── payment-service/

├── notification-service/

│

├── k8s/

│   ├── order-service.yaml

│   ├── inventory-service.yaml

│   ├── payment-service.yaml

│   └── notification-service.yaml

│

└── README.md

```



\## Running the Project



Start the infrastructure and services in the required order:



```text

1\. Service Registry

2\. Config Server

3\. API Gateway

4\. Order Service

5\. Inventory Service

6\. Payment Service

7\. Notification Service

8\. Kafka

9\. Prometheus

10\. Zipkin

```



Build a service using Maven:



```powershell

.\\mvnw.cmd clean package

```



Run tests:



```powershell

.\\mvnw.cmd clean test

```



\## Verification



After starting the services:



\### Eureka



```text

http://localhost:8761

```



\### API Gateway



```text

http://localhost:8090

```



\### Zipkin



```text

http://localhost:9411

```



\### Prometheus



```text

http://localhost:9090

```



\## Project Status



Core implementation, service-level testing, event-driven order workflow, Saga compensation flow, resilience configuration, monitoring configuration, distributed tracing configuration, Dockerfiles, and Kubernetes manifests are included in the project.



Docker/Kubernetes runtime deployment requires the corresponding local tools or deployment environment.



