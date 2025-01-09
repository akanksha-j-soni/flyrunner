package com.jsoni.flyrunner.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Custom annotation
@Retention(RetentionPolicy.RUNTIME) // Retained at runtime
@Target(ElementType.TYPE) // Can only be applied to classes
public @interface FlyrunnerInvoker {
}
