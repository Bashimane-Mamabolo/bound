# Build stage
FROM maven:3-openjdk-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17-alpine
ARG PROFILE=dev
ARG APP_VERSION=0.0.1


WORKDIR /app
COPY --from=build /build/target/bound-backend-*.jar /app/

EXPOSE 8088

ENV DB_URL=jdbc:postgresql://postgres-sql-bsc:5432/book_social_club
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}


# Executes when running the container itself
CMD java -jar \
    -Dspring.profiles.active=${ACTIVE_PROFILE} \
    -Dspring.datasource.url=${DB_URL} \
    -Dspring.datasource.username=${POSTGRES_USER} \
    -Dspring.datasource.password=${POSTGRES_PASSWORD} \
    bound-backend-${JAR_VERSION}.jar
