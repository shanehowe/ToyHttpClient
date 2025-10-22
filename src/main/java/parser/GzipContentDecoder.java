package parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

public class GzipContentDecoder extends ContentDecoder {

  public GzipContentDecoder(byte[] bytes) {
    super(bytes);
  }

  @Override
  public String decode(Charset charset) throws IOException {
    try (GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)) {
      return new String(gzipInputStream.readAllBytes(), charset);
    }
  }
}
