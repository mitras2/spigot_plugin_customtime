package de.jumpdrive.customtime.tasks;

import de.jumpdrive.customtime.CustomTimeMain;
import org.bukkit.Bukkit;

/**
 * @author lucas
 */
public class StopDaylightTask_2 implements Runnable{
    
    private CustomTimeMain plugin;
    private final String STOP_DAYLIGHT_CMD = "gamerule doDaylightCycle false";
    
    public StopDaylightTask_2(CustomTimeMain pluginMain){
        plugin = pluginMain;
    }
    
    @Override
    public void run() {
        boolean daylightCycleStopped = false;
        daylightCycleStopped = stopDaylightCycleCmd();
        
        while (!daylightCycleStopped) {
            try{
                Thread.sleep(2000);
            }catch(InterruptedException e){}
            daylightCycleStopped = stopDaylightCycleCmd();
        }
        
        plugin.startCustomTimeTask();
    }
    
    /*
    @Override
    public synchronized void cancel() throws IllegalStateException{
        super.cancel();
    }
    */
    
       
    
    private boolean stopDaylightCycleCmd(){
        return Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), STOP_DAYLIGHT_CMD);
    }
    
}
