package messaging;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ToyHttpQueryParamsTest {

  private ToyHttpQueryParams queryParams;

  @BeforeEach
  void setUp() {
    queryParams = new ToyHttpQueryParams();
  }

  @Test
  void testWhenNoQueryParamsIsEmptyReturnsTrue() {
    assertTrue(queryParams.isEmpty());
  }

  @Test
  void testWhenAddingQueryParamsIsEmptyReturnsFalse() {
    queryParams.add("hello", "world");
    assertFalse(queryParams.isEmpty());
  }

  @Test
  void testAddingQueryParamInsertsIntoMap() {
    queryParams.add("hello", "world");
    Map<String, String> map = queryParams.asMap();
    assertEquals("world", map.get("hello"));
  }

  @ParameterizedTest
  @CsvSource(
      value = {
        "hello,world,hello=world",
        "q,test spacing,q=test+spacing",
        "q,1=1,q=1%3D1",
        "q,null,q",
        "q,' ',q="
      },
      nullValues = {"null"})
  void testToStringReturnsCorrectlyEncodedQueryString(String key, String value, String expected) {
    String queryString = queryParams.add(key, value).toString();
    assertEquals(expected, queryString);
  }

  @Test
  void testToStringReturnsCorrectQueryStringWhenMultipleAdded() {
    String queryString =
        queryParams.add("hello", "world").add("q", "1 1").add("a", null).add("b", "").toString();

    assertEquals("hello=world&q=1+1&a&b=", queryString);
  }

  @Test
  void testFromURIReturnsCorrectQueryString() {
    ToyHttpQueryParams fromURI =
        ToyHttpQueryParams.fromURI(URI.create("http://example.com?hello=world&q=1+1&a&b="));

    assertEquals("hello=world&q=1+1&a&b=", fromURI.toString());
  }

  @Test
  void testFromURIThrowsNullPointerExceptionWhenURIIsNull() {
    assertThrows(NullPointerException.class, () -> ToyHttpQueryParams.fromURI(null));
  }

  @Test
  void testConcatThrowsNullPointerExceptionWhenOtherIsNull() {
    assertThrows(NullPointerException.class, () -> queryParams.concat(null  ));
  }

  @Test
  void testConcatMergesTwoNonEmptyInstances() {
    ToyHttpQueryParams first = new ToyHttpQueryParams().add("a", "1").add("b", "2");
    ToyHttpQueryParams second = new ToyHttpQueryParams().add("c", "3").add("d", "4");

    ToyHttpQueryParams result = first.concat(second);

    Map<String, String> map = result.asMap();
    assertEquals(4, map.size());
    assertEquals("1", map.get("a"));
    assertEquals("2", map.get("b"));
    assertEquals("3", map.get("c"));
    assertEquals("4", map.get("d"));
  }

  @Test
  void testConcatWithEmptyInstanceReturnsSameParameters() {
    ToyHttpQueryParams first = new ToyHttpQueryParams().add("a", "1").add("b", "2");
    ToyHttpQueryParams empty = new ToyHttpQueryParams();

    ToyHttpQueryParams result1 = first.concat(empty);
    ToyHttpQueryParams result2 = empty.concat(first);

    assertEquals(first.asMap(), result1.asMap());
    assertEquals(first.asMap(), result2.asMap());
  }

  @Test
  void testConcatOverwritesValuesForOverlappingKeys() {
    ToyHttpQueryParams first = new ToyHttpQueryParams().add("a", "1").add("b", "2");
    ToyHttpQueryParams second = new ToyHttpQueryParams().add("b", "20").add("c", "3");

    ToyHttpQueryParams result = first.concat(second);

    Map<String, String> map = result.asMap();
    assertEquals(3, map.size());
    assertEquals("1", map.get("a"));
    assertEquals("20", map.get("b"));
    assertEquals("3", map.get("c"));
  }
}
