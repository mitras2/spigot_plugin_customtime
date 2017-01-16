package de.jumpdrive.customtime.tasks;

import de.jumpdrive.customtime.CustomTimeMain;
import org.bukkit.Bukkit;

/**
 * @author lucas
 */
public class StartDaylightTask implements Runnable{
    
    private CustomTimeMain plugin;
    private final String STOP_DAYLIGHT_CMD = "gamerule doDaylightCycle true";
    
    public StartDaylightTask(CustomTimeMain pluginMain){
        plugin = pluginMain;
    }
    
    @Override
    public void run() {
        boolean daylightCycleStarted = false;
        daylightCycleStarted = startDaylightCycleCmd();
        
        while (!daylightCycleStarted) {
            try{
                Thread.sleep(2000);
            }catch(InterruptedException e){}
            daylightCycleStarted = startDaylightCycleCmd();
        }
    }
    
    
    private boolean startDaylightCycleCmd(){
        return Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), STOP_DAYLIGHT_CMD);
    }
    
}
