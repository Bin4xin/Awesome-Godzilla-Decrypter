package com.core;

import com.core.module.webshell.godzilla.annotation.CryptionAnnotation;
import com.core.module.webshell.godzilla.imp.Cryption;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class ApplicationContext {
  private static String PROPERTIES_FILE = "config.properties";

  public static boolean easterEgg = true;

  private static HashMap<String, Class<?>> cryptionMap = new HashMap<>();

  public static void init() {
    scanCryption();
  }

  private static void scanCryption() {
    try {
      URL url = ApplicationContext.class.getResource("/com/core/module/webshell/godzilla/cryptions/");
      int loadNum = scanClass(url.toURI(), "com.core.module.webshell.godzilla.cryptions", Cryption.class, CryptionAnnotation.class, cryptionMap);
      System.out.println(String.format("load cryption success! cryptionMaxNum:%s onceLoadCryptionNum:%s", new Object[] { Integer.valueOf(cryptionMap.size()), Integer.valueOf(loadNum) }));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static int scanClass(URI uri, String packageName, Class<?> parentClass, Class<?> annotationClass, Map<String, Class<?>> destMap) {
    int num = scanClassX(uri, packageName, parentClass, annotationClass, destMap);
    return num;
  }

  private static int scanClassX(URI uri, String packageName, Class<?> parentClass,
                                Class<?> annotationClass, Map<String, Class<?>> destMap) {
    AtomicInteger addNum = new AtomicInteger();
    Path basePath;
    FileSystem fs = null;

    try {
      if ("jar".equals(uri.getScheme())) {
        // JAR 文件系统需要创建
        fs = FileSystems.newFileSystem(uri, Collections.emptyMap());
        basePath = fs.getPath("/com/core/module/webshell/godzilla/cryptions/");
      } else {
        // 普通目录，直接使用默认文件系统
        basePath = Paths.get(uri);
      }

      // 遍历文件
      Files.walk(basePath).forEach(path -> {
        if (path.toString().endsWith(".class")) {
          try {
            // 计算相对路径，提取子目录和类名
            Path relative = basePath.relativize(path);
            int nameCount = relative.getNameCount();
            if (nameCount < 2) return; // 至少要有子目录和类文件
            String cryptionName = relative.getName(0).toString(); // 子目录名（如 asp）
            String className = relative.getName(1).toString().replace(".class", "");
            String fullClassName = packageName + "." + cryptionName + "." + className;

            Class<?> objectClass = Class.forName(fullClassName);
            if (parentClass.isAssignableFrom(objectClass) &&
                    objectClass.isAnnotationPresent((Class) annotationClass)) {
              Annotation annotation = objectClass.getAnnotation((Class) annotationClass);
              String name = (String) annotation.annotationType()
                      .getMethod("Name").invoke(annotation);
              destMap.put(name, objectClass);
              addNum.getAndIncrement();
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });

      return addNum.get();
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    } finally {
      if (fs != null) {
        try { fs.close(); } catch (IOException ignored) {}
      }
    }
  }

  public static synchronized boolean updateSettingKV(String key, String value) {
    boolean z = true;
    synchronized (ApplicationContext.class) {
      if (existsSettingKey(key)) {
        Properties properties = new Properties();
        try {
          FileInputStream inputStream = new FileInputStream(PROPERTIES_FILE);
          properties.load(inputStream);
          FileOutputStream fos = new FileOutputStream(PROPERTIES_FILE, true);
          properties.setProperty(key, value);
          properties.store(fos, "");
          inputStream.close();
          fos.close();
        } catch (Exception e) {
          e.printStackTrace();
          z = false;
        }
      } else {
        z = addSettingKV(key, value);
      }
    }
    return z;
  }

  public static synchronized boolean removeSettingK(String key) {
    boolean z = true;
    synchronized (ApplicationContext.class) {
      Properties properties = new Properties();
      try {
        FileReader fileReader = new FileReader(PROPERTIES_FILE);
        properties.load(fileReader);
        properties.remove(key);
        fileReader.close();
        FileWriter fileWriter = new FileWriter(PROPERTIES_FILE, false);
        properties.store(fileWriter, "Updated");
        fileWriter.close();
      } catch (Exception e) {
        e.printStackTrace();
        z = false;
      }
    }
    return z;
  }

  public static String getSettingValue(String key) {
    String value = null;
    Properties properties = new Properties();
    FileInputStream inputStream = null;
    File file = new File(PROPERTIES_FILE);
    if (file.exists() && file.isFile())
      try {
        inputStream = new FileInputStream(PROPERTIES_FILE);
        properties.load(inputStream);
        value = (String)properties.get(key);
      } catch (Exception e) {
        e.printStackTrace();
      }
    return value;
  }

  public static boolean existsSettingKey(String key) {
    boolean z = true;
    File file = new File(PROPERTIES_FILE);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
      z = false;
    } else if (!file.isFile() || getSettingValue(key) != null) {
      z = false;
    }
    return z;
  }

  public static synchronized boolean addSettingKV(String key, String value) {
    boolean z = true;
    synchronized (ApplicationContext.class) {
      if (existsSettingKey(key)) {
        z = updateSettingKV(key, value);
      } else {
        Properties properties = new Properties();
        try {
          FileInputStream inputStream = new FileInputStream(PROPERTIES_FILE);
          properties.load(inputStream);
          FileOutputStream fos = new FileOutputStream(PROPERTIES_FILE, true);
          properties.setProperty(key, value);
          properties.store(fos, "Update 'ddd' value");
          inputStream.close();
          fos.close();
        } catch (Exception e) {
          e.printStackTrace();
          z = false;
        }
      }
    }
    return z;
  }

  public static String[] getAllCryption(String payloadName) {
    Iterator<String> keys = cryptionMap.keySet().iterator();
    ArrayList<String> list = new ArrayList<>();
    while (keys.hasNext()) {
      String cryptionName = keys.next();
      Class<?> cryptionClass = cryptionMap.get(cryptionName);
      if (cryptionClass != null) {
        CryptionAnnotation cryptionAnnotation = cryptionClass.<CryptionAnnotation>getAnnotation(CryptionAnnotation.class);
        if (cryptionAnnotation.payloadName().equals(payloadName))
          list.add(cryptionName);
      }
    }
    return list.<String>toArray(new String[0]);
  }

  public static Cryption getCryption(String payloadName, String cryptionName) {
    Class<?> cryptionClass = cryptionMap.get(cryptionName);
    if (cryptionMap == null || !((CryptionAnnotation)cryptionClass.<CryptionAnnotation>getAnnotation(CryptionAnnotation.class)).payloadName().equals(payloadName))
      return null;
    try {
      return (Cryption)cryptionClass.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
