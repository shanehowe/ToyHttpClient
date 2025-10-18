import java.io.IOException;
import messaging.ToyHttpRequest;
import messaging.ToyHttpResponse;
import transport.PlainHttpTransport;
import transport.ToyHttpTransport;

public record ToyHttpClient(ToyHttpTransport transport) {

  public static ToyHttpClient newDefaultClient() {
    return new ToyHttpClient(new PlainHttpTransport());
  }

  public ToyHttpResponse send(ToyHttpRequest request) throws IOException {
    return transport.send(request);
  }
}
