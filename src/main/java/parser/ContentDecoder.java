package parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public abstract class  ContentDecoder {

  protected final ByteArrayInputStream inputStream;

  public ContentDecoder(byte[] bytes) {
    this.inputStream = new ByteArrayInputStream(bytes);
  }

  public abstract String decode(Charset charset) throws IOException;
}
