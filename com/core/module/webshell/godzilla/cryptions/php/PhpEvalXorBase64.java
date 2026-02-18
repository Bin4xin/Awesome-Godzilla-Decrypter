package com.core.module.webshell.godzilla.cryptions.php;

import com.core.module.webshell.godzilla.annotation.CryptionAnnotation;
import com.core.module.webshell.godzilla.imp.Cryption;
import com.core.util.functions;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@CryptionAnnotation(Name = "PHP_EVAL_XOR_BASE64", payloadName = "PhpDynamicPayload")
public class PhpEvalXorBase64 implements Cryption {
  public String decodeRequest(byte[] encodeRequest, String pass, String key) throws Exception {
    String decode = URLDecoder.decode(new String(encodeRequest), StandardCharsets.UTF_8.toString());
    byte[] data = D(new String(decode), key);
    String requestResult = functions.formatParameter(data);
    if (requestResult == "哥斯拉webshell解密失败！！") {
      String s = new String(data);
      return s;
    } 
    return requestResult;
  }
  
  public String decodeResponse(byte[] encodeResponse, String pass, String key) throws Exception {
    byte[] newArray = Arrays.copyOfRange(encodeResponse, 16, encodeResponse.length - 16);
    byte[] data = D(new String(newArray), key);
    data = functions.gzipD(data);
    String responseResult = new String(data);
    if (responseResult == "哥斯拉webshell解密失败！！") {
      String s = new String(data);
      return s;
    } 
    return responseResult;
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
