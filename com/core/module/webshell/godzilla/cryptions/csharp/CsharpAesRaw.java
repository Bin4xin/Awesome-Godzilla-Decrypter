package com.core.module.webshell.godzilla.cryptions.csharp;

import com.core.module.webshell.godzilla.annotation.CryptionAnnotation;
import com.core.module.webshell.godzilla.imp.Cryption;
import com.core.util.functions;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@CryptionAnnotation(Name = "CSHARP_AES_RAW", payloadName = "CSharpDynamicPayload")
public class CsharpAesRaw implements Cryption {
  public String decodeRequest(byte[] encodeRequest, String pass, String key) throws Exception {
    byte[] data = functions.hexToByteArray(new String(encodeRequest));
    data = x(data, false, key);
    String requestResult = functions.formatParameter(data);
    return requestResult;
  }
  
  public String decodeResponse(byte[] encodeResponse, String pass, String key) throws Exception {
    byte[] data = functions.hexToByteArray(new String(encodeResponse));
    data = x(data, false, key);
    String responseResult = new String(functions.gzipD(data));
    return responseResult;
  }
  
  public static byte[] x(byte[] s, boolean m, String secretKeyX) {
    try {
      Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
      String xc = secretKeyX;
      c.init(m ? 1 : 2, new SecretKeySpec(xc.getBytes(), "AES"), new IvParameterSpec(xc.getBytes()));
      return c.doFinal(s);
    } catch (Exception e) {
      return null;
    } 
  }
}
