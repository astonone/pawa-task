FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN ./gradlew build -x test

CMD ["java", "-jar", "build/libs/task-service-0.0.1-SNAPSHOT.jar"]