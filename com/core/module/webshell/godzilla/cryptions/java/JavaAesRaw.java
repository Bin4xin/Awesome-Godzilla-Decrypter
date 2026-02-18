package com.core.module.webshell.godzilla.cryptions.java;

import com.core.module.webshell.godzilla.annotation.CryptionAnnotation;
import com.core.module.webshell.godzilla.imp.Cryption;
import com.core.util.functions;
import java.nio.charset.StandardCharsets;

@CryptionAnnotation(Name = "JAVA_AES_RAW", payloadName = "JavaDynamicPayload")
public class JavaAesRaw implements Cryption {
  public String decodeRequest(byte[] encodeRequest, String pass, String key) throws Exception {
    byte[] data = functions.hexToByteArray(new String(encodeRequest, StandardCharsets.UTF_8));
    data = JavaAesBase64.x(data, false, key);
    if (data == null)
      return "JavaAesRaw解密失败"; 
    String requestResult = functions.formatParameter(data);
    return requestResult;
  }
  
  public String decodeResponse(byte[] encodeResponse, String pass, String key) throws Exception {
    byte[] data = functions.hexToByteArray(new String(encodeResponse, StandardCharsets.UTF_8));
    data = JavaAesBase64.x(data, false, key);
    byte[] bytes = functions.gzipD(data);
    return new String(bytes);
  }
}
