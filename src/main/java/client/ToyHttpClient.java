package client;

import java.io.IOException;
import messaging.ToyHttpRequest;
import messaging.ToyHttpResponse;
import transport.DynamicHttpTransport;
import transport.PlainHttpTransport;
import transport.SecureHttpTransport;
import transport.ToyHttpTransport;

public record ToyHttpClient(ToyHttpTransport transport) {

  public static ToyHttpClient newDefaultClient() {
    PlainHttpTransport plainHttpTransport = new PlainHttpTransport();
    SecureHttpTransport secureHttpTransport = new SecureHttpTransport();
    return new ToyHttpClient(new DynamicHttpTransport(secureHttpTransport, plainHttpTransport));
  }

  public ToyHttpResponse send(ToyHttpRequest request) throws IOException {
    return transport.send(request);
  }
}
