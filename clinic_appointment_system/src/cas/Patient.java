package cas;

/**
 * Patient class extending Person.
 * Represents a clinic patient with an ID and age.
 */
public class Patient extends Person {
    private String patientId;
    private int age;

    public Patient(String patientId, String name, int age) {
        super(name);
        this.patientId = patientId;
        this.age = age;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return patientId + " - " + getName() + " (Age: " + age + ")";
    }
}
