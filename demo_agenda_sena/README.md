# AgendaSENA — API REST de Reserva de Ambientes de Formación

API REST desarrollada con Spring Boot que centraliza la gestión de reservas de ambientes
de formación del SENA, aplicando reglas de negocio automáticas para evitar cruces de
horario, sobrecupo y uso indebido de los espacios.

---

##  Tecnologías utilizadas

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- MySQL 8.x
- Maven

---

##  Cómo ejecutar el proyecto

### 1. Requisitos previos

Tener instalado:
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) o superior
- [MySQL 8.x](https://dev.mysql.com/downloads/mysql/)
- [Maven](https://maven.apache.org/download.cgi) (opcional, el proyecto incluye `mvnw`)

Verificar instalación:
```bash
java -version
mysql --version
```

---

### 2. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/AgendaSENA.git
cd AgendaSENA/demo_agenda_sena
```

---

### 3. Configurar la base de datos MySQL

Crea la base de datos en MySQL antes de ejecutar el proyecto:

```sql
CREATE DATABASE agenda_sena;
```

Luego verifica que el archivo `src/main/resources/application.properties` tenga tus credenciales:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/agenda_sena
spring.datasource.username=root
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

>  `ddl-auto=create-drop` elimina y recrea las tablas cada vez que el proyecto reinicia. Esto es intencional para el entorno de pruebas.

---

### 4. Ejecutar el proyecto

```bash
./mvnw spring-boot:run
```

En Windows:
```bash
mvnw.cmd spring-boot:run
```

La API quedará corriendo en:
```
http://localhost:8080
```

Al arrancar verás en consola:
```
 Ambientes creados: 6
 Reservas creadas: 6
 Datos listos para la sustentación!
```

---

##  Datos de prueba cargados automáticamente

Al iniciar, el sistema carga automáticamente:

### Ambientes
| ID | Nombre | Tipo | Capacidad | Activo |
|---|---|---|---|---|
| 1 | Aula 101 | SALA | 30 | 
| 2 | Aula 102 | SALA | 25 | 
| 3 | Laboratorio de Redes | LABORATORIO | 20 | 
| 4 | Auditorio Principal | AUDITORIO | 100 | 
| 5 | Aula 103 (Inactiva) | SALA | 15 | 
| 6 | Laboratorio de Programación | LABORATORIO | 25 | 

### Reservas
| ID | Ambiente | Instructor | Fecha | Estado |
|---|---|---|---|---|
| 1 | Aula 101 | Carlos Pérez | 25/06/2026 08:00–10:00 | ACTIVA |
| 2 | Lab. Redes | María Gómez | 25/06/2026 14:00–16:00 | ACTIVA |
| 3 | Aula 102 | Juan Rodríguez | 25/06/2026 10:00–12:00 | CANCELADA |
| 4 | Auditorio | Ana Martínez | 23/06/2026 08:00–10:00 | FINALIZADA |
| 5 | Aula 101 | Pedro Sánchez | 26/06/2026 13:00–15:00 | ACTIVA |
| 6 | Lab. Programación | Laura Fernández | 25/06/2026 16:00–18:00 | ACTIVA |

---

##  Endpoints disponibles

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/ambientes` | Listar todos los ambientes |
| POST | `/api/ambientes` | Registrar un ambiente |
| GET | `/api/reservas` | Listar todas las reservas |
| POST | `/api/reservas` | Crear una reserva |
| PATCH | `/api/reservas/{id}/cancelar` | Cancelar una reserva |
| GET | `/api/ambientes/{id}/reservas?fecha=YYYY-MM-DD` | Reservas activas de un ambiente por fecha |
| GET | `/api/ambientes/disponibles?inicio=...&fin=...` | Ambientes libres en un rango de tiempo |
| GET | `/api/reportes/ocupacion?fecha=YYYY-MM-DD` | Reporte de ocupación del día |

---

##  Reglas de negocio implementadas

1. **Sin cruces de horario** — un ambiente no puede tener dos reservas activas que se solapen
2. **Capacidad** — el número de aprendices no puede superar la capacidad del ambiente
3. **Horario institucional** — solo entre las 6:00 y las 22:00, duración entre 1 y 4 horas
4. **Ambientes inactivos** — no se puede reservar un ambiente con `activo = false`
5. **Límite por instructor** — máximo 3 reservas activas por día por instructor
6. **Cancelación con anticipación** — solo se puede cancelar con al menos 2 horas de anticipación
7. **No en el pasado** — la fecha de inicio debe ser posterior al momento actual

---

##  Estructura del proyecto

```
src/
└── main/
    └── java/com/example/demo_agenda_sena/
        ├── config/          # DataLoader con datos de prueba
        ├── controllers/     # Controladores REST
        ├── dto/             # Objetos de transferencia de datos
        ├── entitys/         # Entidades JPA
        ├── enums/           # TipoAmbiente, EstadoReserva
        ├── exception/       # Manejo centralizado de errores
        ├── repository/      # Repositorios Spring Data JPA
        └── services/        # Lógica de negocio
```

---

##  Autor

- **Nombre:** Tu nombre aquí
- **Programa:** Análisis y Desarrollo de Software — SENA
- **Trimestre:** Primer trimestre de Spring Boot
