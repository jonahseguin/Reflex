package com.shawckz.reflex.core.cmd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 360 on 5/28/2015.
 */

/**
 * The Command annotation
 * Used to annotate classes (ElementType.TYPE) that
 * implement the ReflexCommand interface.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {

    /**
     * The name of the command
     * @return The command name
     */
    String name();

    /**
     * The command usage; displayed when the minArgs() is not met
     * Example: '/test [something]'
     * @return The command usage
     */
    String usage() default "";

    /**
     * The minimum args that this command needs to process
     * @return The minimum args
     */
    int minArgs() default 0;

    /**
     * Whether or not this command can only be run by a player
     * @return If this command is player only
     */
    boolean playerOnly() default false;

    /**
     * The permission required to use this command
     * @return The permission
     */
    String permission() default "";

    /**
     * The message sent to the command sender if the permission is not met
     * @return The no permission message
     */
    String noPerm() default "§cNo permission.";

    /**
     * Whether or not this command allows flags (arguments starting with '-')
     * @return If this command allows flags
     */
    boolean allowFlags() default false;

    /**
     * All of the available flags for this command
     * @return A string[] of available flags
     */
    String[] flags() default {};

}
