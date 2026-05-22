import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureHomework2 {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        CompletableFuture<String> products = fetch(client, "https://jsonplaceholder.typicode.com/posts/1");
        CompletableFuture<String> reviews = fetch(client, "https://jsonplaceholder.typicode.com/posts/2");
        CompletableFuture<String> inventory = fetch(client, "https://jsonplaceholder.typicode.com/posts/3");

        // Merge all three
        CompletableFuture.allOf(products, reviews, inventory)
            .thenRun(() -> {
                System.out.println("Combined Data:");
                System.out.println("Products: " + products.join());
                System.out.println("Reviews: " + reviews.join());
                System.out.println("Inventory: " + inventory.join());
            }).join();
    }

    private static CompletableFuture<String> fetch(HttpClient client, String url) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                     .thenApply(HttpResponse::body);
    }
}