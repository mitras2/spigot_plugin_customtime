package de.jumpdrive.customtime;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author lucas
 */
public class CommandTabComplete {

    public List<String> tabComplete(CommandSender sender, Command cmd, String label, String[] args){
        List<String> l = new ArrayList<String>();
        
        if(cmd.getName().equalsIgnoreCase("customtime")){
            if(args.length == 1){
                if(args[0].startsWith("of") || args[0].startsWith("off")){
                    l.add("off");
                    return l;
                }
                if(args[0].startsWith("on")){
                    l.add("on");
                    return l;
                }
                if(args[0].startsWith("o")){
                    l.add("on");
                    l.add("off");
                    return l;
                }
                if(args[0].startsWith("h")){
                    l.add("help");
                    return l;
                }
                if(args[0].startsWith("")){
                    l.add("help");
                    l.add("on");
                    l.add("off");
                    return l;
                }
            }
        }
        
        return l;
    }
}
