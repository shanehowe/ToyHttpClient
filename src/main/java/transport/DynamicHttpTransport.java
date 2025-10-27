package transport;

import java.io.IOException;
import messaging.ToyHttpRequest;
import messaging.ToyHttpResponse;

public class DynamicHttpTransport implements ToyHttpTransport {

  private final ToyHttpTransport secureTransport;
  private final ToyHttpTransport plainTransport;

  public DynamicHttpTransport(ToyHttpTransport secureTransport, ToyHttpTransport plainTransport) {
    this.secureTransport = secureTransport;
    this.plainTransport = plainTransport;
  }

  @Override
  public ToyHttpResponse send(ToyHttpRequest request) throws IOException {
    return switch (request.uri().getScheme().toLowerCase()) {
      case "https" -> secureTransport.send(request);
      case "http" -> plainTransport.send(request);
      default ->
          throw new IllegalArgumentException("can only send http request over http or https");
    };
  }
}
