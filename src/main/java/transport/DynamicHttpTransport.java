package transport;

import java.io.IOException;
import messaging.ToyHttpRequest;
import messaging.ToyHttpResponse;

public class DynamicHttpTransport implements ToyHttpTransport {

  @Override
  public ToyHttpResponse send(ToyHttpRequest request) throws IOException {
    ToyHttpTransport delegate =
        switch (request.uri().getScheme().toLowerCase()) {
          case "https" -> new SecureHttpTransport();
          case "http" -> new PlainHttpTransport();
          default ->
              throw new IllegalArgumentException("can only send http request over http or https");
        };
    return delegate.send(request);
  }
}
