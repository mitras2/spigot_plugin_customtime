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
    
    public boolean isInteger(String str){
        if (str == null) {
            return false;
        }
        
        int length = str.length();
        if (length == 0) {
            return false;
        }
        
        int i = 0;
        String valueLimitStr;
        boolean insideLimit = false;
        
        if (str.charAt(0) == '-') {
            //Wenn der String mit einem Minus anfängt
            if (length == 1) {
                return false;
            }
            i = 1;
            valueLimitStr = Integer.toString(Integer.MIN_VALUE);
        } else {
            valueLimitStr = Integer.toString(Integer.MAX_VALUE);
        }
        
        if(length > valueLimitStr.length()){
            return false;
        }
        if(length < valueLimitStr.length()){
            insideLimit = true;
        }
        
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            } 
            
            if(!insideLimit){
                //Prüfe auf Überschreitung des Limits
                if(Integer.parseInt(Character.toString(c)) > Integer.parseInt(Character.toString(valueLimitStr.charAt(i)))){
                    // Wenn diese Stelle größer ist, als die selbe Stelle im Maximum/Minimum überschreiten wir das Limit
                    return false;
                }
                if(Integer.parseInt(Character.toString(c)) < Integer.parseInt(Character.toString(valueLimitStr.charAt(i)))){
                    // Wenn diese Stelle kleiner ist, als die selbe Stelle im Maximum/Minimum dann sind wir innerhalb des Limits
                    insideLimit = true;
                }
            }
            
        }
        return true;
    }
    
    
    

    
}


