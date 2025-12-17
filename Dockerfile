# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/urlshortener-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render will map dynamically)
EXPOSE 8080

# Use Render-assigned port
ENV PORT=8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
