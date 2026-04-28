# SanFran Community API

API REST para gestion de usuarios, zonas comunes y reservas, implementada con Clean Architecture.

## Stack

- Java 25
- Spring Boot 4.0.6
- Spring Data JPA
- H2 en memoria
- OpenAPI/Swagger (springdoc)

## Arquitectura

Estructura principal:

- applications/appservice: arranque y configuracion de casos de uso
- domain/model: entidades, excepciones y validaciones de negocio
- domain/usecase: casos de uso y puertos
- infrastructure/drivenadapters/jpa: adaptadores y persistencia JPA
- infrastructure/entrypoints/api: controladores REST, DTOs, excepciones API, OpenAPI

## Ejecutar

```bash
./gradlew bootRun
```

En Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

## Documentacion API

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## H2 Console

- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:sanfrandb
- User: sa
- Password: (vacio)

## Endpoints principales

### Users

- POST /api/v1/users
- PUT /api/v1/users/{id}
- DELETE /api/v1/users/{id}
- GET /api/v1/users/{id}
- GET /api/v1/users?names=ana

### Facilities

- POST /api/v1/facilities
- PUT /api/v1/facilities/{id}
- DELETE /api/v1/facilities/{id}
- GET /api/v1/facilities/{id}
- GET /api/v1/facilities?name=salon

### Reservations

- POST /api/v1/reservations
- PUT /api/v1/reservations/{id}
- DELETE /api/v1/reservations/{id}
- GET /api/v1/reservations/{id}
- GET /api/v1/reservations?date=2026-05-10

## Datos iniciales

La aplicacion carga automaticamente:

- schema.sql
- data.sql

Esto deja datos de prueba listos en H2 al iniciar.
# SanFranCommunity
# SanFranCommunity
# SanFranCommunity
