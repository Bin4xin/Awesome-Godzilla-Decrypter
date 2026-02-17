package com.core.module.webshell.godzilla.cryptions.java;

import com.core.module.webshell.godzilla.annotation.CryptionAnnotation;
import com.core.module.webshell.godzilla.imp.Cryption;
import com.core.util.functions;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@CryptionAnnotation(Name = "JAVA_AES_BASE64", payloadName = "JavaDynamicPayload")
public class JavaAesBase64 implements Cryption {
  public String decodeRequest(byte[] encodeRequest, String pass, String key) throws Exception {
    String decode = URLDecoder.decode(new String(encodeRequest), StandardCharsets.UTF_8.toString());
    byte[] data = functions.base64Decode(new String(decode));
    data = x(data, false, key);
    return functions.formatParameter(data);
  }
  
  public String decodeResponse(byte[] encodeResponse, String pass, String key) throws Exception {
    String raw = functions.findStr(encodeResponse, pass, key);
    byte[] data = functions.base64Decode(raw);
    data = x(data, false, key);
    return new String(functions.gzipD(data), StandardCharsets.UTF_8);
  }
  
  public static byte[] x(byte[] s, boolean m, String secretKeyX) {
    try {
      Cipher c = Cipher.getInstance("AES");
      String xc = secretKeyX;
      c.init(m ? 1 : 2, new SecretKeySpec(xc.getBytes(), "AES"));
      return c.doFinal(s);
    } catch (Exception e) {
      System.out.println(e);
      return null;
    } 
  }
}
