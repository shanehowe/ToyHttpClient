package messaging;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ToyHttpQueryParams {

  private final Map<String, String> queryParams;

  private ToyHttpQueryParams(Map<String, String> queryParams) {
    this.queryParams = new LinkedHashMap<>(queryParams);
  }

  public ToyHttpQueryParams() {
    this(new LinkedHashMap<>());
  }

  public static ToyHttpQueryParams fromURI(URI uri) {
    Objects.requireNonNull(uri, "URI cannot be null");

    ToyHttpQueryParams toyHttpQueryParams = new ToyHttpQueryParams();
    if (uri.getQuery() == null) {
      return toyHttpQueryParams;
    }
    Arrays.stream(uri.getQuery().split("&"))
        .forEach(
            s -> {
              String[] splitQueryParam = s.split("=", 2);
              String decodedKey = URLDecoder.decode(splitQueryParam[0], StandardCharsets.UTF_8);
              if (splitQueryParam.length == 2) {
                String decodedValue = URLDecoder.decode(splitQueryParam[1], StandardCharsets.UTF_8);
                toyHttpQueryParams.add(decodedKey, decodedValue);
              } else {
                 toyHttpQueryParams.add(decodedKey, null);
              }
            });
    return toyHttpQueryParams;
  }

  /**
   * Adds a query param in the format of key=value.
   *
   * <p>If value is null or empty the query parameter will just have a key and no value
   *
   * @param key The key of the query parameter
   * @param value The value to of the query parameter
   * @return {@link ToyHttpQueryParams}
   */
  public ToyHttpQueryParams add(String key, String value) {
    Objects.requireNonNull(key, "Key cannot be null");
    queryParams.put(key, value);
    return this;
  }

  public boolean isEmpty() {
    return queryParams.isEmpty();
  }

  public ToyHttpQueryParams concat(ToyHttpQueryParams other) {
    Objects.requireNonNull(other, "other cannot be null");
    Map<String, String> mergedQueryParams = new LinkedHashMap<>();
    mergedQueryParams.putAll(queryParams);
    mergedQueryParams.putAll(other.queryParams);
    return new ToyHttpQueryParams(mergedQueryParams);
  }

  public Map<String, String> asMap() {
    return Collections.unmodifiableMap(queryParams);
  }

  private String encode(String key, String value) {
    String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8);
    if (null == value) {
      return encodedKey;
    }
    if (value.isBlank()) {
      return String.format("%s=", encodedKey);
    }
    String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
    return String.format("%s=%s", encodedKey, encodedValue);
  }

  @Override
  public String toString() {
    return queryParams.entrySet().stream()
        .map(entry -> encode(entry.getKey(), entry.getValue()))
        .collect(Collectors.joining("&"));
  }
}
