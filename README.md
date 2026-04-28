# SanFran Community API

API REST para administrar usuarios, zonas comunes y reservas de una comunidad residencial. El proyecto sigue una estructura de Clean Architecture, separando dominio, casos de uso, puertos, adaptadores y puntos de entrada HTTP.

## Caracteristicas

- CRUD de usuarios, instalaciones y reservas.
- Validaciones de negocio en dominio y en capa HTTP.
- Prevencion de reservas solapadas por instalacion y franja horaria.
- Hash de contrasenas con BCrypt.
- Documentacion OpenAPI disponible con Swagger UI.
- Base de datos H2 en memoria con datos semilla.
- Cobertura automatica con JaCoCo.

## Stack tecnico

- Java 25
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- Spring Security Crypto
- H2 Database
- springdoc OpenAPI
- JUnit 5, Mockito, AssertJ, MockMvc

## Arquitectura

El codigo esta organizado por capas:

- `applications/appservice`: configuracion de beans y ensamblaje de casos de uso.
- `domain/model`: entidades, value objects, excepciones y validaciones del negocio.
- `domain/usecase`: logica de aplicacion y puertos de entrada/salida.
- `infrastructure/drivenadapters/jpa`: repositorios Spring Data, entidades JPA y adaptadores de persistencia.
- `infrastructure/entrypoints/api`: controladores REST, DTOs, manejo global de errores y configuracion OpenAPI.

## Requisitos

- JDK 25 instalado.
- Git opcional para clonar el proyecto.
- No se requiere una base de datos externa.

## Ejecucion local

En Linux o macOS:

```bash
./gradlew bootRun
```

En Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

La aplicacion queda disponible en:

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`
- H2 Console: `http://localhost:8080/h2-console`

## Configuracion de base de datos

La aplicacion usa H2 en memoria con esta configuracion:

- JDBC URL: `jdbc:h2:mem:sanfrandb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL`
- User: `sa`
- Password: vacio

Los scripts `schema.sql` y `data.sql` se cargan automaticamente al iniciar.

## Datos semilla

Se crean usuarios, instalaciones y reservas de ejemplo para probar la API desde el arranque.

Usuarios iniciales:

- `11111111-1111-1111-1111-111111111111` - Ana Maria Perez - `RESIDENT/OWNER`
- `22222222-2222-2222-2222-222222222222` - Carlos Gomez - `STAFF/GUEST`
- `33333333-3333-3333-3333-333333333333` - Laura Rodriguez - `ADMIN/TENANT`

Instalaciones iniciales:

- `aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa` - Salon Social
- `bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb` - Cancha Multiple
- `cccccccc-cccc-cccc-cccc-cccccccccccc` - Piscina

## Pruebas y cobertura

Ejecutar toda la suite:

```bash
./gradlew test
```

En Windows PowerShell:

```powershell
.\gradlew.bat test
```

Al finalizar, JaCoCo genera reportes en:

- XML: `build/reports/jacoco/test/jacocoTestReport.xml`
- HTML: `build/reports/jacoco/test/html/index.html`

## Formato de respuesta

Las respuestas exitosas y de error comparten un envoltorio comun:

```json
{
	"timestamp": "2026-04-28T10:45:00Z",
	"status": 200,
	"data": {}
}
```

Ejemplo de error de validacion:

```json
{
	"timestamp": "2026-04-28T10:45:00Z",
	"status": 400,
	"data": {
		"error": "Validation Error",
		"field": "email",
		"message": "must be a well-formed email address"
	}
}
```

## Endpoints principales

Nota sobre busquedas por ID:

- El proyecto soporta ambas formas para busquedas especificas: `/{id}` y `?id={uuid}`.
- Se mantiene compatibilidad con la forma por ruta para evitar romper integraciones existentes.

### Users

