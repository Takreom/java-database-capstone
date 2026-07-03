# Historias de Usuario - Smart Clinic

## Administrador

### Historia 1: Inicio de sesión de administrador

**Historia de Usuario**

Como administrador, quiero iniciar sesión en el portal con mi nombre de usuario y contraseña, para gestionar la plataforma de manera segura.

**Criterios de Aceptación**

1. Dado que soy un administrador registrado, cuando ingreso credenciales válidas, entonces puedo acceder al panel de administración.
2. Dado que ingreso credenciales inválidas, cuando intento iniciar sesión, entonces el sistema muestra un mensaje de error.
3. Dado que inicio sesión correctamente, cuando accedo al portal, entonces puedo ver las funciones administrativas disponibles.

**Prioridad:** Alta  
**Puntos de Historia:** 3  
**Notas:**

- El acceso administrativo debe estar protegido para evitar cambios no autorizados en el sistema.

---

### Historia 2: Cierre de sesión de administrador

**Historia de Usuario**

Como administrador, quiero cerrar sesión en el portal, para proteger el acceso al sistema cuando termine mis tareas administrativas.

**Criterios de Aceptación**

1. Dado que he iniciado sesión como administrador, cuando hago clic en cerrar sesión, entonces mi sesión finaliza correctamente.
2. Dado que he cerrado sesión, cuando intento acceder nuevamente al panel de administración, entonces el sistema me redirige a la página de inicio de sesión.
3. Dado que la sesión ha terminado, cuando otra persona usa el mismo navegador, entonces no puede acceder a funciones administrativas sin autenticarse.

**Prioridad:** Alta  
**Puntos de Historia:** 2  
**Notas:**

- Esta funcionalidad ayuda a proteger información sensible del sistema.

---

### Historia 3: Agregar doctores al portal

**Historia de Usuario**

Como administrador, quiero agregar doctores al portal, para que los pacientes puedan encontrar doctores disponibles y reservar citas.

**Criterios de Aceptación**

1. Dado que soy administrador autenticado, cuando ingreso los datos requeridos de un doctor, entonces el sistema crea el perfil del doctor.
2. Dado que falta información obligatoria, cuando intento guardar el perfil, entonces el sistema muestra mensajes de validación.
3. Dado que el doctor fue agregado correctamente, cuando los pacientes consultan la lista de doctores, entonces el nuevo doctor aparece disponible.

**Prioridad:** Alta  
**Puntos de Historia:** 5  
**Notas:**

- El perfil del doctor debe incluir información básica como nombre, especialización, correo y datos de contacto.

---

### Historia 4: Eliminar perfil de doctor

**Historia de Usuario**

Como administrador, quiero eliminar el perfil de un doctor del portal, para mantener actualizada la lista de doctores disponibles en la clínica.

**Criterios de Aceptación**

1. Dado que soy administrador autenticado, cuando selecciono un doctor existente, entonces puedo solicitar la eliminación de su perfil.
2. Dado que confirmo la eliminación, cuando el sistema procesa la solicitud, entonces el perfil del doctor deja de estar disponible en el portal.
3. Dado que el doctor tiene citas asociadas, cuando intento eliminarlo, entonces el sistema debe manejar el caso sin afectar la integridad de los datos.

**Prioridad:** Media  
**Puntos de Historia:** 5  
**Notas:**

- Puede ser necesario desactivar el perfil en lugar de eliminarlo físicamente si existen registros históricos.

---

### Historia 5: Consultar número de citas por mes

**Historia de Usuario**

Como administrador, quiero ejecutar un procedimiento almacenado en MySQL para obtener el número de citas por mes, para analizar las estadísticas de uso del sistema.

**Criterios de Aceptación**

1. Dado que existen citas registradas en la base de datos, cuando ejecuto el procedimiento almacenado, entonces obtengo el número de citas agrupadas por mes.
2. Dado que no existen citas en un mes determinado, cuando consulto las estadísticas, entonces el sistema debe manejar correctamente los resultados vacíos.
3. Dado que la consulta se ejecuta correctamente, cuando reviso los resultados, entonces puedo identificar tendencias de uso del portal.

**Prioridad:** Media  
**Puntos de Historia:** 3  
**Notas:**

- Esta historia ayuda a generar reportes administrativos sobre la actividad de la clínica.

---

## Paciente

### Historia 6: Ver lista de doctores sin iniciar sesión

**Historia de Usuario**

Como paciente, quiero ver una lista de doctores sin iniciar sesión, para explorar mis opciones antes de registrarme en el portal.

