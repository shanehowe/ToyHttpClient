package parser;

import java.io.UnsupportedEncodingException;

public class ContentDecoderFactory {

  public static ContentDecoder contentDecoder(String encoding, byte[] bytesToDecode)
      throws UnsupportedEncodingException {
    return switch (encoding.toLowerCase()) {
      case "identity" -> new IdentityContentDecoder(bytesToDecode);
      case "gzip" -> new GzipContentDecoder(bytesToDecode);
      default -> throw new UnsupportedEncodingException("can only decode gzip & identity");
    };
  }
}
