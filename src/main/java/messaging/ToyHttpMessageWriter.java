package messaging;

import java.io.OutputStream;

public final class ToyHttpMessageWriter {

  private final HttpLineWriter httpLineWriter;

  public ToyHttpMessageWriter(OutputStream outputStream) {
    this.httpLineWriter = new HttpLineWriter(outputStream);
  }

  public void writeMessage(ToyHttpRequest request) {
    String queryParams = "";
    if (!request.queryParams().isEmpty()) {
      queryParams = String.format("?%s", request.queryParams());
    }
    String path = "/";
    if (!request.uri().getPath().isEmpty()) {
      path = request.uri().getPath();
    }
    httpLineWriter.writeln(String.format("%s %s%s HTTP/1.1", request.method(), path, queryParams));

    for (var headerEntry : request.headers().entrySet()) {
      httpLineWriter.writeln(String.format("%s: %s", headerEntry.getKey(), headerEntry.getValue()));
    }

    httpLineWriter.writeln();
  }
}
