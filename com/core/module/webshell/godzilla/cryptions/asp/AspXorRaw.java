package com.core.module.webshell.godzilla.cryptions.asp;

import com.core.module.webshell.godzilla.annotation.CryptionAnnotation;
import com.core.module.webshell.godzilla.imp.Cryption;
import com.core.util.functions;
import java.nio.charset.StandardCharsets;

@CryptionAnnotation(Name = "ASP_XOR_RAW", payloadName = "AspDynamicPayload")
public class AspXorRaw implements Cryption {
  public String decodeRequest(byte[] encodeRequest, String pass, String key) throws Exception {
    byte[] encode = functions.hexToByteArray(new String(encodeRequest));
    byte[] d = D(encode, key);
    return new String(d, StandardCharsets.UTF_8);
  }
  
  public String decodeResponse(byte[] encodeResponse, String pass, String key) throws Exception {
    byte[] encode = functions.hexToByteArray(new String(encodeResponse));
    byte[] d = D(encode, key);
    return new String(d, StandardCharsets.UTF_8);
  }
  
  public static byte[] D(byte[] cs, String key) throws Exception {
    int len = cs.length;
    byte[] keyBytes = key.getBytes();
    for (int i = 0; i < len; i++)
      cs[i] = (byte)(cs[i] ^ keyBytes[i + 1 & 0xF]); 
    return cs;
  }
}
