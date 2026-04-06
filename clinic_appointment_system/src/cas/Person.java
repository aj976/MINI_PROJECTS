package cas;

/**
 * Base class representing a Person in the Clinic Appointment System.
 * Demonstrates OOP inheritance principle.
 */
public abstract class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "'}";
    }
}
