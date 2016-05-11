package com.shawckz.reflex.core.cmd;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by 360 on 5/28/2015.
 */

/**
 * The CmdArgs class
 * Used to store command argument data, and provide useful methods.
 */
@RequiredArgsConstructor
public class CmdArgs {

    @NonNull @Getter private CommandSender sender;
    @NonNull @Setter private String[] args;

    /**
     * Similar to a string builder, appends values of args separated by a space into a String.
     * @param start the arg index to start appending at
     * @param end the arg index to stop appending at; -1 to stop appending at the args length
     * @return The appended String
     */
    public String getJoinedArgs(int start,int end){
        if(args.length < start) return "";
        String s = "";
        for(int i = start; i < (end >= 0 ? end : args.length); i++){
            s += args[i] + " ";
        }
        if(s.length() > 2){
            s = s.substring(0 ,s.length() - 1);//To remove trailing space
        }
        return s;
    }

    /**
     * Returns a String[] of the args; without flags.
     * @return String[] of arguments without flags
     */
    public String[] getArgs() {
        return filterFlags(args);
    }

    /**
     * Strips the flags (arguments starting with '-') from a String[]
     * @param a The String[] to filter
     * @return The filtered String[]
     */
    private String[] filterFlags(String[] a){//removes the flags
        int x = 0;
        int removed = 0;
        String[] newArgs = new String[a.length];
        for(int i = 0; i < a.length; i++){
            String s = a[i];
            if(!s.startsWith("-")){
                newArgs[x] = s;
                x++;
            }
            else{
                removed++;
            }
        }
        String[] xArgs = new String[a.length - removed];
        for(int i = 0; i < newArgs.length; i++){
            xArgs[i] = newArgs[i];
        }
        return xArgs;
    }

    /**
     * Gets if the args have any flags
     * @return true if has args, false if not
     */
    public boolean hasFlags(){
        for(int i = 0; i < args.length; i++){
            if(args[i].startsWith("-")){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets how many flags there are
     * @return The amount of flags in the args
     */
    public int getFlagsCount(){
        int x = 0;
        for(int i = 0; i < args.length; i++){
            if(args[i].startsWith("-")){
                x++;
            }
        }
        return x;
    }

    /**
     * Gets a String[] of flags in the args (A string[] of args that start with '-')
     * @return The flags
     */
    public String[] getFlags() {
        String[] flags = new String[getFlagsCount()];
        int x = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                flags[x] = args[i];
                x++;
            }
        }
        return flags;
    }

    /**
     * Gets if the args has a specific flag
     * @param flag The flag
     * @return true if the args have that flag, false if not
     */
    public boolean hasFlag(String flag){
        String[] flags = getFlags();
        for(int i = 0; i < flags.length; i++){
            String s = flags[i];
            if(s.equalsIgnoreCase(flag) || s.contains(flag)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a player from a specific arguement
     * @param arg The arguement to get the player from
     * @return The player if found, null if not found
     */
    public Player getPlayer(int arg){
        return Bukkit.getPlayer(getArg(arg));
    }

    /**
     * Attempts to match a string into a player's name, if no player is found with
     * the specified name, it just returns the name; if there is a player online that
     * matches, it sets the return value to that player's name.
     * @param arg The arg to get the name from
     * @return The name
     */
    public String matchPlayer(int arg){
        String s = getArg(arg);
        Player t = Bukkit.getPlayer(s);
        if(t != null){
            s = t.getName();
        }
        return s;
    }

    /**
     * Attempts to match a string into a player's name, if no player is found with
     * the specified name, it just returns the name; if there is a player online that
     * matches, it sets the return value to that player's name.
     * @param s The name
     * @return The name
     */
    public String matchPlayer(String s){
        Player t = Bukkit.getPlayer(s);
        if(t != null){
            s = t.getName();
        }
        return s;
    }

    /**
     * Gets a joined String from args, starting at a specific index (calls getJoinedArgs(int,int))
     * @param start The index to start appending at
     * @return The String of appended arguments
     */
    public String getJoinedArgs(int start){
        return getJoinedArgs(start,-1);
    }

    /**
     * Gets the argument at a specific index
     * @param index The index
     * @return The arg
     */
    public String getArg(int index){
        return args[index];
    }

    /**
     * Trims the args starting at a specific index
     * @param start The index
     * @return The trimmed String[] of args
     */
    public String[] trim(int start){
        String[] newArgs = new String[args.length - start];
        int x = 0;
        for(int i = start; i < args.length; i++){
            newArgs[x] = args[i];
            x++;
        }
        return newArgs;
    }

    /**
     * Trims the args starting at a specific index, and ending at a specific index
     * @param start The index
     * @param end The end index
     * @return The trimmed String[] of args
     */
    public String[] trim(int start, int end){
        String[] newArgs = new String[args.length - start];
        int x = 0;
        for(int i = start; i < (end >= 0 ? end : args.length); i++){
            newArgs[x] = args[i];
            x++;
        }
        return newArgs;
    }

}
