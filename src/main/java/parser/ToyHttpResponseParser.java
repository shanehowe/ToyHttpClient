package parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import messaging.ToyHttpHeaders;
import messaging.ToyHttpResponse;
import org.jetbrains.annotations.NotNull;

public class ToyHttpResponseParser {

  private static final int VALID_STATUS_LINE_ARRAY_LENGTH = 3;
  private final InputStream inputStream;

  public ToyHttpResponseParser(InputStream inputStream) {
    this.inputStream = inputStream;
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
    String[] statusLineArray = readStatusLine().split(" ", 3);
    validateStatusLineArrayLength(statusLineArray);

    int statusCode = Integer.parseInt(statusLineArray[1]);
    String statusText = statusLineArray[2];

    ToyHttpHeaders headers = parseHeaders();
    String body = parseBody(headers);
    return new ToyHttpResponse(statusCode, statusText, headers, body);
  }

  private @NotNull String parseBody(ToyHttpHeaders headers) throws IOException {
    if ("chunked".equalsIgnoreCase(headers.get("transfer-encoding"))) {
      return parseChunkedBody();
    }
    ByteArrayOutputStream buff = new ByteArrayOutputStream();
    buff.write(inputStream.readAllBytes());
    return buff.toString(StandardCharsets.UTF_8);
  }

  private String parseChunkedBody() throws IOException {
    ByteArrayOutputStream bodyBytes = new ByteArrayOutputStream();
    while (true) {
      String hexLine = readAsciiLine().trim();
      int chunkSize = Integer.parseInt(hexLine, 16);
      if (chunkSize == 0) {
        consumeCRLF();
        break;
      }
      byte[] chunk = inputStream.readNBytes(chunkSize);
      bodyBytes.write(chunk);
      consumeCRLF();
    }
    return bodyBytes.toString(StandardCharsets.UTF_8);
  }

  private void consumeCRLF() throws IOException {
    byte[] crlf = inputStream.readNBytes(2);
    if (!"\r\n".equals(new String(crlf))) {
      throw new UnsupportedOperationException("consumeCRLF called on non CRLF bytes");
    }
  }

  private ToyHttpHeaders parseHeaders() throws IOException {
    ToyHttpHeaders headers = new ToyHttpHeaders();
    String line;
    while (!(line = readAsciiLine().trim()).isEmpty()) {
      String[] splitHeaderLine = line.split(": ", 2);
      headers.add(splitHeaderLine[0], splitHeaderLine[1]);
    }
    return headers;
  }

  private String readStatusLine() throws IOException {
    return readAsciiLine();
  }

  private @NotNull String readAsciiLine() throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int previous = -1, current;
    while ((current = inputStream.read()) != -1) {
      if (previous == '\r' && current == '\n') {
        break;
      }
      buffer.write(current);
      previous = current;
    }
    return buffer.toString(StandardCharsets.US_ASCII).trim();
  }
}
