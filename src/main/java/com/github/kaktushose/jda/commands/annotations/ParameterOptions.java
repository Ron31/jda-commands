package com.github.kaktushose.jda.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This interface is used to annotate command methods to enable special behaviour for the last parameter.
 * It is not mandatory to annotate methods with this interface since the default
 * values will be suitable for the vast majority.
 *
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterOptions {
    /**
     * Indicates whether the last parameter is optional or not.<br>
     * The default value is false.
     */
    boolean isOptional() default false;

    /**
     * If set true, the last argument will be a String concatenated by the remaining arguments that could not be assigned.
     * Thus the last parameter must be a String.<br>
     * The default value is false.
     */
    boolean concat() default false;
}
