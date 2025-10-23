package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ContentDecoderFactoryTest {

  @Test
  void testFactoryReturnsGzipDecoder() throws UnsupportedEncodingException {
    ContentDecoder contentDecoder =
        ContentDecoderFactory.contentDecoder("gzip", "hello".getBytes(StandardCharsets.UTF_8));
    assertInstanceOf(GzipContentDecoder.class, contentDecoder);
  }

  @Test
  void testFactoryReturnsIdentityDecoder() throws UnsupportedEncodingException {
    ContentDecoder contentDecoder =
        ContentDecoderFactory.contentDecoder("identity", "hello".getBytes(StandardCharsets.UTF_8));
    assertInstanceOf(IdentityContentDecoder.class, contentDecoder);
  }

  @Test
  void testFactoryThrowsExceptionWhenEncodingNotSupported() {
    assertThrows(
        UnsupportedEncodingException.class,
        () ->
            ContentDecoderFactory.contentDecoder(
                "unknown", "hello".getBytes(StandardCharsets.UTF_8)));
  }
}
