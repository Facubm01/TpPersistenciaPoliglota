# TpPersistenciaPoliglota - Docker

## Prerrequisitos
- Docker y Docker Compose instalados

## Levantar todo (app + MySQL + MongoDB + Cassandra)

```bash
docker compose up -d --build
```

Esto va a:
- Construir la imagen de la app (Java 17, Spring Boot)
- Levantar MySQL (tpdb), MongoDB y Cassandra
- Exponer la app en http://localhost:8080

## Ver logs de la app

```bash
docker compose logs -f app
```

## Apagar servicios

```bash
docker compose down
```

## Variables de conexión
Las conexiones en ejecución dentro de Docker usan nombres de servicio:
- MySQL: `jdbc:mysql://mysql:3306/tpdb`
- MongoDB: `mongodb://root:root@mongo:27017/tpsensores_meta?authSource=admin`
- Cassandra: host `cassandra`, puerto `9042`, datacenter `datacenter1`

Para ejecución local sin Docker, el `application.properties` apunta a `localhost`.


