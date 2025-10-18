package messaging;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import org.junit.jupiter.api.Test;

class ToyHttpRequestTest {

  @Test
  void testBuildAddsDefaultHeadersWhenNotProvided() {
    ToyHttpRequest request =
        ToyHttpRequest.newBuilder()
            .method("GET")
            .uri(URI.create("http://example.com/test"))
            .build();

    assertEquals("GET", request.method());
    assertEquals("example.com", request.headers().get("host"));
    assertEquals("close", request.headers().get("connection"));
  }

  @Test
  void testBuildDoesNotOverrideExplicitHeaders() {
    ToyHttpRequest request =
        ToyHttpRequest.newBuilder()
            .method("GET")
            .uri(URI.create("http://example.com/test"))
            .header("connection", "keep-alive")
            .header("host", "customhost.com")
            .build();

    assertEquals("keep-alive", request.headers().get("connection"));
    assertEquals("customhost.com", request.headers().get("host"));
  }

  @Test
  void testGetShortcutSetsMethodGET() {
    ToyHttpRequest request =
        ToyHttpRequest.newBuilder().GET().uri(URI.create("http://example.com")).build();

    assertEquals("GET", request.method());
  }

  @Test
  void testQueryParamsFromUriAndAddedAreConcatenated() {
    ToyHttpRequest request =
        ToyHttpRequest.newBuilder()
            .method("GET")
            .uri(URI.create("http://example.com?foo=bar"))
            .queryParam("hello", "world")
            .build();

    String queryString = request.queryParams().toString();
    assertTrue(queryString.contains("foo=bar"));
    assertTrue(queryString.contains("hello=world"));
  }

  @Test
  void testHostHeaderUsesUriHost() {
    ToyHttpRequest request =
        ToyHttpRequest.newBuilder()
            .method("GET")
            .uri(URI.create("http://myserver.local/path"))
            .build();

    assertEquals("myserver.local", request.headers().get("host"));
  }

  @Test
  void testBuildThrowsWhenUriNotProvided() {
    var builder = ToyHttpRequest.newBuilder().method("GET");
    assertThrows(NullPointerException.class, builder::build);
  }

  @Test
  void testBuildThrowsWhenMethodNotProvided() {
    var builder = ToyHttpRequest.newBuilder().uri(URI.create("http://example.com"));
    assertThrows(NullPointerException.class, builder::build);
  }

  @Test
  void testHeadersAndQueryParamsAreImmutableAfterBuild() {
    ToyHttpRequest request =
        ToyHttpRequest.newBuilder().method("GET").uri(URI.create("http://example.com")).build();

    assertThrows(
        UnsupportedOperationException.class, () -> request.headers().asMap().put("new", "value"));
  }

  @Test
  void testWhenNoMethodAddedDefaultsToGET() {
    ToyHttpRequest request =
        ToyHttpRequest.newBuilder().uri(URI.create("http://example.com")).build();
    assertEquals("GET", request.method());
  }
}
