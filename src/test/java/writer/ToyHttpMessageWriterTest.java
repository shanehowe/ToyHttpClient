package writer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import messaging.ToyHttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ToyHttpMessageWriterTest {

  private ByteArrayOutputStream outputStream;
  private ToyHttpMessageWriter toyHttpMessageWriter;

  @BeforeEach
  void setUp() {
    outputStream = new ByteArrayOutputStream();
    toyHttpMessageWriter = new ToyHttpMessageWriter(outputStream);
  }

  @ParameterizedTest
  @CsvSource(
      value = {
        "http://example.com/home,  GET /home HTTP/1.1",
        "http://example.com,  GET / HTTP/1.1",
        "http://example.com?q=1,  GET /?q=1 HTTP/1.1",
      })
  void testWriteMessageCorrectlyWritesGETHttpStatusLine(String uri, String expectedStatusLine) {
    ToyHttpRequest request = ToyHttpRequest.newBuilder().uri(URI.create(uri)).build();
    toyHttpMessageWriter.writeMessage(request);

    assertTrue(outputStream.toString(StandardCharsets.UTF_8).contains(expectedStatusLine + "\r\n"));
  }

  @Test
  void testWriteMessageCorrectlyWritesHeaders() {
    ToyHttpRequest request =
        ToyHttpRequest.newBuilder()
            .uri(URI.create("http://example.com"))
            .header("test", "header")
            .build();
    toyHttpMessageWriter.writeMessage(request);

    assertTrue(
        outputStream
            .toString(StandardCharsets.UTF_8)
            .contains("test: header\r\nconnection: close\r\nhost: example.com\r\n"));
  }
}
