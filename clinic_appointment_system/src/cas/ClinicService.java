package cas;

import java.io.*;
import java.util.*;

/**
 * ClinicService acts as the central backend for all clinic operations.
 *
 * Responsibilities:
 *  - Manages patient/doctor collections (ArrayList)
 *  - Handles appointment booking (HashMap + Pair)
 *  - Provides thread-safe booking via synchronized method
 *  - Handles file persistence (load on start, save on changes)
 */
public class ClinicService {

    // ──────────────────────── Collections ────────────────────────
    private final ArrayList<Patient> patients = new ArrayList<>();
    private final ArrayList<Doctor>  doctors  = new ArrayList<>();

    /**
     * Appointment mapping:
     *   key   = "doctorId_slot"  (uniquely identifies a slot for a doctor)
     *   value = Pair<doctorId, patientName>
     */
    private final HashMap<String, Pair<String, String>> appointments = new HashMap<>();

    // Derived list kept in sync for easy display
    private final ArrayList<Appointment> appointmentList = new ArrayList<>();

    // ─────────────────── ID auto-increment counters ───────────────
    private int patientCounter = 1;
    private int doctorCounter  = 1;

    // ──────────────────────── File paths ─────────────────────────
    private static final String PATIENTS_FILE     = "patients.txt";
    private static final String DOCTORS_FILE      = "doctors.txt";
    private static final String APPOINTMENTS_FILE = "appointments.txt";

    // ─────────────────────── Constructor ─────────────────────────
    public ClinicService() {
        loadFromFiles();
    }

    // ════════════════════════════════════════════════════════════
    //  PATIENT MANAGEMENT
    // ════════════════════════════════════════════════════════════

    /** Adds a patient and persists the patients list. */
    public synchronized void addPatient(String name, int age) {
        String id = "P" + String.format("%03d", patientCounter++);
        patients.add(new Patient(id, name, age));
        savePatients();
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    // ════════════════════════════════════════════════════════════
    //  DOCTOR MANAGEMENT
    // ════════════════════════════════════════════════════════════

    /** Adds a doctor and persists the doctors list. */
    public synchronized void addDoctor(String name, String specialization) {
        String id = "D" + String.format("%03d", doctorCounter++);
        doctors.add(new Doctor(id, name, specialization));
        saveDoctors();
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    // ════════════════════════════════════════════════════════════
    //  APPOINTMENT BOOKING  (thread-safe)
    // ════════════════════════════════════════════════════════════

    /**
     * Books an appointment.
     *
     * @param patient the selected patient
     * @param doctor  the selected doctor
     * @param slot    the time slot string
     * @return true if booked successfully, false if slot already taken
     */
    public synchronized boolean bookAppointment(Patient patient, Doctor doctor, String slot) {
        String key = doctor.getDoctorId() + "_" + slot;

        if (appointments.containsKey(key)) {
            // Slot already booked for this doctor
            return false;
        }

        Pair<String, String> pair = new Pair<>(doctor.getDoctorId(), patient.getName());
        appointments.put(key, pair);

        Appointment appt = new Appointment(doctor.getDoctorId(), patient.getName(), slot);
        appointmentList.add(appt);
        saveAppointments();
        return true;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointmentList;
    }

    // ════════════════════════════════════════════════════════════
    //  FILE PERSISTENCE
    // ════════════════════════════════════════════════════════════

    private void savePatients() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PATIENTS_FILE))) {
            for (Patient p : patients) {
                pw.println(p.getPatientId() + "," + p.getName() + "," + p.getAge());
            }
        } catch (IOException e) {
            System.err.println("Error saving patients: " + e.getMessage());
        }
    }

    private void saveDoctors() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DOCTORS_FILE))) {
            for (Doctor d : doctors) {
                pw.println(d.getDoctorId() + "," + d.getName() + "," + d.getSpecialization());
            }
        } catch (IOException e) {
            System.err.println("Error saving doctors: " + e.getMessage());
        }
    }

    private synchronized void saveAppointments() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(APPOINTMENTS_FILE))) {
            for (Appointment a : appointmentList) {
                pw.println(a.toCsv());
            }
        } catch (IOException e) {
            System.err.println("Error saving appointments: " + e.getMessage());
        }
    }

    /** Loads all data from text files on application startup. */
    private void loadFromFiles() {
        loadPatients();
        loadDoctors();
        loadAppointments();
    }

    private void loadPatients() {
        File f = new File(PATIENTS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    try {
                        String id   = parts[0].trim();
                        String name = parts[1].trim();
                        int    age  = Integer.parseInt(parts[2].trim());
                        patients.add(new Patient(id, name, age));
                        // Sync counter
                        int num = Integer.parseInt(id.substring(1));
                        if (num >= patientCounter) patientCounter = num + 1;
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading patients: " + e.getMessage());
        }
    }

    private void loadDoctors() {
        File f = new File(DOCTORS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    String id   = parts[0].trim();
                    String name = parts[1].trim();
                    String spec = parts[2].trim();
                    doctors.add(new Doctor(id, name, spec));
                    try {
                        int num = Integer.parseInt(id.substring(1));
                        if (num >= doctorCounter) doctorCounter = num + 1;
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading doctors: " + e.getMessage());
        }
    }

    private void loadAppointments() {
        File f = new File(APPOINTMENTS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                Appointment a = Appointment.fromCsv(line);
                if (a != null) {
                    String key = a.getDoctorId() + "_" + a.getSlot();
                    appointments.put(key, new Pair<>(a.getDoctorId(), a.getPatientName()));
                    appointmentList.add(a);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading appointments: " + e.getMessage());
        }
    }
}
