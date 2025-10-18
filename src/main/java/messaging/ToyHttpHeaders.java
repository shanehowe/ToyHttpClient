package messaging;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

public class ToyHttpHeaders {
  private final Map<String, String> headers = new LinkedHashMap<>();

  private static String normalizeKey(String key) {
    return Objects.requireNonNull(key, "Header name cannot be null").toLowerCase();
  }

  public void add(String name, String value) {
    Objects.requireNonNull(value, "Header value cannot be null");
    headers.put(normalizeKey(name), value);
  }

  public String get(String name) {
    return headers.get(normalizeKey(name));
  }

  public boolean contains(String name) {
    return headers.containsKey(normalizeKey(name));
  }

  public String remove(String name) {
    return headers.remove(normalizeKey(name));
  }

  public void forEach(BiConsumer<? super String, ? super String> action) {
    headers.forEach(action);
  }

  public Set<Map.Entry<String, String>> entrySet() {
    return Collections.unmodifiableSet(headers.entrySet());
  }

  public Map<String, String> asMap() {
    return Collections.unmodifiableMap(new LinkedHashMap<>(headers));
  }
}
