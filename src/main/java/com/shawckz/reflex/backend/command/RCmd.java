/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.backend.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Jonah Seguin on 12/13/2015.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RCmd {

    String name();

    String[] aliases() default {};

    String usage() default "";

    String description() default "";

    String permission() default "";

    boolean playerOnly() default false;

    int minArgs() default 0;

}
