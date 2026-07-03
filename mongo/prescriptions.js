db = db.getSiblingDB("prescriptions");

db.prescriptions.deleteMany({});

db.prescriptions.insertMany([
  {
    patientName: "Jane Doe",
    appointmentId: 1,
    medication: "Paracetamol",
    dosage: "500mg",
    doctorNotes: "Take 1 tablet every 6 hours.",
    _class: "com.project.back_end.models.Prescription"
  },
  {
    patientName: "John Smith",
    appointmentId: 2,
    medication: "Aspirin",
    dosage: "300mg",
    doctorNotes: "Take 1 tablet after meals.",
    _class: "com.project.back_end.models.Prescription"
  },
  {
    patientName: "Emily Rose",
    appointmentId: 3,
    medication: "Ibuprofen",
    dosage: "400mg",
    doctorNotes: "Take 1 tablet every 8 hours.",
    _class: "com.project.back_end.models.Prescription"
  },
  {
    patientName: "Michael Jordan",
    appointmentId: 4,
    medication: "Antihistamine",
    dosage: "10mg",
    doctorNotes: "Take 1 tablet daily before bed.",
    _class: "com.project.back_end.models.Prescription"
  },
  {
    patientName: "Olivia Moon",
    appointmentId: 5,
    medication: "Vitamin C",
    dosage: "1000mg",
    doctorNotes: "Take 1 tablet daily.",
    _class: "com.project.back_end.models.Prescription"
  }
]);
