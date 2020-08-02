package com.github.kaktushose.jda.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String[] value() default "";

    boolean isDefault() default false;

    String name() default "";

    String desc() default "";

    String help() default "";

    String usage() default "";

    boolean isActive() default true;
}
