package com.jacky.springbootdemo.config;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author jacky
 * @time 2021-01-23 10:58
 * @discription
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface RoutingWithSlave {
}
