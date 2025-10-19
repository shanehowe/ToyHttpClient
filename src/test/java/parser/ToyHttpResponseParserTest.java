package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import messaging.ToyHttpHeaders;
import messaging.ToyHttpResponse;
import org.junit.jupiter.api.Test;

class ToyHttpResponseParserTest {

  private ToyHttpResponse parseFromString(String httpMessage) throws IOException {
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(httpMessage.getBytes(StandardCharsets.UTF_8));
    return ToyHttpResponseParser.parse(inputStream);
  }

  @Test
  void testParsesBasicResponseWithBody() throws IOException {
    String response =
        """
            HTTP/1.1 200 OK\r
            Content-Type: text/plain\r
            Content-Length: 13\r
            Connection: close\r
            \r
            Hello, world!""";

    ToyHttpResponse parsed = parseFromString(response);

    assertEquals(200, parsed.statusCode());
    assertEquals("OK", parsed.statusText());
    assertEquals("text/plain", parsed.headers().get("content-type"));
    assertEquals("13", parsed.headers().get("content-length"));
    assertEquals("Hello, world!", parsed.body());
  }

  @Test
  void testParsesResponseWithNoBody() throws IOException {
    String response =
        """
            HTTP/1.1 204 No Content\r
            Content-Length: 0\r
            Connection: close\r
            \r
            """;

    ToyHttpResponse parsed = parseFromString(response);

    assertEquals(204, parsed.statusCode());
    assertEquals("No Content", parsed.statusText());
    assertEquals("0", parsed.headers().get("content-length"));
    assertEquals("", parsed.body());
  }

  @Test
  void testParses404ResponseWithHtmlBody() throws IOException {
    String response =
        """
            HTTP/1.1 404 Not Found\r
            Date: Sat, 18 Oct 2025 19:22:00 GMT\r
            Content-Type: text/html\r
            Connection: close\r
            \r
            <html><body><h1>404 Not Found</h1></body></html>""";

    ToyHttpResponse parsed = parseFromString(response);

    assertEquals(404, parsed.statusCode());
    assertEquals("Not Found", parsed.statusText());
    assertEquals("text/html", parsed.headers().get("content-type"));
    assertTrue(parsed.body().contains("404 Not Found"));
  }

  @Test
  void testParsesMultipleHeadersCorrectly() throws IOException {
    String response =
        """
            HTTP/1.1 200 OK\r
            Server: Apache\r
            Content-Type: application/json\r
            Connection: keep-alive\r
            \r
            {"message": "hi"}""";

    ToyHttpResponse parsed = parseFromString(response);

    ToyHttpHeaders headers = parsed.headers();
    assertEquals("Apache", headers.get("server"));
    assertEquals("application/json", headers.get("content-type"));
    assertEquals("keep-alive", headers.get("connection"));
    assertEquals("{\"message\": \"hi\"}", parsed.body());
  }

  @Test
  void testParsesResponseWithMultiWordStatusText() throws IOException {
    String response =
        """
            HTTP/1.1 500 Internal Server Error\r
            Content-Type: text/plain\r
            Connection: close\r
            \r
            Something broke.""";

    ToyHttpResponse parsed = parseFromString(response);

    assertEquals(500, parsed.statusCode());
    assertEquals("Internal Server Error", parsed.statusText());
    assertEquals("text/plain", parsed.headers().get("content-type"));
    assertEquals("Something broke.", parsed.body());
  }

  @Test
  void testThrowsIfMalformedStatusLine() {
    String badResponse = "HTTP/1.1 OK\r\n\r\n";
    assertThrows(IOException.class, () -> parseFromString(badResponse));
  }
}