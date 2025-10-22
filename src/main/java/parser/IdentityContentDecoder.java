package parser;

import java.nio.charset.Charset;

public class IdentityContentDecoder extends ContentDecoder {

  public IdentityContentDecoder(byte[] bytes) {
    super(bytes);
  }

  @Override
  public String decode(Charset charset) {
    return new String(inputStream.readAllBytes(), charset);
  }
}
