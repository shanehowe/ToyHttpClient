package writer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HttpLineWriterTest {

  private ByteArrayOutputStream outputStream;
  private HttpLineWriter writer;

  @BeforeEach
  void setUp() {
    outputStream = new ByteArrayOutputStream();
    writer = new HttpLineWriter(outputStream);
  }

  @Test
  void testWritelnWritesStringWithCRLF() {
    writer.writeln("Hello World");

    String result = outputStream.toString(StandardCharsets.UTF_8);
    assertEquals("Hello World\r\n", result);
  }

  @Test
  void testWritelnStripsWhitespace() {
    writer.writeln("   padded line   ");

    String result = outputStream.toString(StandardCharsets.UTF_8);
    assertEquals("padded line\r\n", result);
  }

  @Test
  void testWritelnEmptyStringWritesOnlyCRLF() {
    writer.writeln("");

    String result = outputStream.toString(StandardCharsets.UTF_8);
    assertEquals("\r\n", result);
  }

  @Test
  void testWritelnNoArgsWritesEmptyLineCRLF() {
    writer.writeln();

    String result = outputStream.toString(StandardCharsets.UTF_8);
    assertEquals("\r\n", result);
  }

  @Test
  void testMultipleWritesAppendSequentially() {
    writer.writeln("Line 1");
    writer.writeln("Line 2");
    writer.writeln();

    String result = outputStream.toString(StandardCharsets.UTF_8);
    assertEquals("Line 1\r\nLine 2\r\n\r\n", result);
  }

  @Test
  void testNormalizeRemovesLeadingAndTrailingWhitespace() {
    // Access indirectly via writeln
    writer.writeln("  Test  ");
    String result = outputStream.toString(StandardCharsets.UTF_8);
    assertEquals("Test\r\n", result);
  }

  @Test
  void testEncodingIsUTF8() {
    writer.writeln("✓ Unicode Test");
    String result = outputStream.toString(StandardCharsets.UTF_8);
    assertTrue(result.contains("✓"));
  }
}