/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.backend.command;

/**
 * Created by Jonah Seguin on 12/13/2015.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class RCmdArgs {

    private final RCommandSender sender;
    private final String[] args;

    public RCmdArgs(RCommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
    }

    public RCommandSender getSender() {
        return sender;
    }

    public String[] getArgs() {
        return args;
    }

    public String getArg(int index) {
        return getArgs()[index];
    }

    public String[] getTrimmedArgs(int start, int end) {
        if (args.length < start || args.length < end) {
            throw new RCommandException("Args not long enough to trim");
        }
        String[] trimmed = new String[(args.length - start) + (args.length - end)];

        for (int i = start, x = 0; i < end; i++, x++) {
            trimmed[x] = args[i];
        }
        return trimmed;
    }

    public String[] getTrimmedArgs(int start) {
        return getTrimmedArgs(start, args.length);
    }

    public String getBuiltArgs() {
        return getBuiltArgs(0, args.length);
    }

    public String getBuiltArgs(int start) {
        return getBuiltArgs(start, args.length);
    }

    public String getBuiltArgs(int start, int end) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            String s = args[i];
            buffer.append(s);
            if (i < (args.length - 1)) {
                //not the last item in the array
                buffer.append(" ");
            }
        }
        return buffer.toString();
    }

}
