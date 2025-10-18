package transport;

import java.io.IOException;
import messaging.ToyHttpRequest;
import messaging.ToyHttpResponse;

public interface ToyHttpTransport {

  ToyHttpResponse send(ToyHttpRequest request) throws IOException;
}
