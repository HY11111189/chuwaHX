import java.util.Objects;

public class Product {
    private String id;
    private String name;
    private double price;

    // Constructor taking all three parameters
    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Override toString to match the required custom pattern
    @Override
    public String toString() {
        return "Product{id='" + id + "', name='" + name + "', price=" + price + "}";
    }

    // Override equals to evaluate business logic matching only by ID
    @Override
    public boolean equals(Object obj) {
        // 1. Optimization check for identical reference pointers
        if (this == obj) {
            return true;
        }
        // 2. Structural safety checks for null values and matching type classes
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        // 3. Cast the object to verify structural equivalence on fields
        Product product = (Product) obj;
        return Objects.equals(id, product.id);
    }

    // Override hashCode consistent with equals (hashing only the id field)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Main Testing Method
    public static void main(String[] args) {
        // Create two Product objects with matching IDs but different names/prices
        Product prod1 = new Product("P001", "Laptop", 999.99);
        Product prod2 = new Product("P001", "Gaming Laptop", 1499.99);

        // Verify logical equivalence matching via overridden equals method
        boolean areEqual = prod1.equals(prod2);
        System.out.println("Are the two products equal? " + areEqual);

        // Verify hash uniformity across distinct memory instances
        int hash1 = prod1.hashCode();
        int hash2 = prod2.hashCode();
        System.out.println("Product 1 HashCode: " + hash1);
        System.out.println("Product 2 HashCode: " + hash2);
        System.out.println("Do hash codes match? " + (hash1 == hash2));

        System.out.println();
        
        // Print out both items utilizing the custom toString format
        System.out.println(prod1.toString());
        System.out.println(prod2.toString());
    }
}