**Criterios de Aceptación**

1. Dado que no he iniciado sesión, cuando ingreso al portal, entonces puedo ver la lista de doctores disponibles.
2. Dado que consulto la lista de doctores, cuando reviso un perfil, entonces puedo ver información básica como nombre y especialización.
3. Dado que deseo reservar una cita, cuando intento continuar, entonces el sistema me solicita registrarme o iniciar sesión.

**Prioridad:** Alta  
**Puntos de Historia:** 3  
**Notas:**

- La información pública debe ser limitada y no mostrar datos sensibles.

---

### Historia 7: Registro de paciente

**Historia de Usuario**

Como paciente, quiero registrarme usando mi correo electrónico y contraseña, para poder reservar y gestionar mis citas médicas.

**Criterios de Aceptación**

1. Dado que ingreso un correo válido y una contraseña, cuando envío el formulario de registro, entonces el sistema crea mi cuenta de paciente.
2. Dado que el correo ya está registrado, cuando intento crear una nueva cuenta, entonces el sistema muestra un mensaje de error.
3. Dado que el registro es exitoso, cuando inicio sesión, entonces puedo acceder a las funciones del paciente.

**Prioridad:** Alta  
**Puntos de Historia:** 5  
**Notas:**

- La contraseña debe almacenarse de forma segura.

---

### Historia 8: Inicio de sesión de paciente

**Historia de Usuario**

Como paciente, quiero iniciar sesión en el portal, para gestionar mis reservas y acceder a mis citas médicas.

**Criterios de Aceptación**

1. Dado que soy un paciente registrado, cuando ingreso credenciales válidas, entonces puedo acceder a mi panel de paciente.
2. Dado que ingreso credenciales incorrectas, cuando intento iniciar sesión, entonces el sistema muestra un mensaje de error.
3. Dado que inicio sesión correctamente, cuando accedo al portal, entonces puedo ver mis opciones de citas.

**Prioridad:** Alta  
**Puntos de Historia:** 3  
**Notas:**

- El inicio de sesión debe validar que el usuario tenga rol de paciente.

---

### Historia 9: Cierre de sesión de paciente

**Historia de Usuario**

Como paciente, quiero cerrar sesión en el portal, para proteger mi cuenta y mi información médica.

**Criterios de Aceptación**

1. Dado que he iniciado sesión como paciente, cuando hago clic en cerrar sesión, entonces mi sesión finaliza correctamente.
2. Dado que he cerrado sesión, cuando intento acceder a mi panel, entonces el sistema me redirige al inicio de sesión.
3. Dado que mi sesión terminó, cuando otra persona usa el navegador, entonces no puede ver mis citas ni mis datos.

**Prioridad:** Alta  
**Puntos de Historia:** 2  
**Notas:**

- Esta historia protege la privacidad del paciente.

---

### Historia 10: Reservar cita de una hora

**Historia de Usuario**

Como paciente, quiero iniciar sesión y reservar una cita de una hora con un doctor, para recibir atención médica en un horario disponible.

**Criterios de Aceptación**

1. Dado que he iniciado sesión como paciente, cuando selecciono un doctor y un horario disponible, entonces puedo reservar una cita de una hora.
2. Dado que el horario seleccionado no está disponible, cuando intento reservar, entonces el sistema muestra un mensaje indicando que debo elegir otro horario.
3. Dado que la cita se reserva correctamente, cuando reviso mis próximas citas, entonces la nueva cita aparece en la lista.

**Prioridad:** Alta  
**Puntos de Historia:** 5  
**Notas:**

- El sistema debe verificar la disponibilidad del doctor antes de confirmar la cita.

---

### Historia 11: Ver próximas citas

**Historia de Usuario**

Como paciente, quiero ver mis próximas citas, para poder prepararme adecuadamente antes de cada consulta.

**Criterios de Aceptación**

1. Dado que he iniciado sesión como paciente, cuando entro a mi panel, entonces puedo ver una lista de mis próximas citas.
2. Dado que tengo citas programadas, cuando reviso la lista, entonces puedo ver la fecha, hora y doctor asignado.
3. Dado que no tengo citas próximas, cuando accedo a la sección de citas, entonces el sistema muestra un mensaje indicando que no hay citas programadas.

**Prioridad:** Media  
**Puntos de Historia:** 3  
**Notas:**

- La lista debe mostrar solo las citas asociadas al paciente autenticado.

---

## Doctor

### Historia 12: Inicio de sesión de doctor

**Historia de Usuario**

