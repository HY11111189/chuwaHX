import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// 1. Product Class
class Product {
    private final String id;
    private final String name;
    private final BigDecimal price;
    private final String category;
    private final boolean available;

    public Product(String id, String name, BigDecimal price, String category, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return available; }

    @Override
    public String toString() {
        return name + " (" + category + ": " + OrderProcessor.formatPrice(price) + ")";
    }
}

// 2. Order Class
class Order {
    private final String orderId;
    private final LocalDateTime orderDate;
    private final List<Product> items;
    private final String customerEmail;

    public Order(String orderId, LocalDateTime orderDate, List<Product> items, String customerEmail) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.items = items;
        this.customerEmail = customerEmail;
    }

    public String getOrderId() { return orderId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public List<Product> getItems() { return items; }
    public String getCustomerEmail() { return customerEmail; }

    @Override
    public String toString() {
        return "Order #" + orderId + " by " + customerEmail + " | Items: " + items.size();
    }
}

// 3. OrderProcessor Interface
interface OrderProcessor {
    
    default BigDecimal calculateTotal(Order order) {
        if (order == null || order.getItems() == null) {
            return BigDecimal.ZERO;
        }
        return order.getItems().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    static String formatPrice(BigDecimal price) {
        if (price == null) price = BigDecimal.ZERO;
        return NumberFormat.getCurrencyInstance(Locale.US).format(price);
    }

    void processOrder(Order order);
}

// 4. OrderService Class
class OrderService implements OrderProcessor {

    @Override
    public void processOrder(Order order) {
        System.out.println("Processing order: " + order.getOrderId() + 
                           " | Total: " + OrderProcessor.formatPrice(calculateTotal(order)));
    }

    public List<Order> filterOrders(List<Order> orders, Predicate<Order> condition) {
        return orders.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public Map<String, List<Order>> groupOrdersByCategory(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getItems() != null && !order.getItems().isEmpty())
                .collect(Collectors.groupingBy(order -> order.getItems().get(0).getCategory()));
    }

    public Optional<Order> findMostExpensiveOrder(List<Order> orders) {
        return orders.stream()
                .max(Comparator.comparing(this::calculateTotal));
    }
}

// 5. Main Execution Method
public class OrderSystemMain {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();

        // Sample Data: Products
        Product laptop = new Product("P1", "Laptop", new BigDecimal("1200.00"), "Electronics", true);
        Product mouse = new Product("P2", "Wireless Mouse", new BigDecimal("25.50"), "Electronics", true);
        Product javaBook = new Product("P3", "Effective Java", new BigDecimal("45.00"), "Books", true);
        Product cookbook = new Product("P4", "Mastering the Grill", new BigDecimal("30.00"), "Books", true);
        Product deskChair = new Product("P5", "Ergonomic Chair", new BigDecimal("250.00"), "Furniture", true);

        // Sample Data: Orders
        Order order1 = new Order("O1", LocalDateTime.now(), Arrays.asList(laptop, mouse), "alice@example.com");
        Order order2 = new Order("O2", LocalDateTime.now(), Arrays.asList(javaBook, cookbook), "bob@example.com");
        Order order3 = new Order("O3", LocalDateTime.now(), Collections.singletonList(mouse), "charlie@example.com");
        Order order4 = new Order("O4", LocalDateTime.now(), Collections.singletonList(deskChair), "dan@example.com");

        List<Order> allOrders = Arrays.asList(order1, order2, order3, order4);

        System.out.println("=== 1. Initial Order Statuses ===");
        allOrders.forEach(orderService::processOrder);

        // Filter: Total > $100 using a Lambda Expression
        System.out.println("\n=== 2. Filtering Orders Total > $100 ===");
        List<Order> expensiveOrders = orderService.filterOrders(allOrders, 
                order -> orderService.calculateTotal(order).compareTo(new BigDecimal("100.00")) > 0);
        
        expensiveOrders.forEach(o -> System.out.println(o + " (Total: " + OrderProcessor.formatPrice(orderService.calculateTotal(o)) + ")"));

        // Grouping: Group orders by the category of their first product
        System.out.println("\n=== 3. Grouping Orders By Category ===");
        Map<String, List<Order>> ordersByCategory = orderService.groupOrdersByCategory(allOrders);
        ordersByCategory.forEach((category, orders) -> {
            System.out.println("Category [" + category + "]:");
            orders.forEach(o -> System.out.println("   - " + o.getOrderId()));
        });

        // Optional Handling: Find the highest total order
        System.out.println("\n=== 4. Most Expensive Order ===");
        Optional<Order> mostExpensive = orderService.findMostExpensiveOrder(allOrders);
        
        mostExpensive.ifPresent(order -> System.out.println("Highest value transaction is " + order.getOrderId() + 
                " totaling " + OrderProcessor.formatPrice(orderService.calculateTotal(order))));
    }
}