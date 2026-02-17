package com.core.module.webshell.godzilla.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CryptionAnnotation {
  String Name();
  
  String payloadName();
}
