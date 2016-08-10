package de.jumpdrive.customtime.dump;

import de.jumpdrive.customtime.settings.SaveLastSetTime;
import java.util.GregorianCalendar;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author lucas
 */
public class RealTimeTask extends BukkitRunnable {

    private float timescale;
    private float offset;
    private float lastSetTime;
    
    private SaveLastSetTime lastSetTimeSaver;
    
    private GregorianCalendar cal;
    private int lastTickTime = 0;
    
    public RealTimeTask(double timescale, double offset){
        this.timescale = (float) timescale;
        this.offset = (float) offset;
        
        lastSetTimeSaver = new SaveLastSetTime();
        // Get the Last saved time and use it for further calculation
        
        cal = new GregorianCalendar();
    }
    

    @Override
    public void run() {
        
        //Set the calender object to teh current time
        cal.setTimeInMillis(System.currentTimeMillis());
        //Get the current time in hours, minutes and seconds
        int timeHH = cal.get(GregorianCalendar.HOUR_OF_DAY);
        int timeMM = cal.get(GregorianCalendar.MINUTE);
        int timeSS = cal.get(GregorianCalendar.SECOND);
        
        //Calculate the corresponding MC time tick.
        //Basiclis this is based on:
        // (currenttime)/(24) = (McTickTime)/(24000)
        // You have to substract 6000 from the result, because Minecraft-Days 
        // start at 6:00 and you have to compensate this offset.
        // Otherwise midnight in real life would be calculated as Tick 00000
        // which takes place a few seconds bevor sunrise...
        float calculatesTickFloat = ((((((float)timeSS/3600)+((float)timeMM/60)+(float)timeHH)/(24f*timescale))*24000f)-(6000+offset));
        //TODO calculate the way that the int alwas stays betwen 0 - 24000, indipendent of offset and scale
        if(calculatesTickFloat < 0){
            calculatesTickFloat += 24000f;
        }
        
        int calculatesTick = (int) calculatesTickFloat;

        //TODO manage debug messages
        //Bukkit.getLogger().info("Realtime had a run. CalculatedTick: " + calculatesTickFloat + " Hour: " + timeHH + " ,Min: " + timeMM + " ,Sec: " + timeSS + ". Cal is: " + cal.getCalendarType());
        //Bukkit.getLogger().info("Timescale is: " + timescale + " and offset runs for: " + offset);
        //Bukkit.getLogger().info("Time is: " + System.currentTimeMillis());
        
        if(calculatesTick != lastTickTime){
            String timeSetCmd = "time set " + calculatesTick;
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), timeSetCmd);
            lastTickTime = calculatesTick;
            
            /*
            String msg = "Time was set to " + calculatesTick;
            Bukkit.getLogger().info(msg);
            */
        }
        
    }
    
    
    
    @Override
    public synchronized void cancel() throws IllegalStateException{
        super.cancel();
        cal = null;
    }

}
