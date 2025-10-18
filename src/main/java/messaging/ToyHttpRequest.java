package messaging;

import java.net.URI;
import java.util.Objects;

/**
 * The http request that will be sent over the socket.
 *
 * <p>Note: Not building the request using <code>ToyHttpRequest.newBuilder()</code> could lead to a
 * malformatted request being sent over the socket.
 *
 * @param method
 * @param uri
 * @param headers
 */
public record ToyHttpRequest(String method, URI uri, ToyHttpQueryParams queryParams, ToyHttpHeaders headers) {

  public ToyHttpRequest {
    Objects.requireNonNull(method);
    Objects.requireNonNull(uri);
    Objects.requireNonNull(queryParams);
    Objects.requireNonNull(headers);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private final ToyHttpHeaders headers = new ToyHttpHeaders();
    private final ToyHttpQueryParams queryParams = new ToyHttpQueryParams();
    private String method;
    private URI uri;

    public Builder GET() {
      return method("GET");
    }

    public Builder method(String method) {
      this.method = method.strip();
      return this;
    }

    public Builder header(String key, String value) {
      headers.add(key, value);
      return this;
    }

    public Builder uri(URI uri) {
      this.uri = uri;
      return this;
    }

    public Builder queryParam(String key, String value) {
      queryParams.add(key, value);
      return this;
    }

    public ToyHttpRequest build() {
      // unless explicitly added - request the connection to be closed.
      if (!headers.contains("connection")) {
        header("connection", "close");
      }
      if (!headers.contains("host")) {
        header("host", uri.getHost());
      }

      if (method == null) {
        method("GET");
      }

      ToyHttpQueryParams concatenatedParams = queryParams.concat(ToyHttpQueryParams.fromURI(uri));
      return new ToyHttpRequest(method, uri, concatenatedParams, headers);
    }
  }
}
