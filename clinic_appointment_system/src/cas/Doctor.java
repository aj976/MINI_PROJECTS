package cas;

/**
 * Doctor class extending Person.
 * Represents a clinic doctor with an ID and specialization.
 */
public class Doctor extends Person {
    private String doctorId;
    private String specialization;

    public Doctor(String doctorId, String name, String specialization) {
        super(name);
        this.doctorId = doctorId;
        this.specialization = specialization;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return doctorId + " - Dr. " + getName() + " (" + specialization + ")";
    }
}
