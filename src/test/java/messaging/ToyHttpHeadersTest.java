package messaging;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ToyHttpHeadersTest {

  private ToyHttpHeaders headers;

  @BeforeEach
  void setUp() {
    headers = new ToyHttpHeaders();
  }

  @Test
  void testAddAndGetStoresHeaderCaseInsensitive() {
    headers.add("Content-Type", "application/json");
    assertEquals("application/json", headers.get("content-type"));
    assertEquals("application/json", headers.get("CONTENT-TYPE"));
    assertEquals("application/json", headers.get("Content-Type"));
  }

  @Test
  void testAddReplacesExistingValue() {
    headers.add("Host", "example.com");
    headers.add("host", "example.org");
    assertEquals("example.org", headers.get("HOST"));
    assertEquals(1, headers.asMap().size());
  }

  @Test
  void testContainsReturnsTrueForExistingKey() {
    headers.add("User-Agent", "ToyHttpClient");
    assertTrue(headers.contains("user-agent"));
    assertTrue(headers.contains("USER-AGENT"));
  }

  @Test
  void testContainsReturnsFalseForMissingKey() {
    assertFalse(headers.contains("Accept"));
  }

  @Test
  void testRemoveDeletesHeaderAndReturnsValue() {
    headers.add("Connection", "keep-alive");
    String removedValue = headers.remove("connection");
    assertEquals("keep-alive", removedValue);
    assertFalse(headers.contains("Connection"));
  }

  @Test
  void testForEachIteratesOverAllEntries() {
    headers.add("A", "1");
    headers.add("B", "2");

    StringBuilder sb = new StringBuilder();
    headers.forEach((k, v) -> sb.append(k).append("=").append(v).append(";"));

    assertTrue(sb.toString().contains("a=1"));
    assertTrue(sb.toString().contains("b=2"));
  }

  @Test
  void testEntrySetIsUnmodifiable() {
    headers.add("X-Test", "value");
    Set<Map.Entry<String, String>> entrySet = headers.entrySet();
    assertThrows(UnsupportedOperationException.class, entrySet::clear);
  }

  @Test
  void testAsMapReturnsUnmodifiableCopy() {
    headers.add("Key", "value");
    Map<String, String> map = headers.asMap();
    assertThrows(UnsupportedOperationException.class, () -> map.put("another", "x"));
    assertEquals("value", map.get("key"));
  }

  @Test
  void testNormalizeKeyThrowsWhenNullKey() {
    assertThrows(NullPointerException.class, () -> headers.add(null, "x"));
  }

  @Test
  void testAddThrowsWhenValueNull() {
    assertThrows(NullPointerException.class, () -> headers.add("key", null));
  }

  @Test
  void testRemoveNonExistentKeyReturnsNull() {
    assertNull(headers.remove("non-existent"));
  }
}