- `POST /api/v1/users`
- `PUT /api/v1/users/{id}`
- `PUT /api/v1/users?id={uuid}`
- `PUT /api/v1/users?names=nombreExacto`
- `DELETE /api/v1/users/{id}`
- `DELETE /api/v1/users?id={uuid}`
- `DELETE /api/v1/users?names=nombreExacto`
- `GET /api/v1/users/{id}`
- `GET /api/v1/users?id={uuid}`
- `GET /api/v1/users?names=ana`

Notas para update/delete por query param en users:

- Si envias `id`, el update se aplica sobre ese registro.
- Si envias `names`, debe coincidir exactamente con un solo usuario.
- Si hay multiples coincidencias por nombre, la API responde con error y debes usar `id`.

Payload de ejemplo para crear:

```json
{
	"names": "Mateo Andrés Pineda Beltrán",
	"idDocument": "CC1066869266",
	"email": "mateo.pineda.be@gmail.com",
	"password": "ContraseñaSeguraObvi",
	"role": "ADMIN",
	"subRole": "OWNER"
}
```

Payload de ejemplo para modificar:

```json
{
	"names": "Mateo Andrés Pineda Beltrán Actualizado",
	"idDocument": "CC1066869266",
	"email": "mateo.pineda.actualizado@gmail.com",
	"password": "ContraseñaSeguraObvi2",
	"role": "ADMIN",
	"subRole": "OWNER"
}
```

### Facilities

- `POST /api/v1/facilities`
- `PUT /api/v1/facilities/{id}`
- `PUT /api/v1/facilities?id={uuid}`
- `PUT /api/v1/facilities?name=nombreExacto`
- `DELETE /api/v1/facilities/{id}`
- `DELETE /api/v1/facilities?id={uuid}`
- `DELETE /api/v1/facilities?name=nombreExacto`
- `GET /api/v1/facilities/{id}`
- `GET /api/v1/facilities?id={uuid}`
- `GET /api/v1/facilities?name=salon`

Payload de ejemplo:

```json
{
	"name": "Salon Social",
	"description": "Espacio para eventos y reuniones",
	"imageUrl": "https://example.com/salon.jpg",
	"capacity": 100
}
```

### Reservations

- `POST /api/v1/reservations`
- `PUT /api/v1/reservations/{id}`
- `PUT /api/v1/reservations?id={uuid}`
- `DELETE /api/v1/reservations/{id}`
- `DELETE /api/v1/reservations?id={uuid}`
- `GET /api/v1/reservations/{id}`
- `GET /api/v1/reservations?id={uuid}`
- `GET /api/v1/reservations?date=2026-05-10`

Payload de ejemplo:

```json
{
	"userId": "11111111-1111-1111-1111-111111111111",
	"facilityId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
	"date": "2026-05-10",
	"startTime": "09:00:00",
	"endTime": "11:00:00"
}
```

## Reglas de negocio relevantes

- El email y el documento del usuario deben ser unicos.
- Las contrasenas se almacenan cifradas, no en texto plano.
- Una reserva debe referenciar un usuario y una instalacion existentes.
- La fecha de reserva debe ser futura.
- La hora de fin debe ser mayor que la hora de inicio.
- No se permiten reservas traslapadas para la misma instalacion.

## Semantica HTTP

- `200 OK`: operaciones de lectura, actualizacion y eliminacion exitosas.
- `201 Created`: creacion exitosa.
- `400 Bad Request`: validaciones de entrada.
- `404 Not Found`: recurso no encontrado.
- `405 Method Not Allowed`: metodo HTTP no soportado para el endpoint.
- `409 Conflict`: violacion de integridad de datos (por ejemplo, intentar borrar una instalacion con reservas asociadas).
- `422 Unprocessable Entity`: regla de negocio incumplida.

## Documentacion interactiva

Swagger refleja los DTOs y respuestas tipadas del proyecto. Si se realizan cambios en controladores, DTOs o manejo de errores, la documentacion se actualiza automaticamente a traves de springdoc.
