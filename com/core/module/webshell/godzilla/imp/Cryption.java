package com.core.module.webshell.godzilla.imp;

import com.core.util.functions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public interface Cryption {
  String decodeRequest(byte[] paramArrayOfbyte, String paramString1, String paramString2) throws Exception;
  
  String decodeResponse(byte[] paramArrayOfbyte, String paramString1, String paramString2) throws Exception;
  
  static String formatParameter(byte[] data2) throws IOException {
    byte[] parameterByte = data2;
    ByteArrayInputStream tStream = new ByteArrayInputStream(parameterByte);
    ByteArrayOutputStream tp = new ByteArrayOutputStream();
    String key = null;
    String result = "";
    byte[] lenB = new byte[4];
    Object var6 = null;
    InputStream inputStream = null;
    try {
      inputStream = new GZIPInputStream(tStream);
    } catch (Exception var11) {
      inputStream = tStream;
    } 
    while (true) {
      byte t = (byte)inputStream.read();
      if (t == -1) {
        tp.close();
        tStream.close();
        inputStream.close();
        return result;
      } 
      if (t == 2) {
        key = new String(tp.toByteArray());
        inputStream.read(lenB);
        int len = functions.bytesToInt(lenB);
        byte[] data = new byte[len];
        int readOneLen = 0;
        while ((readOneLen += inputStream.read(data, readOneLen, data.length - readOneLen)) < data.length);
        result = result + key + "=" + new String(data) + "\n";
        tp.reset();
        continue;
      } 
      tp.write(t);
    } 
  }
}
