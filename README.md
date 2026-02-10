# EcoTrack 🌱

**EcoTrack** es una plataforma web integral diseñada para fomentar y gestionar iniciativas ambientales, centrándose especialmente en la recolección de residuos y la localización de puntos de reciclaje ("Puntos Verdes"). Este proyecto combina tecnología moderna con gamificación para incentivar la participación comunitaria en el cuidado del medio ambiente.

## 📋 Tabla de Contenidos

1. [Descripción del Proyecto](#descripción-del-proyecto)
2. [Características Principales](#características-principales)
3. [Tecnologías Utilizadas](#tecnologías-utilizadas)
4. [Requisitos Previos](#requisitos-previos)
5. [Instalación y Configuración](#instalación-y-configuración)
6. [Estructura del Proyecto](#estructura-del-proyecto)
7. [Contribución](#contribución)

## 📖 Descripción del Proyecto

EcoTrack nace de la necesidad de conectar a los ciudadanos con los recursos de reciclaje disponibles en su comunidad. La aplicación permite a los usuarios localizar puntos de entrega de residuos, registrar sus aportes, y visualizar estadísticas de impacto ambiental. Para los administradores, ofrece herramientas robustas para la gestión de usuarios, puntos de recolección y monitoreo de la actividad en la plataforma.

## ✨ Características Principales

*   **🗺️ Mapa Interactivo de Puntos Verdes:** Visualización geoespacial de ubicaciones de reciclaje utilizando PostGIS. Permite a los usuarios encontrar los puntos más cercanos.
*   **♻️ Gestión de Residuos:** Clasificación y seguimiento de diferentes tipos de residuos.
*   **🏆 Gamificación y Ranking:** Sistema de medallas y tablas de clasificación para motivar a los usuarios a reciclar más.
*   **📊 Panel de Estadísticas:** Visualización de datos sobre recolecciones e impacto ambiental.
*   **👥 Gestión de Usuarios y Roles:** Sistema de autenticación seguro con múltiples roles (ej. Administrador, Usuario) para controlar el acceso a funcionalidades sensibles.
*   **📅 Registro de Recolecciones:** Historial detallado de las actividades de recolección.

## 🛠️ Tecnologías Utilizadas

Este proyecto ha sido construido siguiendo las mejores prácticas de desarrollo de software, utilizando un stack tecnológico robusto y escalable:

*   **Lenguaje:** Java 21
*   **Framework Backend:** Spring Boot 3.5.3
    *   *Spring Web (MVC)*
    *   *Spring Data JPA*
    *   *Spring Security (con JWT)*
    *   *Spring Validation*
*   **Base de Datos:** PostgreSQL con extensión **PostGIS** (para datos geográficos).
*   **Frontend / Vistas:** Thymeleaf (Motor de plantillas del lado del servidor).
*   **Contenerización:** Docker & Docker Compose.
*   **Gestión de Dependencias:** Maven.
*   **Herramientas Adicionales:** Lombok, DevTools.

## ⚙️ Requisitos Previos

Antes de comenzar, asegúrate de tener instalado lo siguiente en tu sistema:

*   [Java JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
*   [Docker](https://www.docker.com/) y Docker Compose
*   [Maven](https://maven.apache.org/) (Opcional, si usas el wrapper `mvnw` incluido)

## 🚀 Instalación y Configuración

Sigue estos pasos para levantar el entorno de desarrollo:

1.  **Clonar el repositorio:**

    ```bash
    git clone <url-del-repositorio>
    cd ecotrack
    ```

2.  **Configurar la Base de Datos con Docker:**

    El proyecto incluye un archivo `docker-compose.yml` en la carpeta `database/` para levantar PostgreSQL y pgAdmin automáticamente.

    ```bash
    cd database
    docker-compose up -d
    cd ..
    ```

    *Nota: Esto levantará una instancia de PostgreSQL en el puerto `5432` y pgAdmin en el puerto `5050`.*

3.  **Configurar Variables de Entorno (Opcional):**
    
    Verifica el archivo `src/main/resources/application.properties` para asegurarte de que las credenciales de la base de datos coincidan con las definidas en el `docker-compose.yml`.

4.  **Ejecutar la Aplicación:**

    Puedes usar el wrapper de Maven incluido para ejecutar la aplicación:

    ```bash
    ./mvnw spring-boot:run
    ```

    O si tienes Maven instalado globalmente:

    ```bash
    mvn spring-boot:run
    ```

5.  **Acceder a la Aplicación:**
    
    Una vez que la aplicación inicie, abre tu navegador y visita:
    `http://localhost:8080` (o el puerto configurado).

## 📂 Estructura del Proyecto

La estructura del código fuente sigue la arquitectura estándar de Spring Boot:

```
src/main/java/com/ecotrack/ecotrack
├── configuration   # Configuraciones de seguridad, web, etc.
├── controller      # Controladores MVC (Manejo de peticiones HTTP)
├── dto             # Data Transfer Objects (Objetos de transferencia de datos)
├── model           # Entidades JPA (Modelos de base de datos)
├── repository      # Interfaces de acceso a datos (Spring Data JPA)
└── service         # Lógica de negocio
    └── impl        # Implementaciones de los servicios
```

## 🤝 Contribución

¡Las contribuciones son bienvenidas! Si deseas mejorar EcoTrack, por favor sigue estos pasos:

1.  Haz un Fork del repositorio.
2.  Crea una rama para tu funcionalidad (`git checkout -b feature/NuevaFuncionalidad`).
3.  Realiza tus cambios y haz Commit (`git commit -m 'Agrega nueva funcionalidad'`).
4.  Haz Push a la rama (`git push origin feature/NuevaFuncionalidad`).
5.  Abre un Pull Request.

---
Generado con ❤️ para el equipo de EcoTrack.
