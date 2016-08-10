package de.jumpdrive.customtime.helper;

import org.bukkit.command.CommandSender;

/**
 * @author lucas
 */
public class Helper {
    
    public boolean isHelp(String string){
        return string.equalsIgnoreCase("help");
    }
    
    public boolean compare(String arg, String[] matches){
        for(String m : matches){
            if(arg.equalsIgnoreCase(m)){
                return true;
            }
        }
        return false;
    }
    
    public void messagesToSender(CommandSender sender, String[] messages){
        for(String msg: messages){
            sender.sendMessage(msg);
        };
    }
    
}
