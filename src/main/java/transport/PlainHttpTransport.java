package transport;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;
import messaging.ToyHttpMessageWriter;
import messaging.ToyHttpRequest;
import messaging.ToyHttpResponse;
import messaging.ToyHttpResponseParser;

public class PlainHttpTransport implements ToyHttpTransport {

  @Override
  public ToyHttpResponse send(ToyHttpRequest request) throws IOException {
    validate(request);

    int HTTP_PORT = 80;
    try (Socket socket = new Socket(InetAddress.getByName(request.uri().getHost()), HTTP_PORT)) {
      ToyHttpMessageWriter toyHttpMessageWriter = new ToyHttpMessageWriter(socket.getOutputStream());
      toyHttpMessageWriter.writeMessage(request);
      return ToyHttpResponseParser.parse(socket.getInputStream());
    }
  }

  private void validate(ToyHttpRequest request) {
    Objects.requireNonNull(request);
    if (!"http".equals(request.uri().getScheme())) {
      throw new IllegalArgumentException("Scheme must be http when using PlainHttpTransport");
    }
  }
}
