package com.core.module.webshell.godzilla.cryptions.asp;

import com.core.module.webshell.godzilla.annotation.CryptionAnnotation;
import com.core.module.webshell.godzilla.imp.Cryption;
import com.core.util.functions;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@CryptionAnnotation(Name = "ASP_BASE64", payloadName = "AspDynamicPayload")
public class AspBase64 implements Cryption {
  public String decodeRequest(byte[] encodeRequest, String pass, String key) throws Exception {
    String decode = URLDecoder.decode(new String(encodeRequest));
    byte[] bytes = functions.base64Decode(decode);
    return new String(bytes, StandardCharsets.UTF_8);
  }
  
  public String decodeResponse(byte[] encodeResponse, String pass, String key) throws Exception {
    byte[] newArray = Arrays.copyOfRange(encodeResponse, 6, encodeResponse.length - 6);
    byte[] bytes = functions.base64Decode(new String(newArray));
    return new String(bytes, StandardCharsets.UTF_8);
  }
}
