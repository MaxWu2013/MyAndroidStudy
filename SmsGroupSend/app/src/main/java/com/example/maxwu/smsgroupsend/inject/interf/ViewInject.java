package com.example.maxwu.smsgroupsend.inject.interf;

/**
 * User: MaxWu
 * Date: 2015-04-09
 * Time: 15:52
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {
    int value() default -1;
}
