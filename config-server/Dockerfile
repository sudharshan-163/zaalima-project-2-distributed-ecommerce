FROM eclipse-temurin:17-jre
WORKDIR /app
COPY config-server/target/config-server-0.0.1-SNAPSHOT.jar app.jar
COPY config-repository /config-repository
EXPOSE 8888
ENTRYPOINT ["java","-jar","app.jar"]
