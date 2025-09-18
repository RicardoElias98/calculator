FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/calculator-core-0.0.1-SNAPSHOT.jar calculator-core.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
ENTRYPOINT ["sh", "/wait-for-it.sh", "kafka:9092", "--", "java", "-jar", "calculator-core.jar"]