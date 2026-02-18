package com.core.module.webshell.godzilla.cryptions.php;

import com.core.module.webshell.godzilla.annotation.CryptionAnnotation;
import com.core.module.webshell.godzilla.imp.Cryption;
import com.core.util.functions;
import java.nio.charset.StandardCharsets;

@CryptionAnnotation(Name = "PHP_XOR_RAW", payloadName = "PhpDynamicPayload")
public class PhpXorRaw implements Cryption {
  public String decodeRequest(byte[] encodeRequest, String pass, String key) {
    byte[] DecodeHex = functions.hexToByteArray(new String(encodeRequest));
    System.out.println(new String(encodeRequest));
    byte[] d = D(DecodeHex, key);
    String RequestResult = functions.formatParameter(d);
    if (RequestResult == "哥斯拉webshell解密失败！！") {
      String s = new String(d);
      return s;
    } 
    return RequestResult;
  }
  
  public String decodeResponse(byte[] encodeResponse, String pass, String key) {
    byte[] DecodeHex = functions.hexToByteArray(new String(encodeResponse));
    System.out.println(new String(encodeResponse));
    byte[] d = D(DecodeHex, key);
    byte[] bytes = functions.gzipD(d);
    String responseResult = new String(bytes, StandardCharsets.UTF_8);
    if (responseResult == "哥斯拉webshell解密失败！！") {
      String s = new String(bytes);
      return s;
    } 
    return responseResult;
  }
  
  public static byte[] D(byte[] data, String key) {
    int len = data.length;
    byte[] keyBytes = key.getBytes();
    for (int i = 0; i < len; i++)
      data[i] = (byte)(data[i] ^ keyBytes[i + 1 & 0xF]); 
    return data;
  }
}
