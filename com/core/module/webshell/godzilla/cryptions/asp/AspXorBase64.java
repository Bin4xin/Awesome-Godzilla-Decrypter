package com.core.module.webshell.godzilla.cryptions.asp;

import com.core.module.webshell.godzilla.annotation.CryptionAnnotation;
import com.core.module.webshell.godzilla.imp.Cryption;
import com.core.util.functions;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@CryptionAnnotation(Name = "ASP_XOR_BASE64", payloadName = "AspDynamicPayload")
public class AspXorBase64 implements Cryption {
  public String decodeRequest(byte[] encodeRequest, String pass, String key) throws Exception {
    String decode = URLDecoder.decode(new String(encodeRequest));
    byte[] d = D(decode, key);
    String s = functions.formatParameter(d);
    return s;
  }
  
  public String decodeResponse(byte[] encodeResponse, String pass, String key) throws Exception {
    byte[] newArray = Arrays.copyOfRange(encodeResponse, 6, encodeResponse.length - 6);
    System.out.println(new String(newArray));
    byte[] d = D(new String(newArray), key);
    return new String(d, StandardCharsets.UTF_8);
  }
  
  public static byte[] D(String data, String key) throws Exception {
    byte[] cs = functions.base64Decode(data);
    int len = cs.length;
    byte[] keyBytes = key.getBytes();
    for (int i = 0; i < len; i++)
      cs[i] = (byte)(cs[i] ^ keyBytes[i + 1 & 0xF]); 
    return cs;
  }
}
