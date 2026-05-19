import java.util.*;
import java.util.stream.Collectors;

// 1. Student Class
class Student {
    private final String id;
    private final String name;
    private final int age;
    private final String major;
    private final List<Double> scores;

    public Student(String id, String name, int age, String major, List<Double> scores) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.scores = scores;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getMajor() { return major; }
    public List<Double> getScores() { return scores; }

    // Custom helper method to safely evaluate individual stream calculations
    public double getAverageScore() {
        if (scores == null || scores.isEmpty()) 
            return 0.0;

        return scores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public String toString() {
        return name + " (" + major + ") Avg: " + String.format("%.2f", getAverageScore());
    }
}

// 2. StudentAnalyzer Class
class StudentAnalyzer {

    public List<String> getTopStudentNames(List<Student> students, int n) {
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::getAverageScore).reversed())
                .limit(n)
                .map(Student::getName)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getAverageScoreByMajor(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::getMajor,
                        Collectors.averagingDouble(Student::getAverageScore)
                ));
    }

    public Optional<Student> findStudentWithHighestSingleScore(List<Student> students) {
        return students.stream()
                .filter(s -> s.getScores() != null && !s.getScores().isEmpty())
                .max(Comparator.comparingDouble(s -> s.getScores().stream()
                        .mapToDouble(Double::doubleValue)
                        .max()
                        .orElse(0.0)));
    }

    public List<Student> getStudentsAboveAverageInMajor(List<Student> students, String major) {
        // Step 1: Pre-calculate the comprehensive baseline average for the target major department
        double majorAverage = students.stream()
                .filter(s -> s.getMajor().equalsIgnoreCase(major))
                .mapToDouble(Student::getAverageScore)
                .average()
                .orElse(0.0);

        // Step 2: Extract students inside that major segment out-performing the segment average
        return students.stream()
                .filter(s -> s.getMajor().equalsIgnoreCase(major))
                .filter(s -> s.getAverageScore() > majorAverage)
                .collect(Collectors.toList());
    }

    public Map<Boolean, List<Student>> partitionByPassFail(List<Student> students, double passingScore) {
        return students.stream()
                .collect(Collectors.partitioningBy(s -> s.getAverageScore() >= passingScore));
    }
}

// 3. Main Execution Method
public class StudentAnalysisMain {
    public static void main(String[] args) {
        StudentAnalyzer analyzer = new StudentAnalyzer();

        // Initializing 6 sample student profiles with varying scores across 2 majors
        List<Student> classroom = Arrays.asList(
                new Student("S1", "Zachary", 22, "Computer Science", Arrays.asList(95.0, 88.0, 91.5)),
                new Student("S2", "Yvonne", 21, "Computer Science", Arrays.asList(70.0, 65.0, 78.0)),
                new Student("S3", "Xavier", 23, "Computer Science", Arrays.asList(88.0, 94.0, 100.0)), // Has highest single score: 100
                new Student("S4", "Walter", 22, "Data Science", Arrays.asList(82.5, 85.0, 89.0)),
                new Student("S5", "Victoria", 24, "Data Science", Arrays.asList(92.0, 96.0, 94.5)),
                new Student("S6", "Ulysses", 21, "Data Science", Arrays.asList(55.0, 60.0, 48.0))
        );

        // Method 1 Execution: Get top 3 names
        System.out.println("=== 1. Top 3 Students Overall (by Average) ===");
        List<String> topStudents = analyzer.getTopStudentNames(classroom, 3);
        topStudents.forEach(System.out::println);

        // Method 2 Execution: Get average scores by major
        System.out.println("\n=== 2. Aggregated Average Score By Major ===");
        Map<String, Double> majorAverages = analyzer.getAverageScoreByMajor(classroom);
        majorAverages.forEach((major, avg) -> 
                System.out.println(major + " Base Average: " + String.format("%.2f", avg)));

        // Method 3 Execution: Find student with highest single score (Handling Optional via ifPresent)
        System.out.println("\n=== 3. Highest Single Value Milestone Record ===");
        Optional<Student> apexScorer = analyzer.findStudentWithHighestSingleScore(classroom);
        apexScorer.ifPresent(student -> System.out.println("Student with highest individual test score: " + student.getName()));

        // Method 4 Execution: Get students above average in Computer Science
        System.out.println("\n=== 4. High Performers in Computer Science (Outperforming Major Average) ===");
        List<Student> eliteCS = analyzer.getStudentsAboveAverageInMajor(classroom, "Computer Science");
        eliteCS.forEach(System.out::println);

        // Method 5 Execution: Partition by passing score threshold of 75.0
        System.out.println("\n=== 5. Partitioning Results (Passing Cutoff Threshold: 75.0) ===");
        Map<Boolean, List<Student>> gradeReport = analyzer.partitionByPassFail(classroom, 75.0);
        
        System.out.println("Passed Cohort:");
        gradeReport.get(true).forEach(s -> System.out.println(" + " + s));
        
        System.out.println("Failed Cohort:");
        gradeReport.get(false).forEach(s -> System.out.println(" - " + s));
        
        // Demonstrating structural safety variations of Optional handling (orElseThrow vs orElse)
        System.out.println("\n=== 6. Optional Fallback Demonstration ===");
        List<Student> emptyClassroom = new ArrayList<>();
        Student fallbackStudent = analyzer.findStudentWithHighestSingleScore(emptyClassroom)
                .orElse(new Student("DEFAULT", "Unknown Student", 0, "N/A", Collections.emptyList()));
        System.out.println("Safe handling output for empty query sequence: " + fallbackStudent.getName());
    }
}