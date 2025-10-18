import java.io.IOException;
import java.net.URI;
import messaging.ToyHttpRequest;
import messaging.ToyHttpResponse;

public class Main {

  public static void main(String[] args) throws IOException {
    ToyHttpRequest toyHttpRequest =
        ToyHttpRequest.newBuilder()
            .GET()
            .uri(URI.create("http://echo.free.beeceptor.com"))
            .header("X-Requested-by", "someone")
            .queryParam("page", "1")
            .build();

    ToyHttpClient client = ToyHttpClient.newDefaultClient();
    ToyHttpResponse response = client.send(toyHttpRequest);

    System.out.printf("%s %s%n", response.statusCode(), response.statusText());
    response.headers().forEach((k, v) -> System.out.printf("%s: %s%n", k,v));
    System.out.println(response.body());
  }
}
