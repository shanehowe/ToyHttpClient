package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;
import org.junit.jupiter.api.Test;

class GzipContentDecoderTest {

  private static final int BUFFER_SIZE = 512;

  public static void gzip(InputStream is, OutputStream os) throws IOException {
    GZIPOutputStream gzipOs = new GZIPOutputStream(os);
    byte[] buffer = new byte[BUFFER_SIZE];
    int bytesRead;
    while ((bytesRead = is.read(buffer)) > -1) {
      gzipOs.write(buffer, 0, bytesRead);
    }
    gzipOs.close();
  }

  @Test
  void decode() throws IOException {
    String payload = "This is a sample text to test the gzip method. Have a nice day!";
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    gzip(new ByteArrayInputStream(payload.getBytes()), os);
    byte[] compressed = os.toByteArray();
    GzipContentDecoder gzipContentDecoder = new GzipContentDecoder(compressed);
    assertEquals(payload, gzipContentDecoder.decode(StandardCharsets.UTF_8));
  }
}
