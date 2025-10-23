package transport;

import java.io.IOException;
import java.net.InetAddress;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import messaging.ToyHttpRequest;
import messaging.ToyHttpResponse;
import parser.ToyHttpResponseParser;
import writer.ToyHttpMessageWriter;

public class SecureHttpTransport implements ToyHttpTransport {

  private static final int HTTPS_PORT = 443;

  @Override
  public ToyHttpResponse send(ToyHttpRequest request) throws IOException {
    validate(request);
    SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    try (SSLSocket socket =
        (SSLSocket)
            sslSocketFactory.createSocket(
                InetAddress.getByName(request.uri().getHost()), HTTPS_PORT)) {
      socket.startHandshake();
      ToyHttpMessageWriter toyHttpMessageWriter =
          new ToyHttpMessageWriter(socket.getOutputStream());
      toyHttpMessageWriter.writeMessage(request);
      return ToyHttpResponseParser.parse(socket.getInputStream());
    }
  }

  private void validate(ToyHttpRequest request) {
    if (!"https".equalsIgnoreCase(request.uri().getScheme())) {
      throw new IllegalArgumentException("Scheme must be http when using SecureHttpTransport");
    }
  }
}