Como doctor, quiero iniciar sesión en el portal, para gestionar mis citas y consultar información relacionada con mis pacientes.

**Criterios de Aceptación**

1. Dado que soy un doctor registrado, cuando ingreso credenciales válidas, entonces puedo acceder al panel de doctor.
2. Dado que ingreso credenciales incorrectas, cuando intento iniciar sesión, entonces el sistema muestra un mensaje de error.
3. Dado que inicio sesión correctamente, cuando accedo al portal, entonces puedo ver mis citas y opciones de gestión.

**Prioridad:** Alta  
**Puntos de Historia:** 3  
**Notas:**

- El sistema debe validar que el usuario tenga rol de doctor.

---

### Historia 13: Cierre de sesión de doctor

**Historia de Usuario**

Como doctor, quiero cerrar sesión en el portal, para proteger mis datos y la información de mis pacientes.

**Criterios de Aceptación**

1. Dado que he iniciado sesión como doctor, cuando hago clic en cerrar sesión, entonces mi sesión finaliza correctamente.
2. Dado que he cerrado sesión, cuando intento volver al panel de doctor, entonces el sistema me redirige a la página de inicio de sesión.
3. Dado que la sesión terminó, cuando otra persona usa el navegador, entonces no puede acceder a información médica o de citas.

**Prioridad:** Alta  
**Puntos de Historia:** 2  
**Notas:**

- Esta función es importante para proteger datos médicos sensibles.

---

### Historia 14: Ver calendario de citas

**Historia de Usuario**

Como doctor, quiero ver mi calendario de citas, para mantenerme organizado y conocer mi agenda de atención.

**Criterios de Aceptación**

1. Dado que he iniciado sesión como doctor, cuando accedo a mi calendario, entonces puedo ver mis citas programadas.
2. Dado que tengo citas asignadas, cuando reviso el calendario, entonces puedo ver fecha, hora y paciente correspondiente.
3. Dado que no tengo citas programadas, cuando consulto el calendario, entonces el sistema muestra que no hay citas disponibles.

**Prioridad:** Alta  
**Puntos de Historia:** 5  
**Notas:**

- El calendario debe mostrar únicamente las citas del doctor autenticado.

---

### Historia 15: Marcar indisponibilidad

**Historia de Usuario**

Como doctor, quiero marcar mi indisponibilidad, para que los pacientes solo puedan reservar citas en horarios disponibles.

**Criterios de Aceptación**

1. Dado que he iniciado sesión como doctor, cuando selecciono un horario como no disponible, entonces el sistema bloquea ese horario para nuevas reservas.
2. Dado que un paciente intenta reservar durante un horario bloqueado, cuando consulta la disponibilidad, entonces ese horario no aparece como opción.
3. Dado que actualizo mi disponibilidad, cuando reviso mi calendario, entonces los cambios se reflejan correctamente.

**Prioridad:** Alta  
**Puntos de Historia:** 5  
**Notas:**

- Esta función evita conflictos de agenda y reservas duplicadas.

---

### Historia 16: Actualizar perfil de doctor

**Historia de Usuario**

Como doctor, quiero actualizar mi perfil con mi especialización e información de contacto, para que los pacientes tengan información actualizada antes de reservar una cita.

**Criterios de Aceptación**

1. Dado que he iniciado sesión como doctor, cuando edito mi perfil, entonces puedo actualizar mi especialización e información de contacto.
2. Dado que guardo los cambios correctamente, cuando los pacientes ven mi perfil, entonces se muestra la información actualizada.
3. Dado que ingreso información inválida o incompleta, cuando intento guardar, entonces el sistema muestra mensajes de validación.

**Prioridad:** Media  
**Puntos de Historia:** 3  
**Notas:**

- La información del perfil ayuda a los pacientes a elegir el doctor adecuado.

---

### Historia 17: Ver detalles del paciente

**Historia de Usuario**

Como doctor, quiero ver los detalles del paciente para mis próximas citas, para prepararme adecuadamente antes de cada consulta.

**Criterios de Aceptación**

1. Dado que he iniciado sesión como doctor, cuando selecciono una cita próxima, entonces puedo ver los detalles básicos del paciente.
2. Dado que la cita pertenece a otro doctor, cuando intento acceder a sus datos, entonces el sistema no permite el acceso.
3. Dado que consulto los detalles del paciente, cuando reviso la información, entonces puedo prepararme para la consulta.

**Prioridad:** Alta  
**Puntos de Historia:** 5  
**Notas:**

- El acceso a información del paciente debe estar restringido según permisos y rol.