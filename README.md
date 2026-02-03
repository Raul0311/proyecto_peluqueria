# ✂️ Peluquería Master Project

Sistema integral de gestión para peluquerías basado en una **arquitectura multimódulo** con Java y Spring Boot. Este proyecto está diseñado siguiendo principios de integración y despliegue continuo (CI/CD).

## 🏗️ Arquitectura del Proyecto

El proyecto se divide en tres módulos principales gestionados por un **POM Padre**:

* **`project_jar`**: Librería compartida que contiene las utilidades comunes.
* **`Back-End`**: API REST encargada de la lógica de negocio, persistencia con JPA y seguridad con token de usuario.
* **`Front-End`**: Interfaz de usuario basada en JSPs y Spring MVC que consume la API, también se encarga de la seguridad CSRF y autenticación.



---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.5.10
* **Seguridad:** Spring Security (Custom Providers)
* **Persistencia:** Spring Data JPA + MySQL
* **Documentación:** Springdoc-OpenAPI (Swagger)
* **DevOps:** * **Jenkins:** Automatización de builds y releases.
    * **Artifactory:** Repositorio de artefactos (Snapshots y Releases).
    * **Gitea:** Control de versiones (Git).

---

## 🚀 Configuración y Ejecución

### Requisitos Previos
1.  **JDK 21** instalado.
2.  **Maven 3.9+** configurado.
3.  Servidor **MySQL** en ejecución.
4.  Configurar credenciales de Artifactory en tu `~/.m2/settings.xml`.

### Instalación Local
Clona el repositorio y compila desde la raíz para instalar todos los módulos:

```bash
git clone [https://github.com/Raul0311/proyecto_peluqueria.git](https://github.com/Raul0311/proyecto_peluqueria.git)
cd proyecto-peluqueria
mvn clean install -DskipTests
