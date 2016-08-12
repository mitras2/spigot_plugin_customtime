package de.jumpdrive.customtime;

import de.jumpdrive.customtime.helper.Helper;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author lucas
 */
public class CommandTabComplete {
    
    private Helper h = new Helper();

    public List<String> tabComplete(CommandSender sender, Command cmd, String label, String[] args){
        List<String> l = new ArrayList<String>();
        
        if(cmd.getName().equalsIgnoreCase("customtime")){
            if(args.length == 1){
                if(args[0].startsWith("0")){
                    l.add("0");
                    return l;
                }
                if(args[0].startsWith("1")){
                    l.add("1");
                    return l;
                }
                
                if(args[0].startsWith("a")){
                    l.add("autostart");
                    return l;
                }
                
                if(args[0].startsWith("h")){
                    l.add("help");
                    return l;
                }
                
                if(args[0].startsWith("sto")){
                    l.add("stop");
                    return l;
                }
                if(args[0].startsWith("sta")){
                    l.add("start");
                    return l;
                }
                if(args[0].startsWith("s")){
                    l.add("start");
                    l.add("stop");
                    return l;
                }
                
                if(args[0].startsWith("of")){
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
                
                if(args[0].startsWith("p")){
                    l.add("pollingrate");
                    return l;
                }
                
                if(args[0].startsWith("")){
                    l.add("help");
                    l.add("on");
                    l.add("off");
                    l.add("autostart");
                    l.add("pollingrate");
                    return l;
                }
            }
            
            if(args.length == 2){
                if(h.compare(args[0], new String[] {"on", "start", "1"})){
                    if(args[1].startsWith("h")){
                        l.add("help");
                        return l;
                    }
                    if(args[1].startsWith("")){
                        l.add("help");
                        return l;
                    }
                }
                
                if(args[0].equalsIgnoreCase("autostart")){
                    if(args[1].startsWith("t")){
                        l.add("true");
                        return l;
                    }
                    if(args[1].startsWith("f")){
                        l.add("false");
                        return l;
                    }
                    if(args[1].startsWith("1")){
                        l.add("1");
                        return l;
                    }
                    if(args[1].startsWith("0")){
                        l.add("0");
                        return l;
                    }
                    if(args[1].startsWith("on")){
                        l.add("on");
                        return l;
                    }
                    if(args[1].startsWith("of")){
                        l.add("off");
                        return l;
                    }
                    if(args[1].startsWith("o")){
                        l.add("on");
                        l.add("off");
                        return l;
                    }
                    if(args[1].startsWith("h")){
                        l.add("help");
                        return l;
                    }
                    if(args[1].startsWith("")){
                        l.add("true");
                        l.add("false");
                        l.add("help");
                        return l;
                    }
                }
                
                if(args[0].equalsIgnoreCase("pollingrate")){
                    if(args[1].startsWith("h")){
                        l.add("help");
                        return l;
                    }
                }
            }
            
        }
        
        return l;
    }
}
