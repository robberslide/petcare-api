# PetCare – API Clínica Veterinaria

Backend en Spring Boot 3 (Java 17+) con arquitectura **Entity → Repository → Service → Controller**.

## Cómo ejecutar
1. Abrir la carpeta en IntelliJ / VS Code (proyecto Maven).
2. Ejecutar `PetcareApplication` (o `mvn spring-boot:run`).
3. API en `http://localhost:8080` · Consola BD: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:petcare`, usuario `sa`).

## Endpoints
| Recurso | Endpoints |
|---|---|
| Dueños | GET/POST `/api/duenos` · GET/PUT/DELETE `/api/duenos/{id}` · GET `/api/duenos/{id}/mascotas` |
| Mascotas | GET/POST `/api/mascotas` · GET/PUT/DELETE `/api/mascotas/{id}` · GET `/api/mascotas/{id}/citas` |
| Citas | GET/POST `/api/citas` · GET/PUT/DELETE `/api/citas/{id}` |

## Ejemplos de body
```json
// POST /api/duenos
{ "rut": "12345678-9", "nombre": "Ana Pérez", "telefono": "+56911112222", "email": "ana@mail.cl" }

// POST /api/mascotas
{ "nombre": "Firulais", "especie": "Perro", "raza": "Quiltro", "fechaNacimiento": "2021-05-10", "dueno": { "id": 1 } }

// POST /api/citas
{ "fecha": "2026-11-20T10:30:00", "motivo": "Vacunación", "diagnostico": "Sano", "costo": 15000, "mascota": { "id": 1 } }
```

## Validaciones (capa Service)
- No se crea una cita para una mascota inexistente → 404.
- No se crea una mascota para un dueño inexistente → 404.
- RUT de dueño único → 400.
- Fecha de nacimiento no futura, costo no negativo, campos obligatorios → 400.

## Postman
Importar `PetCare.postman_collection.json`. Orden sugerido: Dueños → Mascotas → Citas → Validaciones.

## Logrado / Pendiente
- [x] Entidades JPA con relaciones
- [x] Repositories, Services con validaciones, Controllers CRUD
- [x] Endpoints de relación
- [ ] Capturas de Postman
- [ ] Frontend React (opcional)
