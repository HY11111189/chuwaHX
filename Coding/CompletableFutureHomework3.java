import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureHomework3 {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        CompletableFuture<String> productsFuture =
                fetchApiWithDefault("https://jsonplaceholder.typicode.com/posts/1",
                        "Default product data");

        CompletableFuture<String> reviewsFuture =
                fetchApiWithDefault("https://jsonplaceholder.typicode.com/comments/1",
                        "Default review data");

        CompletableFuture<String> inventoryFuture =
                fetchApiWithDefault("https://wrong-url.typicode.com/todos/1",
                        "Default inventory data");

        CompletableFuture<String> mergedFuture =
                productsFuture.thenCombine(reviewsFuture, (products, reviews) ->
                        "Products:\n" + products + "\n\nReviews:\n" + reviews
                ).thenCombine(inventoryFuture, (partialResult, inventory) ->
                        partialResult + "\n\nInventory:\n" + inventory
                );

        System.out.println(mergedFuture.join());
    }

    private static CompletableFuture<String> fetchApiWithDefault(String url, String defaultValue) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .exceptionally(ex -> {
                    System.out.println("API call failed: " + url);
                    System.out.println("Reason: " + ex.getMessage());
                    return defaultValue;
                });
    }
}
