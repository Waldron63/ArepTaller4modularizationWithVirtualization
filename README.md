
# Taller de de modularización con virtualización e Introducción a Docker

La intención de este taller, es mostrar el desarrollo para la implementacion de un servidor Web en java (tipo APACHE) el cual entrega una página completa HTML con imagenes y diseño, al igual que aplicaciones web a partir de POJOS.
Generando los archivos en Docker y desplegandolos en AWS con EC2; la intención tambien es complementar el desarrollo de los servicios rest para agregar path a los controladores, y un mejor sistema de hilos y apagado elegante.

## Contenidos iniciales

El servidor escanea y registra dinámicamente los controladores y rutas utilizando anotaciones personalizadas como:

- @RestController: Marca una clase como un controlador que manejará solicitudes HTTP.
- @GreetingController:Genera una ruta base para utilizar el controlador (tipo Endpoint).
- @GetMapping: Asocia métodos de un controlador con solicitudes GET.
- @RequestParam: Permite extraer parámetros de la URL en los métodos del controlador.
- @RequestMapping: Permite adicionar a las clases controllers, un path para redireccionamiento.

### Prerrequisitos

Para el correcto uso del servidor, es necesario tener las siguientes aplicaciones instaladas:
- JAVA
     ```sh
   java -version
   ```
- MAVEN
     ```sh
   mvn -v
   ```
- GIT
   ```sh
   git --version
   ```
(NOTA: si alguna de estas aplicaciones no fue instalada, ir a la página oficial de cada una e instalar la versión recomendada).

### Instalación
1. clonar el repositorio con el siguiente comando y ingresar a la carpeta en donde esta incluido el *pom.xml*:

   ```sh
   git clone https://github.com/Waldron63/ArepTaller4modularizationWithVirtualization.git
   cd ArepTaller4modularizationWithVirtualization
   ```

2. Construir el proyecto:

   ```sh
   mvn clean package
   ```
  La consola mostrará información parecida a:
  
  <img width="1448" height="398" alt="image" src="https://github.com/user-attachments/assets/91a494c4-83e4-4fd5-bcff-0b98d4d25062" />
  
3. Correr la aplicación:

   ```sh
   java -cp target/classes com.edu.escuelaing.areptaller4modularizationwithvirtualization.httpserver.MainStart com.edu.escuelaing.areptaller4modularizationwithvirtualization.httpserver.anotations.GreetingController Starting MainStart
   ```

   La consola debería mostrar el siguiente mensaje:

   ```sh
   Listo para recibir ...
   ```

   - Página principal:
     Una vez iniciado, en el buscador ingresar: "http://localhost:35000/" o "http://localhost:35000/index.html" y lo llevará a la página inicial del proyecto:

     <img width="1919" height="961" alt="image" src="https://github.com/user-attachments/assets/5c085ab6-c79b-49d6-9c27-8ddfd415b5d4" />

   - Archivos estáticos:
     Ingresar alguno de los siguientes comandos para mirar cada archivo estático
     ```bash
       http://localhost:35000/imgage.png
       http://localhost:35000/styles.css
       http://localhost:35000/script.js
       http://localhost:35000/form.html
       http://localhost:35000/index.html
     ```
   - servicios Rest:
     Ingresar el siguiente comando para mirar el servicio Greeting:
     ```bash
       http://localhost:35000/app/greeting?name=Santiago
       http://localhost:35000/app/greeting
     ```

## Arquitectura

<img width="691" height="588" alt="image" src="https://github.com/user-attachments/assets/c3ade282-2718-4365-b697-17214010679f" />

La estructura del directorio del proyecto es:

<img width="1156" height="742" alt="image" src="https://github.com/user-attachments/assets/1ab6e434-3b3b-4e23-a482-50ae270ec43a" />

donde:

