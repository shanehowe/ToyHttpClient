package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import messaging.ToyHttpHeaders;
import messaging.ToyHttpResponse;
import org.jetbrains.annotations.NotNull;

public class ToyHttpResponseParser {

  private final BufferedReader reader;

  public ToyHttpResponseParser(InputStream inputStream) {
    this.reader = new BufferedReader(new InputStreamReader(inputStream));
  }

  public static ToyHttpResponse parse(InputStream socketInputStream) throws IOException {
    return new ToyHttpResponseParser(socketInputStream).parse();
  }

  public ToyHttpResponse parse() throws IOException {
    String[] statusLineArray = reader.readLine().split(" ");
    int statusCode = Integer.parseInt(statusLineArray[1]);
    String statusText = statusLineArray[2];

    ToyHttpHeaders headers = parseHeaders();
    String body = parseBody();
    return new ToyHttpResponse(statusCode, statusText, headers, body);
  }

  private @NotNull String parseBody() throws IOException {
    StringBuilder bodyBuilder = new StringBuilder();
    String nextLine;
    while ((nextLine = reader.readLine()) != null) {
      bodyBuilder.append(nextLine);
    }
    return bodyBuilder.toString();
  }

  private ToyHttpHeaders parseHeaders() throws IOException {
    ToyHttpHeaders headers = new ToyHttpHeaders();
    String nextLine;
    while (!(nextLine = reader.readLine()).isBlank()) {
      String[] splitHeader = nextLine.split(": ");
      headers.add(splitHeader[0], splitHeader[1]);
    }
    return headers;
  }
}
