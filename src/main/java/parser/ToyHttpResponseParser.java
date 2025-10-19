package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import messaging.ToyHttpHeaders;
import messaging.ToyHttpResponse;
import org.jetbrains.annotations.NotNull;

public class ToyHttpResponseParser {

  private static final int VALID_STATUS_LINE_ARRAY_LENGTH = 3;
  private final BufferedReader reader;

  public ToyHttpResponseParser(InputStream inputStream) {
    this.reader = new BufferedReader(new InputStreamReader(inputStream));
  }

  public static ToyHttpResponse parse(InputStream socketInputStream) throws IOException {
    return new ToyHttpResponseParser(socketInputStream).parse();
  }

  private static void validateStatusLineArrayLength(String[] statusLineArray) throws IOException {
    if (statusLineArray.length != VALID_STATUS_LINE_ARRAY_LENGTH) {
      String message =
          String.format("Malformatted HTTP status line: %s", Arrays.toString(statusLineArray));
      throw new IOException(message);
    }
  }

  public ToyHttpResponse parse() throws IOException {
    String[] statusLineArray = reader.readLine().split(" ", 3);
    validateStatusLineArrayLength(statusLineArray);

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
