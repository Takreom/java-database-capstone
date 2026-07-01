# Diseño de Arquitectura del Sistema de Gestión de Clínica Inteligente

## 1. Resumen de la Arquitectura

El Sistema de Gestión de Clínica Inteligente utiliza una arquitectura de tres capas: 
capa de presentación, capa de aplicación y capa de datos. 
La capa de presentación incluye paneles web creados con Thymeleaf y clientes que consumen APIs REST. 
La capa de aplicación está construida con Spring Boot e incluye controladores, servicios, lógica de negocio y repositorios. 
La capa de datos utiliza MySQL para información estructurada y relacional, como pacientes, doctores, citas, usuarios, roles y administradores, mientras que MongoDB almacena información flexible basada en documentos, como las prescripciones médicas. Esta separación permite que el sistema sea más escalable, mantenible y fácil de desplegar.

## 2. Flujo Numerado de Solicitud y Respuesta

1. Un usuario interactúa con la aplicación mediante un panel web basado en Thymeleaf o mediante un cliente externo que consume una API REST.
2. La solicitud se envía al backend y se dirige al controlador correspondiente según la URL y el método HTTP utilizado.
3. Los controladores Thymeleaf gestionan las solicitudes de páginas web renderizadas en el servidor y devuelven plantillas HTML con datos dinámicos.
4. Los controladores REST gestionan las solicitudes de API y devuelven respuestas en formato JSON.
5. Los controladores delegan la lógica de negocio a la capa de servicios.
6. La capa de servicios aplica reglas de negocio, validaciones y coordina procesos del sistema, como verificar la disponibilidad de un doctor antes de programar una cita.
7. La capa de servicios se comunica con la capa de repositorios para consultar, guardar, actualizar o eliminar datos.
8. Los repositorios de Spring Data JPA se comunican con MySQL para manejar datos relacionales como pacientes, doctores, citas, usuarios, roles y administradores.
9. Los repositorios de Spring Data MongoDB se comunican con MongoDB para manejar datos basados en documentos, como las prescripciones médicas.
10. Los datos recuperados desde las bases de datos se convierten en clases modelo de Java.
11. Los datos de MySQL se representan mediante entidades JPA anotadas con `@Entity`.
12. Los datos de MongoDB se representan mediante documentos anotados con `@Document`.
13. En los flujos MVC, los modelos se pasan desde el controlador hacia las plantillas Thymeleaf para generar páginas HTML dinámicas.
14. En los flujos REST, los modelos o DTOs se serializan en formato JSON y se devuelven al cliente.
15. El ciclo de solicitud y respuesta finaliza entregando una página web completa o datos estructurados en formato JSON.