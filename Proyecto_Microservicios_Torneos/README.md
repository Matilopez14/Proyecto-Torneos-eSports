
Sistema de Gestión de Torneos eSports

Este proyecto consiste en una plataforma backend centralizada para la administración y automatización de torneos de videojuegos competitivos (eSports). El sistema está diseñado bajo una **arquitectura de microservicios** utilizando **Spring Boot 3.x** y Java, donde cada módulo implementa el patrón de diseño **CSR (Controller-Service-Repository)** para garantizar modularidad, escalabilidad y una separación limpia de responsabilidades.

---

  Información del Equipo
**Integrantes:**
  * Ariel Velasquez
  * Matias Lopez
  * Daniel Muñoz

---

Tecnologías y Arquitectura
* **Framework Principal:** Spring Boot 3.x (Java 17+)
* **Gestión de Dependencias:** Apache Maven
* **Validación de Datos:** Jakarta Validation API (`jakarta.validation`)
* **Patrón de Diseño Interno:** CSR (Controller -> Service -> Repository)
* **Herramienta de Versionamiento:** Git & GitHub

---
  Funcionalidades Implementadas (Estructura de 10 Microservicios)

El ecosistema de la aplicación se divide en los siguientes microservicios independientes, cada uno encargado de un dominio específico del negocio bajo el patrón CSR:

1. **`msjugadores`**
   * **Descripción:** Gestión de perfiles competitivos de los pro-players, registro de usuarios y control de datos personales. Implementa validaciones en la capa de controladores (`@Valid`) utilizando `jakarta.validation`.

2. **`msequipos`**
   * **Descripción:** Registro, edición y control de las escuadras, administración de los rosters de jugadores y asignación organizativa de los clubes.

3. **`msrankings`**
   * **Descripción:** Procesamiento automático de tablas de posiciones, cómputo de clasificaciones globales e historiales de rendimiento.

4. **`mspremios`**
   * **Descripción:** Administración del pozo de dinero (*prize pool*), distribución de incentivos financieros y asignación de trofeos según resultados.

5. **`mstransmisiones`**
   * **Descripción:** Coordinación de la agenda de streaming, vinculación de URLs con plataformas en vivo (Twitch/YouTube) y control de horarios oficiales.

6. **`mspartidas`**
   * **Descripción:** Control del ciclo de vida de los enfrentamientos en vivo, registro de emparejamientos (brackets), ingreso de resultados en tiempo real y asignación de llaves del torneo.

7. **`msnotificaciones`**
   * **Descripción:** Servicio de mensajería y alertas
     
8. **`msinscripciones`**
   * **Descripción:** Módulo de gestión para el proceso de postulación y pago (si aplica) de los equipos o jugadores que desean asegurar un cupo dentro de un torneo vigente.

9. **`msestadisticas`**
   * **Descripción:** Motor analítico encargado de recopilar métricas de juego avanzadas (KDA, porcentaje de victorias, mapas jugados) tanto para jugadores individuales como para escuadras completas.

10. **`mstorneos`**
    * **Descripción:** Microservicio central encargado de la creación, parametrización y configuración de las reglas generales, fechas y formatos de cada evento competitivo.

---

 Pasos para Ejecutar el Proyecto Localmente

Siga estas instrucciones técnicas para levantar el entorno de desarrollo y probar el funcionamiento de los microservicios de forma local:

 Prerrequisitos
* **Java JDK 17** o superior instalado y configurado en las variables de entorno.
* **Apache Maven** instalado (o uso del Maven Wrapper `./mvnw` incluido).
* Un IDE compatible (Recomendado: **IntelliJ IDEA**).

Paso 1: Clonar el Repositorio
Abra la terminal de su sistema y ejecute el comando de clonación para descargar el código fuente:
```bash
git clone [https://github.com/Matilopez14/Proyecto-Torneos-eSports.git](https://github.com/Matilopez14/Proyecto-Torneos-eSports.git)
cd Proyecto-Torneos-eSports-main

Paso 2: Configuración del Entorno (application.properties)
Cada microservicio es completamente independiente y posee su propio archivo de configuración en la ruta src/main/resources/application.properties.

Revise que las propiedades de puertos (server.port) no entren en conflicto al ejecutar los 10 servicios en paralelo.

Paso 3: Compilación del Código Fuente
Sitúese en el directorio raíz de la carpeta general o de cada microservicio en la terminal y construya el proyecto utilizando Maven para descargar las dependencias y validar que no existan errores de compilación:
mvn clean package

Paso 4: Inicialización de los Servicios
Una vez creado el empaquetado de manera exitosa, ejecute el archivo binario .jar generado a través de la consola o presione el botón de ejecución (Run) directamente sobre la clase principal de cada servicio en IntelliJ:

Bash
java -jar target/[nombre-del-microservicio].jar
```

---

### Exportación y Compartición del Proyecto

Para enviar este proyecto a otra persona, siga estos pasos para asegurar que el archivo sea ligero y contenga solo lo necesario:

1. **Limpiar los archivos de compilación:**
   Antes de comprimir, es vital eliminar las carpetas `target` de todos los microservicios, ya que contienen archivos generados que pueden pesar cientos de megabytes. 
   Desde la terminal, en la carpeta raíz del proyecto, puede ejecutar:
   ```bash
   # Si tiene Maven instalado globalmente
   mvn clean
   ```
   *Nota: Deberá hacerlo en cada carpeta de microservicio si no hay un pom.xml raíz, o simplemente borrar manualmente las carpetas `target` de cada uno.*

2. **Excluir carpetas del IDE:**
   No es necesario enviar la carpeta `.idea` (de IntelliJ) ni carpetas de configuración personal de su editor.

3. **Comprimir el proyecto:**
   Comprima la carpeta principal `Proyecto-Torneos-eSports-main` en un archivo **.zip** o **.7z**.

4. **Requisitos para el destinatario:**
   Asegúrese de informar al destinatario que para ejecutar el proyecto necesitará:
   * **Docker y Docker Desktop** (para levantar la base de datos y servicios mediante `docker-compose up`).
   * **Java 17 JDK**.
   * **Maven 3.8+**.

