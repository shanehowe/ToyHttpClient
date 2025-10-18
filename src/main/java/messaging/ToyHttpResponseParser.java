package messaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ToyHttpResponseParser {

  public static ToyHttpResponse parse(InputStream socketInputStream) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(socketInputStream));

    String[] statusLineArray = reader.readLine().split(" ");
    int statusCode = Integer.parseInt(statusLineArray[1]);
    String statusText = statusLineArray[2];

    ToyHttpHeaders headers = new ToyHttpHeaders();
    StringBuilder bodyBuilder = new StringBuilder();
    boolean encounteredNewLine = false;
    String nextLine;

    while ((nextLine = reader.readLine()) != null) {
      if (nextLine.isBlank()) {
        encounteredNewLine = true;
        continue;
      }

      if (encounteredNewLine) {
        bodyBuilder.append(nextLine);
      } else {
        String[] splitHeader = nextLine.split(": ");
        headers.add(splitHeader[0], splitHeader[1]);
      }
    }
    return new ToyHttpResponse(statusCode, statusText, headers, bodyBuilder.toString());
  }
}