- MainStart.java: programa de ejecución base.
- HttpServer.java: programa del servidor principal.
- HttpRequest, HttpResponse, Server: programas para el correcto funcionamiento de los servicios Rest.
- Resources/*: aplicación web(html, js, css y png).
- HttpServerTest.java: pruebas test del servidor para un funcionamiento correcto.

## Concurrencia
### Rendimiento
Para poder generar el servidor, con hilos en diferentes momentos, evitando condiciones carrera, se utilzó un bloque while, que cada vez que se hace una petición al servidor, crea un nuevo hilo.

De esta forma se pueden generar diversos hilos sin que choquen entre si.

<img width="871" height="267" alt="image" src="https://github.com/user-attachments/assets/21c6be68-85c7-4ddb-9173-ec93d242aea3" />

### Apagado elegante
Se implemento un sistema, donde el apagado está mayormente controlado, el cual ayuda a evitar que procesos se bloqueen o terminen antes de tiempo, liberando todos los hilos correctamente, antes de que se apague el servidor:

<img width="835" height="273" alt="image" src="https://github.com/user-attachments/assets/84b6086d-df42-4f3d-a7ed-03299d4f49cf" />

## Despliegue de Docker y AWS
- En la raiz del proyecto, se crearon 2 archivos: DockerFile y docker-compose.yml

  <img width="1389" height="414" alt="image" src="https://github.com/user-attachments/assets/5353f0fa-6d2b-441c-8485-a9389d58f447" />

  <img width="793" height="651" alt="image" src="https://github.com/user-attachments/assets/99732f43-ecf4-4191-8d21-6e83c9a6bd6f" />

- Para construir el proyecto en docker, usamos el comando
  (NOTA: el nombre de la imagen es de esa forma, para luego subir todo a docker hub, con la cuenta de uno inicial y luego el nombre del repositorio que se genero)
  
  ```bash
       docker build --tag santiagogualdron/areptaller4modularizationwithvirtualization
     ```

  <img width="1449" height="661" alt="image" src="https://github.com/user-attachments/assets/698c4b05-89ba-4a21-b288-0d2d6b65f3e7" />

- ahora se suben los cambios a docker hub, teniendo en cuenta que el nombre de la imagen es el mismo que el nombre del repositorio, sino no podrán sincronizarse. Con el siguiente comando:
  
  ```bash
       docker push santiagogualdron/areptaller4modularizationwithvirtualization
     ```
  
  <img width="1451" height="256" alt="image" src="https://github.com/user-attachments/assets/9cfb77ad-a2f8-4696-84b6-8af78410278b" />

  - Ahora, se ejecutan 3 instancias en docker localmente, para revisar el funcionamiento del proyecto:

    ```bash
       docker run -d -p 34000:35000 --name app1 santiagogualdron/areptaller4modularizationwithvirtualization
       docker run -d -p 34001:35000 --name app2 santiagogualdron/areptaller4modularizationwithvirtualization
       docker run -d -p 34002:35000 --name app3 santiagogualdron/areptaller4modularizationwithvirtualization

      docker ps
     ```
    
    <img width="1464" height="587" alt="image" src="https://github.com/user-attachments/assets/9d3c63ed-befe-445e-a784-622db61ed6e0" />

    <img width="1198" height="154" alt="image" src="https://github.com/user-attachments/assets/d24d275b-61e5-41da-b1a9-b4b5bb608b18" />

  - Revisamos en cualquiera de los hilos su funcionamiento:

    <img width="1600" height="802" alt="image" src="https://github.com/user-attachments/assets/b1baa2f9-58a9-4b03-a357-8aca66e82d95" />

    <img width="476" height="104" alt="image" src="https://github.com/user-attachments/assets/c2bd07f9-9d3a-4b1b-bf80-75e0eb4d945e" />

  - Por ultimo, para el funcionamiento de AWS, se crea la instancia en EC2 gratis y en la terminal inicial de la máquina virtual se ingresan los siguientes comandos:

    ```bash
    sudo yum update -y
    sudo yum install docker
    sudo service docker start
    sudo usermod -a -G docker ec2-user
    docker run -d -p 42000:6000 --name firstdockerimageaws santiagogualdron/areptaller4modularizationwithvirtualization
     ```

    <img width="1600" height="522" alt="image" src="https://github.com/user-attachments/assets/e4f5250d-66ae-4f4c-82bb-764c13fdff44" />

  - Para ver el funcionamiento y el despliegue detallado en AWS, ingresar al siguiente video, que está subido a este repositorio, con el nombre "areptaller4aws.mp4"

    [Ver video de funcionamiento](./areptaller4aws.mp4)

## Reporte de pruebas

**NOTA:** Si se desean correr las pruebas unitarias, es recomentable tener el servidor apagado y el puerto 35000 libre, de lo contrario puede generar error

### fecha

Fecha: 15/09/2025

### Pruebas unitarias:

<img width="1284" height="305" alt="image" src="https://github.com/user-attachments/assets/db1c30a1-5d42-46fc-986d-817621fb75ec" />

donde cada prueba unitaria (del archivo mainTest, las pruebas de HttpServerTest no cambiaron de la anterior versión) sirve para:
- shouldLoadStaticFileHtml(): comprobar que funciona el estándar o index principal de la aplicación web.
- shouldLoadStaticFileForm(): comprueba que el servidor acepta formato html.
- shouldLoadStaticFileCss(): comprueba que el servidor acepta formato css.
- shouldLoadStaticFileJs(): comprueba que el servidor acepta formato js.
- shouldLoadStaticImagePNG(): comprueba que el servidor acepta formato png y jpg.
- shouldLoadGreetingControllerWithQuery(): Comprueba que genera la petición Http con servicio RestController de forma adecuada, obteniendo el nombre de la persona y devolviendo la respuesta.
- shouldLoadGreetingControllerWithoutQuery(): Comprueba que genera la petición Http con servicio RestController de forma adecuada, sin el nombre de la persona y devolviendo la respuesta estandar.

### Pruebas de aceptación

- index.html:
  
  <img width="1560" height="950" alt="image" src="https://github.com/user-attachments/assets/deace7f1-80e0-456c-badd-4a6c64049f51" />

- servicio rest Greeting?name=Santiago:

  <img width="591" height="103" alt="image" src="https://github.com/user-attachments/assets/e8200e40-22d3-4d46-bd4c-f8726625090a" />

- servicio rest Greeting:

  <img width="510" height="165" alt="image" src="https://github.com/user-attachments/assets/0946cd18-6810-4b02-b28f-59b65cf42223" />
  
## Construido con

[Maven](https://maven.apache.org/index.html) - Dependency Management

[Git](https://git-scm.com) - Version Control System

## Autor

Santiago Gualdron Rincon - [Waldron63](https://github.com/Waldron63)
