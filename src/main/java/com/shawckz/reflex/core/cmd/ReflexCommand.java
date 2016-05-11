package com.shawckz.reflex.core.cmd;

/**
 * Created by 360 on 5/30/2015.
 */

/**
 * The ReflexCommand interface
 * Used for handling commands using the {@link CommandHandler} Framework
 * All commands to be registered using the CommandHandler should implement this.
 * Max one command per class
 */
public interface ReflexCommand {

    /**
     * Called when the command is processed by a CommandSender
     * @param cmdArgs The Command arguements ({@link CmdArgs})
     */
    void onCommand(CmdArgs cmdArgs);

}
