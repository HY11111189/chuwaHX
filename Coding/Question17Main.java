import java.util.ArrayList;
import java.util.List;

// 1. Professor Class (Aggregated Component)
class Professor {
    private String name;
    private String specialization;

    public Professor(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Professor " + name + " (Specialization: " + specialization + ")";
    }
}

// 2. Department Class (Composed Component)
class Department {
    private String name;
    private String building;

    public Department(String name, String building) {
        this.name = name;
        this.building = building;
    }

    @Override
    public String toString() {
        return name + " Department [Building: " + building + "]";
    }
}

// 3. University Class
class University {
    private String name;
    // Composition: University directly controls creation/lifecycle of Departments
    private List<Department> departments;
    // Aggregation: University holds references to independently existing Professors
    private List<Professor> professors;

    public University(String name) {
        this.name = name;
        this.departments = new ArrayList<>();
        this.professors = new ArrayList<>();

        // Requirement: Create 3 departments internally (Composition)
        departments.add(new Department("Computer Science", "Ryerson Hall"));
        departments.add(new Department("Data Science", "John Crerar Library"));
        departments.add(new Department("Mathematics", "Eckhart Hall"));
    }

    public void addProfessor(Professor p) {
        professors.add(p);
    }

    public void listProfessors() {
        System.out.println("Faculty List for " + name + ":");
        for (Professor p : professors) {
            System.out.println(" - " + p);
        }
    }

    public void displayStructure() {
        System.out.println("=== " + name + " Structure ===");
        System.out.println("Internal Departments:");
        for (Department d : departments) {
            System.out.println(" * " + d);
        }
    }
}

// 4. Main Execution Runner
public class Question17Main {
    public static void main(String[] args) {
        // Step 1: Create 2 Professor objects independently
        Professor prof1 = new Professor("Dr. Alan Turing", "Theoretical Computer Science");
        Professor prof2 = new Professor("Dr. Grace Hopper", "Compilers and Systems");

        // Step 2: Create a University
        University uChicago = new University("University of Chicago");
        uChicago.displayStructure();
        System.out.println();

        // Step 3: Add both professors to the university (Aggregation)
        uChicago.addProfessor(prof1);
        uChicago.addProfessor(prof2);
        uChicago.listProfessors();

        System.out.println("\n--- Simulating University Destruction ---");
        // Step 4: Destroy the university container object
        uChicago = null; 
        // At this stage, the Department objects are unreachable and scheduled for Garbage Collection.
        // However, because prof1 and prof2 were created independently in the main method, they survive.
        System.out.println("The university object has been set to null.");
        System.out.println("Verifying professor life status independent of the institution:");
        System.out.println("Still active: " + prof1);
        System.out.println("Still active: " + prof2);
    }
}