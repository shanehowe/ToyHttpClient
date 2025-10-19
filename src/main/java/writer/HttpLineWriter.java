package writer;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class HttpLineWriter {

  private final PrintWriter writer;

  public HttpLineWriter(OutputStream socketOutputStream) {
    this.writer = new PrintWriter(new OutputStreamWriter(socketOutputStream, StandardCharsets.UTF_8));
  }

  public void writeln() {
    writeln("");
  }

  public void writeln(String s) {
    String formattedLine = String.format("%s\r\n", normalize(s));
    writer.print(formattedLine);
    writer.flush();
  }

  private String normalize(String s) {
    return s.strip();
  }
}
