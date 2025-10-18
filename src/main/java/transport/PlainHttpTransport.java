package transport;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import messaging.HttpLineWriter;
import messaging.ToyHttpRequest;
import messaging.ToyHttpResponse;
import messaging.ToyHttpResponseParser;

public class PlainHttpTransport implements ToyHttpTransport {

  @Override
  public ToyHttpResponse send(ToyHttpRequest request) throws IOException {
    validate(request);

    int HTTP_PORT = 80;
    try (Socket socket = new Socket(InetAddress.getByName(request.uri().getHost()), HTTP_PORT)) {
      HttpLineWriter writer = new HttpLineWriter(socket.getOutputStream());

      String queryParams = "";
      if (!request.queryParams().isEmpty()) {
        queryParams = String.format("?%s", request.queryParams());
      }
      String path = "/";
      if (!request.uri().getPath().isEmpty()) {
        path = request.uri().getPath();
      }
      writer.writeln(
          String.format(
              "%s %s%s HTTP/1.1", request.method(), path, queryParams));

      for (var headerEntry : request.headers().entrySet()) {
        writer.writeln(String.format("%s: %s", headerEntry.getKey(), headerEntry.getValue()));
      }

      writer.writeln();

      return ToyHttpResponseParser.parse(socket.getInputStream());
    }
  }

  private void validate(ToyHttpRequest request) {
    if (!"http".equals(request.uri().getScheme())) {
      throw new IllegalArgumentException("Scheme must be http when using PlainHttpTransport");
    }
  }
}
