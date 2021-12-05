package dev.abelab.gifttree.annotation;

import java.lang.annotation.*;

/**
 * Authenticated Interface
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Authenticated {
}
