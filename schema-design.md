# Diseño de Esquema de Base de Datos - Smart Clinic Management System

## MySQL Database Design

El Sistema de Gestión de Clínica Inteligente utiliza MySQL para almacenar los datos estructurados y relacionales del sistema. Estos datos incluyen pacientes, doctores, administradores, citas y disponibilidad médica. MySQL es adecuado para esta información porque permite definir relaciones claras mediante claves primarias y foráneas, aplicar restricciones de integridad y mantener consistencia en operaciones críticas como la reserva de citas.

---

### Table: patients

| Column | Data Type | Constraints | Description |
|---|---|---|---|
| patient_id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Identificador único del paciente |
| first_name | VARCHAR(100) | NOT NULL | Nombre del paciente |
| last_name | VARCHAR(100) | NOT NULL | Apellido del paciente |
| email | VARCHAR(150) | NOT NULL, UNIQUE | Correo electrónico usado para registro e inicio de sesión |
| password_hash | VARCHAR(255) | NOT NULL | Contraseña almacenada de forma segura |
| phone | VARCHAR(30) | NULL | Número de teléfono del paciente |
| date_of_birth | DATE | NULL | Fecha de nacimiento del paciente |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Fecha de creación del registro |
| updated_at | TIMESTAMP | NULL | Fecha de última actualización |

**Relaciones y reglas:**

- Un paciente puede tener muchas citas.
- El correo electrónico debe ser único para evitar cuentas duplicadas.
- Si un paciente tiene citas históricas, no se recomienda eliminarlo físicamente; es mejor conservar el historial médico.

---

### Table: doctors

| Column | Data Type | Constraints | Description |
|---|---|---|---|
| doctor_id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Identificador único del doctor |
| first_name | VARCHAR(100) | NOT NULL | Nombre del doctor |
| last_name | VARCHAR(100) | NOT NULL | Apellido del doctor |
| email | VARCHAR(150) | NOT NULL, UNIQUE | Correo electrónico usado para inicio de sesión |
| password_hash | VARCHAR(255) | NOT NULL | Contraseña almacenada de forma segura |
| specialization | VARCHAR(150) | NOT NULL | Especialidad médica del doctor |
| phone | VARCHAR(30) | NULL | Número de contacto |
| active | BOOLEAN | NOT NULL, DEFAULT TRUE | Indica si el doctor está activo en el portal |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Fecha de creación del registro |
| updated_at | TIMESTAMP | NULL | Fecha de última actualización |

**Relaciones y reglas:**

- Un doctor puede tener muchas citas.
- Un doctor puede tener múltiples bloques de disponibilidad o indisponibilidad.
- Si un doctor tiene citas asociadas, es preferible desactivarlo usando `active = false` en lugar de eliminarlo físicamente.

---

### Table: admins

| Column | Data Type | Constraints | Description |
|---|---|---|---|
| admin_id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Identificador único del administrador |
| first_name | VARCHAR(100) | NOT NULL | Nombre del administrador |
| last_name | VARCHAR(100) | NOT NULL | Apellido del administrador |
| email | VARCHAR(150) | NOT NULL, UNIQUE | Correo electrónico usado para inicio de sesión |
| password_hash | VARCHAR(255) | NOT NULL | Contraseña almacenada de forma segura |
| role | VARCHAR(50) | NOT NULL, DEFAULT 'ADMIN' | Rol administrativo dentro del sistema |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Fecha de creación del registro |

**Relaciones y reglas:**

- Los administradores gestionan doctores, revisan estadísticas y administran la plataforma.
- El correo debe ser único para proteger la identidad de cada cuenta administrativa.

---

### Table: appointments

| Column | Data Type | Constraints | Description |
|---|---|---|---|
| appointment_id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Identificador único de la cita |
| patient_id | BIGINT | NOT NULL, FOREIGN KEY references patients(patient_id) | Paciente que reserva la cita |
| doctor_id | BIGINT | NOT NULL, FOREIGN KEY references doctors(doctor_id) | Doctor asignado a la cita |
| appointment_date | DATE | NOT NULL | Fecha de la cita |
| start_time | TIME | NOT NULL | Hora de inicio de la cita |
| end_time | TIME | NOT NULL | Hora de finalización de la cita |
| status | VARCHAR(30) | NOT NULL, DEFAULT 'SCHEDULED' | Estado de la cita |
| reason | VARCHAR(255) | NULL | Motivo de la consulta |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Fecha de creación de la cita |
| updated_at | TIMESTAMP | NULL | Fecha de última actualización |

**Relaciones y reglas:**

- Cada cita pertenece a un paciente.
- Cada cita pertenece a un doctor.
- Un paciente puede tener muchas citas.
- Un doctor puede tener muchas citas.
- El sistema debe evitar citas superpuestas para el mismo doctor.
- Los estados posibles pueden ser `SCHEDULED`, `COMPLETED`, `CANCELLED` o `NO_SHOW`.

