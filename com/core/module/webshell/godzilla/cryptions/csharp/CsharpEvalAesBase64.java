package com.core.module.webshell.godzilla.cryptions.csharp;

import com.core.module.webshell.godzilla.annotation.CryptionAnnotation;
import com.core.module.webshell.godzilla.imp.Cryption;
import com.core.util.functions;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@CryptionAnnotation(Name = "CSHARP_EVAL_AES_BASE64", payloadName = "CSharpDynamicPayload")
public class CsharpEvalAesBase64 implements Cryption {
  public String decodeRequest(byte[] encodeRequest, String pass, String key) throws Exception {
    String decode = URLDecoder.decode(new String(encodeRequest));
    byte[] data = functions.base64Decode(decode);
    data = x(data, false, key);
    String requestResult = functions.formatParameter(data);
    return requestResult;
  }
  
  public String decodeResponse(byte[] encodeResponse, String pass, String key) throws Exception {
    byte[] newArray = Arrays.copyOfRange(encodeResponse, 16, encodeResponse.length - 16);
    byte[] data = functions.base64Decode(new String(newArray, StandardCharsets.UTF_8));
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
