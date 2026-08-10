# TCS Banking — prueba técnica Backend Java

Solución bancaria implementada con Java 21, Spring Boot, JPA/Hibernate y PostgreSQL.

## Módulos

- `tcs-banking-core`: clases compartidas de infraestructura y auditoría.
- `tcs-banking-backoffice`: administración de clientes y personas. Puerto `9098`.
- `tcs-banking-operaciones`: cuentas, movimientos y estado de cuenta. Puerto `9091`.

Backoffice y Operaciones se comunican en línea mediante HTTP con `RestClient`. La creación
de cuentas consulta inmediatamente la existencia, identificador y estado del cliente.

Los módulos utilizan la misma instancia PostgreSQL y separan sus tablas mediante los
esquemas `backoffice` y `operaciones`.

## Configuración local

La configuración actual espera PostgreSQL en:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tcs_bank
spring.datasource.username=admin
spring.datasource.password=123456789
```

Hibernate administra el esquema local mediante `spring.jpa.hibernate.ddl-auto=update`.

## Nomenclaturas

### Tipo de identificación

| Código | Descripción | Longitud |
|---|---|---:|
| `CED` | Cédula | 10 dígitos |
| `RUC` | Registro Único de Contribuyentes | 13 dígitos |

### Tipo de cuenta

| Código | Descripción |
|---|---|
| `AHO` | Cuenta de Ahorro |
| `CTE` | Cuenta Corriente |

### Tipo de movimiento

| Código | Descripción |
|---|---|
| `DEP` | Depósito |
| `RET` | Retiro |

Los códigos se utilizan sin conversiones en requests JSON, Java, responses JSON y base de
datos.

## Endpoints

### Clientes

```text
POST   /api/v1/mantenimiento/clientes/crear
GET    /api/v1/mantenimiento/clientes/listar
GET    /api/v1/mantenimiento/clientes/consultarPersonaPorIdentificacion/{identificacion}
GET    /api/v1/mantenimiento/clientes/identificacion/{identificacion}
PUT    /api/v1/mantenimiento/clientes/actualizar/{identificacion}
DELETE /api/v1/mantenimiento/clientes/eliminar/{identificacion}
```

### Cuentas

```text
POST   /api/v1/operaciones/cuentas/crear
GET    /api/v1/operaciones/cuentas/listar
PUT    /api/v1/operaciones/cuentas/actualizar/{numeroCuenta}
DELETE /api/v1/operaciones/cuentas/eliminar/{numeroCuenta}
```

### Movimientos

```text
POST   /api/v1/operaciones/movimientos/transaccion
GET    /api/v1/operaciones/movimientos/listar
GET    /api/v1/operaciones/movimientos/consultar/{idMovimiento}
DELETE /api/v1/operaciones/movimientos/eliminar/{idMovimiento}
```

La creación de movimientos es transaccional. La cuenta se bloquea mediante
`PESSIMISTIC_WRITE`; el movimiento y el nuevo saldo se confirman en una sola transacción.
Un retiro que supera el saldo responde con `Saldo no disponible`.

Ejemplo:

```json
{
  "tipoMovimiento": "DEP",
  "valor": 100.00,
  "numeroCuenta": "7909950040"
}
```

### Estado de cuenta

```text
POST /api/v1/operaciones/reportes/estado-cuenta
```

```json
{
  "identificacionCliente": "0945678901",
  "fechaInicio": "01-08-2026",
  "fechaFin": "30-08-2026"
}
```

El reporte contiene el cliente, todas sus cuentas y los movimientos de cada cuenta dentro
del rango inclusivo indicado.

## Compilación y pruebas

```bash
cd tcs-banking-core
mvn clean install

cd ../tcs-banking-backoffice
mvn clean package

cd ../tcs-banking-operaciones
mvn clean package
```

Los controllers retornan DTOs de response y no serializan directamente las entidades JPA.
Las contraseñas y los campos internos de auditoría no se exponen en los contratos REST.

## Ejecución con Docker Compose

La contenerización incluye PostgreSQL 17, Backoffice y Operaciones. Cada aplicación se
compila en una imagen multi-stage con Java 21 y se ejecuta con un usuario sin privilegios.

Preparar las variables locales:

```bash
cp .env.example .env
```

Antes de iniciar, reemplazar `POSTGRES_PASSWORD` en `.env`. El archivo `.env` está excluido
de Git.

Construir e iniciar:

```bash
docker-compose up --build -d
```

Consultar el estado y los logs:

```bash
docker-compose ps
docker-compose logs -f backoffice operaciones
```

Servicios expuestos:

```text
Backoffice: http://localhost:9098
Operaciones: http://localhost:9091
PostgreSQL: localhost:5432
```

Detener los contenedores conservando la información de PostgreSQL:

```bash
docker-compose down
```

Para eliminar también el volumen de datos, utilizar explícitamente:

```bash
docker-compose down -v
```

## Pendientes de entrega

- Exportar la colección Postman al repositorio.
- Incorporar `BaseDatos.sql`.