**Restricción recomendada:**

```sql
UNIQUE (doctor_id, appointment_date, start_time)
Esta restricción ayuda a evitar que un doctor tenga dos citas en la misma fecha y hora de inicio.
Table: doctor_availability
Column	Data Type	Constraints	Description
availability_id	BIGINT	PRIMARY KEY, AUTO_INCREMENT	Identificador único del bloque de disponibilidad
doctor_id	BIGINT	NOT NULL, FOREIGN KEY references doctors(doctor_id)	Doctor asociado al bloque
available_date	DATE	NOT NULL	Fecha del bloque
start_time	TIME	NOT NULL	Hora de inicio del bloque
end_time	TIME	NOT NULL	Hora de finalización del bloque
status	VARCHAR(30)	NOT NULL	Indica si el doctor está disponible o no disponible
notes	VARCHAR(255)	NULL	Comentarios opcionales
created_at	TIMESTAMP	NOT NULL, DEFAULT CURRENT_TIMESTAMP	Fecha de creación del registro
Relaciones y reglas:
- Un doctor puede tener muchos bloques de disponibilidad.
- Esta tabla permite que el doctor marque horarios disponibles o no disponibles.
- El sistema debe consultar esta tabla antes de permitir que un paciente reserve una cita.
- Los valores sugeridos para status son AVAILABLE y UNAVAILABLE.
MySQL Design Justification
MySQL se utiliza para los datos principales del sistema porque estos requieren estructura, consistencia y relaciones claras. Pacientes, doctores, administradores y citas son entidades con campos definidos y relaciones importantes. Usar claves primarias y foráneas permite mantener la integridad de los datos y evitar registros inconsistentes, como citas sin paciente o sin doctor.
La tabla appointments conecta pacientes y doctores, representando una relación de muchos a muchos a través de citas individuales. La tabla doctor_availability complementa este diseño al permitir controlar los horarios disponibles antes de confirmar una reserva.
MongoDB Collection Design
MongoDB se utiliza para datos flexibles basados en documentos. En este diseño, la colección principal será prescriptions, porque las recetas médicas pueden variar según el paciente, el doctor, los medicamentos, las instrucciones y las notas clínicas. Este tipo de información puede incluir arreglos, campos opcionales y estructuras anidadas, por lo que MongoDB es una buena elección.
Collection: prescriptions
La colección prescriptions almacena recetas médicas asociadas a una cita, un paciente y un doctor. En lugar de guardar objetos completos de paciente o doctor, el documento almacena sus identificadores para mantener la relación con los datos principales en MySQL.
{
  "_id": "presc_1001",
  "appointmentId": 501,
  "patientId": 101,
  "doctorId": 201,
  "issuedAt": "2026-07-02T10:30:00Z",
  "diagnosis": {
    "code": "J00",
    "description": "Resfriado común"
  },
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500 mg",
      "frequency": "Cada 8 horas",
      "duration": "5 días",
      "instructions": "Tomar después de las comidas"
    },
    {
      "name": "Solución salina nasal",
      "dosage": "2 aplicaciones",
      "frequency": "Cada 6 horas",
      "duration": "3 días",
      "instructions": "Aplicar en cada fosa nasal"
    }
  ],
  "doctorNotes": "El paciente debe mantenerse hidratado y regresar si presenta fiebre persistente.",
  "followUp": {
    "required": true,
    "recommendedDate": "2026-07-09",
    "notes": "Revisión si los síntomas no mejoran"
  },
  "tags": ["respiratory", "common-cold", "follow-up"],
  "metadata": {
    "createdBy": "doctor",
    "version": 1,
    "source": "Smart Clinic Portal"
  }
}
MongoDB Design Justification
La colección prescriptions se almacena en MongoDB porque una receta puede tener una estructura variable. Algunas recetas pueden contener uno o varios medicamentos, instrucciones especiales, notas del doctor, seguimiento recomendado o metadatos adicionales.
MongoDB permite guardar esta información como documentos flexibles sin modificar constantemente un esquema relacional. Además, usar appointmentId, patientId y doctorId permite relacionar la receta con los datos principales almacenados en MySQL, sin duplicar información completa del paciente o del doctor.
Summary
Este diseño utiliza un enfoque híbrido:
- MySQL almacena datos estructurados y relacionales como pacientes, doctores, administradores, disponibilidad y citas.
- MongoDB almacena datos flexibles basados en documentos como recetas médicas.
- Las relaciones principales se mantienen mediante identificadores.
- El diseño facilita la futura implementación de modelos JPA para MySQL y documentos MongoDB para recetas.
```

https://github.com/Takreom/java-database-capstone/blob/main/schema-design.md