class DatabaseConnection {

    // 1. Private constructor ensures no external instantiation
    private DatabaseConnection() {
        System.out.println("Database connection created");
    }

    // 2. Static inner class holds the single instance of the outer class
    private static class SingletonHelper {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }

    // 3. Public global access point
    public static DatabaseConnection getInstance() {
        // Accessing the inner class triggers its loading, which initializes the instance
        return SingletonHelper.INSTANCE;
    }

    public void executeQuery(String sql) {
        System.out.println("Executing: " + sql);
    }
}

public class Question16Main {
    public static void main(String[] args) {
        System.out.println("--- Requesting Connection 1 ---");
        DatabaseConnection conn1 = DatabaseConnection.getInstance();
        System.out.println("\n--- Requesting Connection 2 ---");
        DatabaseConnection conn2 = DatabaseConnection.getInstance();

        // 1. Verify they point to the exact same object in memory
        System.out.println("\nAre both instances identical? (conn1 == conn2): " + (conn1 == conn2));

        // 2. Execute a sample query
        System.out.println();
        conn1.executeQuery("SELECT * FROM users;");
    }
}