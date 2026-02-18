package com.core.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;
import javax.swing.JComboBox;

public class functions {
  private static final double CURRENT_HEIGHT;
  
  private static final double CURRENT_WIDTH;
  
  private static final double TOOLSKIT_HEIGHT = 1080.0D;
  
  private static final double TOOLSKIT_WIDTH = 1920.0D;
  
  static {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    if (1080.0D < screenSize.getHeight() || 1920.0D < screenSize.getWidth()) {
      CURRENT_WIDTH = 1920.0D;
      CURRENT_HEIGHT = 1080.0D;
    } else {
      CURRENT_WIDTH = screenSize.getWidth();
      CURRENT_HEIGHT = screenSize.getHeight();
    } 
  }
  
  public static void setWindowSize(Window window, int width, int height) {
    window.setSize((int)(width / 1920.0D * CURRENT_WIDTH), (int)(height / 1080.0D * CURRENT_HEIGHT));
  }
  
  public static void fireActionEventByJComboBox(JComboBox comboBox) {
    try {
      comboBox.setSelectedIndex(0);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static byte[] gzipD(byte[] data) {
    if (data.length == 0)
      return data; 
    try {
      return readInputStream(new GZIPInputStream(new ByteArrayInputStream(data), data.length));
    } catch (Exception e) {
      if (data.length < 200)
        System.out.println(new String(data, StandardCharsets.UTF_8)); 
      return "error!".getBytes();
    } 
  }
  
  public static byte[] readInputStream(InputStream inputStream) throws IOException {
    byte[] temp = new byte[5120];
    int readOneNum = 0;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    while ((readOneNum = inputStream.read(temp)) != -1)
      bos.write(temp, 0, readOneNum); 
    return bos.toByteArray();
  }
  
  public static String md5(String s) {
    String ret = null;
    try {
      MessageDigest m = MessageDigest.getInstance("MD5");
      m.update(s.getBytes(), 0, s.length());
      ret = (new BigInteger(1, m.digest())).toString(16).toUpperCase();
    } catch (Exception exception) {}
    return ret;
  }
  
  public static int bytesToInt(byte[] bytes) {
    int i = bytes[0] & 0xFF | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF) << 16 | (bytes[3] & 0xFF) << 24;
    return i;
  }
  
  public static byte[] base64Decode(String bs) throws Exception {
    byte[] value = null;
    try {
      Class<?> base64 = Class.forName("java.util.Base64");
      Object decoder = base64.getMethod("getDecoder", null).invoke(base64, null);
      value = (byte[])decoder.getClass().getMethod("decode", new Class[] { String.class }).invoke(decoder, new Object[] { bs });
    } catch (Exception e) {
      try {
        Class<?> base64 = Class.forName("sun.misc.BASE64Decoder");
        Object decoder = base64.newInstance();
        value = (byte[])decoder.getClass().getMethod("decodeBuffer", new Class[] { String.class }).invoke(decoder, new Object[] { bs });
      } catch (Exception exception) {}
    } 
    return value;
  }
  
  public static String subMiddleStr(String data, String leftStr, String rightStr) {
    int leftIndex = data.indexOf(leftStr) + leftStr.length();
    int rightIndex = data.indexOf(rightStr);
    if (leftIndex == -1 || rightIndex == -1)
      return null; 
    return data.substring(leftIndex, rightIndex);
  }
  
  public static String findStr(byte[] respResult, String pass, String key) {
    key = key.toLowerCase();
    String findStrMd5 = md5(pass + key);
    String findStrLeft = findStrMd5.substring(0, 16).toUpperCase();
    String findStrRight = findStrMd5.substring(16).toUpperCase();
    return subMiddleStr(new String(respResult), findStrLeft, findStrRight);
  }
  
  public static String getSecretKeyX(String pass) {
    return md5(pass).substring(0, 16).toLowerCase();
  }
  
  public static byte hexToByte(String inHex) {
    return (byte)Integer.parseInt(inHex, 16);
  }
  
  public static byte[] hexToByteArray(String inHex) {
    byte[] result;
    int hexlen = inHex.length();
    if (hexlen % 2 == 1) {
      hexlen++;
      result = new byte[hexlen / 2];
      inHex = "0" + inHex;
    } else {
      result = new byte[hexlen / 2];
    } 
    int j = 0;
    for (int i = 0; i < hexlen; i += 2) {
      result[j] = hexToByte(inHex.substring(i, i + 2));
      j++;
    } 
    return result;
  }
  
  public static String formatParameter(byte[] data2) {
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
      System.out.println(var11);
      return "error!";
    } 
    while (true) {
      byte t = 0;
      try {
        t = (byte)inputStream.read();
        if (t == -1) {
          tp.close();
          tStream.close();
          inputStream.close();
          return result;
        } 
        if (t == 2) {
          key = new String(tp.toByteArray());
          inputStream.read(lenB);
          int len = bytesToInt(lenB);
          byte[] data = new byte[len];
          int readOneLen = 0;
          while ((readOneLen += inputStream.read(data, readOneLen, data.length - readOneLen)) < data.length);
          result = result + key + "=" + new String(data, StandardCharsets.UTF_8) + "\n";
          tp.reset();
          continue;
        } 
        tp.write(t);
      } catch (IOException e) {
        return "哥斯拉webshell解密失败！！";
      } 
    } 
  }
}
