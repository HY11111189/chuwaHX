interface IStatusCode {
    int getCode();
    String getDescription();
}

enum OrderStatus implements IStatusCode {
    PENDING(0, "Order is pending"),
    PAID(1, "Payment received"),
    SHIPPED(2, "Order has been shipped"),
    DELIVERED(3, "Order delivered"),
    CANCELLED(-1, "Order cancelled");

    private final int code;
    private final String description;

    // Enum constructors are implicitly private
    OrderStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}

public class Question14Main {
    public static void main(String[] args) {
        // 1 & 2. Iterate through all OrderStatus values and print properties
        System.out.println("=== Iterating through OrderStatus ===");
        for (OrderStatus status : OrderStatus.values()) {
            System.out.println("Status: " + status.name() + 
                               " | Code: " + status.getCode() + 
                               " | Description: " + status.getDescription());
        }

        System.out.println("\n=== Looking up status by name ===");
        // 3. Demonstrate valueOf()
        String lookupName = "PAID";
        OrderStatus verifiedStatus = OrderStatus.valueOf(lookupName);
        System.out.println("Successfully retrieved status for '" + lookupName + "':");
        System.out.println("Code: " + verifiedStatus.getCode() + 
                           ", Description: " + verifiedStatus.getDescription());
    }
}