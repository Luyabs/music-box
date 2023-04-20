package com.example.musicbox.common;

import java.lang.annotation.*;

/**
 * 自定义注解@NeedToken
 * 在使用UserInfo.get()前一定要使用@NeedToken
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedToken {
    boolean required() default true;
}
