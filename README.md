# Bound

Bound — a social network for book lovers.

This repository contains the frontend (Angular / TypeScript) and backend (Spring Boot / Java) for Bound, a platform where readers can share reviews, follow fellow readers, and discover books.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Tech Stack](#tech-stack)
- [Repository Layout](#repository-layout)
- [Prerequisites](#prerequisites)
- [Local Development](#local-development)
  - [Backend (Spring Boot)](#backend-spring-boot)
  - [Frontend (Angular)](#frontend-angular)
  - [Running both with Docker Compose](#running-both-with-docker-compose)
- [Configuration & Environment](#configuration--environment)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Linting & Formatting](#linting--formatting)
- [Deployment Notes](#deployment-notes)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License & Contact](#license--contact)

---

## Project Overview

Bound connects book lovers: users can post reviews, follow other users, and get personalized recommendations. The frontend is implemented in Angular and communicates with a Spring Boot REST API backend. Authentication is token-based (JWT) and the backend persists data to a relational database.

## Tech Stack

- Frontend: Angular (TypeScript)
- Backend: Spring Boot (Java)
- Database: PostgreSQL (recommended) — or any JDBC-compatible RDBMS
- Build: npm / Angular CLI for frontend, Maven (or Gradle) for backend
- Optional: Docker & Docker Compose for local orchestration

## Repository Layout

- `/frontend` — Angular app (TypeScript, HTML, CSS)
- `/backend` — Spring Boot app (Java)
- `/docker` — optional Docker-related files and examples
- `README.md` — this file

If your repository differs, adjust paths accordingly.

## Prerequisites

Install the following on your machine:

- Node.js 16+ and npm (or yarn)
- Angular CLI (optional, for development): `npm install -g @angular/cli`
- Java 17+ (or the Java version your Spring Boot app requires)
- Maven 3.6+ (or Gradle if your backend uses it)
- Docker & Docker Compose (optional, for containerized setup)
- PostgreSQL (if not using Docker for DB)

## Local Development

### Backend (Spring Boot)

1. Open a terminal and go to the backend folder:
   ```
   cd backend
   ```

2. Configure environment variables (see the Configuration section below) or create `src/main/resources/application-local.properties` with your DB and JWT settings.

3. Build:
   ```
   ./mvnw clean package
   ```

4. Run:
   ```
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
   ```
   Or run the packaged JAR:
   ```
   java -jar target/*.jar --spring.profiles.active=local
   ```

Common Maven targets:
- `./mvnw test` — run backend tests
- `./mvnw -DskipTests package` — build without running tests

If your project uses Gradle, replace the Maven commands with `./gradlew` equivalents.

### Frontend (Angular)

1. Open a terminal and go to the frontend folder:
   ```
   cd frontend
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. Configure API base URL (see Configuration below). Many apps keep environment config in `src/environments/environment.ts` and `src/environments/environment.prod.ts`.

4. Serve the app locally:
   ```
   npm start
   ```
   Or with Angular CLI:
   ```
   ng serve --open
   ```

5. Build production bundle:
   ```
   npm run build
   ```
   or
   ```
   ng build --configuration production
   ```

Proxying / CORS: While developing, either enable CORS in the backend or use an Angular proxy (e.g., `proxy.conf.json`):
```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true
  }
}
```
Run with:
```
ng serve --proxy-config proxy.conf.json
```

### Running both with Docker Compose

A sample `docker-compose.yml` to start PostgreSQL, backend, and frontend (builds images) could look like:

```yaml
version: "3.8"
services:
  db:
    image: postgres:13
    environment:
      POSTGRES_DB: bound
      POSTGRES_USER: bound
      POSTGRES_PASSWORD: boundpass
    ports:
      - "5432:5432"
    volumes:
      - db-data:/var/lib/postgresql/data

  backend:
    build: ./backend
    depends_on:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/bound
      SPRING_DATASOURCE_USERNAME: bound
      SPRING_DATASOURCE_PASSWORD: boundpass
      SPRING_PROFILES_ACTIVE: docker
    ports:
      - "8080:8080"

  frontend:
    build: ./frontend
    ports:
      - "4200:80"
    depends_on:
      - backend

volumes:
  db-data:
```

Adjust service build contexts and Dockerfiles as needed.

## Configuration & Environment

Backend common environment variables (examples):

- SPRING_DATASOURCE_URL — JDBC URL, e.g. `jdbc:postgresql://localhost:5432/bound`
- SPRING_DATASOURCE_USERNAME — DB user
- SPRING_DATASOURCE_PASSWORD — DB password
- SPRING_PROFILES_ACTIVE — active Spring profile (e.g. `local`)
- JWT_SECRET — secret for signing tokens
- SERVER_PORT — optional override of backend port

Example application.properties snippet:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/bound
spring.datasource.username=bound
spring.datasource.password=boundpass
spring.jpa.hibernate.ddl-auto=update
jwt.secret=replace-with-strong-secret
```

Frontend configuration (example in `src/environments/environment.ts`):
```ts
export const environment = {
  production: false,
  apiBaseUrl: 'http://localhost:8080/api'
};
```

Store sensitive secrets securely (e.g., local environment variables, .env files excluded from git, or secrets manager in production).

## API Documentation

If the backend exposes OpenAPI/Swagger UI, it is commonly available at:

- Swagger UI: `http://localhost:8080/swagger-ui.html` or `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Check your Springdoc/Springfox configuration for exact paths.

## Testing

- Backend:
  ```
  cd backend
  ./mvnw test
  ```
- Frontend:
  ```
  cd frontend
  npm test
  ```
Make sure test databases or mocks are configured for CI runs.

## Linting & Formatting

- Frontend:
  ```
  cd frontend
  npm run lint
  ```
  (or `ng lint` depending on setup)

- Backend:
  Use your chosen Java style tools (Checkstyle, Spotless, etc.). Example:
  ```
  ./mvnw checkstyle:check
  ```

## Deployment Notes

- Build production frontend and serve with a static server or CDN.
- Package the backend as a JAR for deployment to your JVM host or containerize it.
- Use CI/CD pipelines to run tests, linting, and builds before deployment.
- Keep DB migrations under source control (Flyway or Liquibase recommended).

## Troubleshooting

- "Cannot connect to DB": verify JDBC URL, credentials, and that the database accepts connections.
- CORS errors: either enable CORS in the backend or use a proxy during development.
- Port conflicts: ensure distinct ports (frontend default 4200, backend 8080).
- JWT auth issues: confirm same secret is used for sign/verify and token format is correct.

## Contributing

- Fork the repository and create a topic branch per feature or fix.
- Follow existing coding style, add tests for new behavior.
- Open a Pull Request describing the change, testing steps, and any migration notes.
- Use meaningful commit messages and reference related issues.

## License & Contact

Add a `LICENSE` file to the repository (e.g., MIT) to make the project license explicit.

For questions or help, open an issue in this repository or reach out to the maintainers.
