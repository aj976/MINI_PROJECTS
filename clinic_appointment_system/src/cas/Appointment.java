package cas;

/**
 * Appointment class representing a booked clinic appointment.
 * Stores doctorId, patientName, and time slot.
 */
public class Appointment {
    private String doctorId;
    private String patientName;
    private String slot;

    public Appointment(String doctorId, String patientName, String slot) {
        this.doctorId = doctorId;
        this.patientName = patientName;
        this.slot = slot;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getSlot() {
        return slot;
    }

    /**
     * Returns a CSV-formatted line for file persistence.
     * Format: doctorId,patientName,slot
     */
    public String toCsv() {
        return doctorId + "," + patientName + "," + slot;
    }

    /**
     * Parses a CSV line back into an Appointment object.
     */
    public static Appointment fromCsv(String csvLine) {
        String[] parts = csvLine.split(",", 3);
        if (parts.length == 3) {
            return new Appointment(parts[0].trim(), parts[1].trim(), parts[2].trim());
        }
        return null;
    }

    @Override
    public String toString() {
        return "Doctor: " + doctorId + " | Patient: " + patientName + " | Slot: " + slot;
    }
}
