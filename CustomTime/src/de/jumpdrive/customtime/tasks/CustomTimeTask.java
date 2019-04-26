package de.jumpdrive.customtime.tasks;

import de.jumpdrive.customtime.CustomTimeMain;
import de.jumpdrive.customtime.settings.SaveLastSetTime;
import de.jumpdrive.customtime.settings.SettingAllowTimeChange;
import de.jumpdrive.customtime.settings.SettingAllowsleep;
import de.jumpdrive.customtime.settings.SettingDurationDay;
import de.jumpdrive.customtime.settings.SettingDurationNight;
import de.jumpdrive.customtime.settings.SettingWorldName;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;

/**
 * @author lucas
 */
public class CustomTimeTask implements Runnable {
    
    private final CustomTimeMain plugin;

    private final World serverWorld;
    
    
    /**
     * <b>allowTimeChange</b> is a boolean that defines wether the plugin accepts changes in 
     * game time, or wether it reverts them with the next run().<br>
     * <i>true</i> means that changes are accepted and time goes on based on the changed times.<br>
     * <i>false</i> means that the plugin will revert the changes made on the gameTime.
     */
    private boolean allowTimeChange;
    /**
     * <b>durationDay</b> stores the target duration of one Minecraft-day in reallife seconds.<br>
     * It is a long and therefore can only store integers and longs but no decimal numbers.
     */
    private long durationDay;
    /**
     * <b>millisPerDayTick</b> stores the real time in milliseconds required to pass, 
     * until one day-Tick can be be added. It is calculated by <i>durationDay</i>/12 .
     */
    private float millisPerDayTick;
    /**
     * <b>durationNight</b> stores the target duration of one Minecraft-night in reallife seconds.<br>
     * It is a long and therefore can only store integers and longs but no decimal numbers
     */
    private long durationNight;
    /**
     * <b>millisPerNightTick</b> stores the real time in milliseconds required to pass, 
     * until one day-Tick can be be added. It is calculated by <i>durationDay</i>/12 .
     */
    private float millisPerNightTick;
    /**
     * <b>sleepAwayTheNight</b> determines, wether the night ends with the next run() 
     * when all Players sleep or wether the plugin ignores sleeping players.
     */
    private boolean sleepAwayTheNight = false;        
    
    
    /**
     * <b>lastSetTime</b> is a long that holds the FullTimeTicks that were last set by CustomTimeTask.
     * After every run() of CustomTimeTask, this has to be updated to the new set time.
     */
    private long lastSetTime = 0;
    
    
    private SaveLastSetTime lastSetTimeSaver;
    
    //Final vars that are used inside the run()
    private final String MESSAGE_PREFIX_ERROR = ChatColor.RED + "[ERROR] ";
    private final String MESSAGE_PREFIX_WARNING = ChatColor.YELLOW + "[WARNING] ";
    
    
    //////Vars that are used and assigned inside the run()
    //Der epoch-Zeitstempel für den letzten gesetzten Tick
    private double lastTickTime = 0;
    
    //// TEMPORÄRE VARS für jeden run()
    // Die aktuelle Zeit(in Ticks) auf dem Server
    private long currentServerTickTime;
    // Angabe darüber um wieviel Ticks die Server-Tick-Zeit verändert werden muss
    private long offset;
    // Der epoch Zeitstempel für den aktuellen durchlauf von run()
    private long currentRealEpochTime;
    // Die Zeit die Verganen ist, seit run() das letzte mal durchlaufen wurde
    private double deltaTimeEpoch;
    // Die Information, ob diese Nacht an mit dem nächsten run() übersprungen werden muss,
    // da alle Spierler schlafen und "sleepAwayTheNight" true ist
    private boolean sleepThisNight = false;
    
    
    
    public CustomTimeTask(CustomTimeMain customTimeMain){
        
        this.plugin = customTimeMain;
        
        SettingAllowTimeChange settingAllowTimeChange =  new SettingAllowTimeChange();
        allowTimeChange = settingAllowTimeChange.getSettingValue(plugin);
        
        SettingDurationDay settingDurationDay = new SettingDurationDay();
        durationDay = settingDurationDay.getSettingValue(plugin);
        millisPerDayTick = durationDay / 12f;
        
        SettingDurationNight settingDurationNight = new SettingDurationNight();
        durationNight = settingDurationNight.getSettingValue(plugin);
        millisPerNightTick = durationNight / 12f;
        
        lastSetTimeSaver = new SaveLastSetTime();
        lastSetTime = lastSetTimeSaver.getSaveValue(plugin);
        
        SettingAllowsleep settingAllowsleep = new SettingAllowsleep();
        sleepAwayTheNight = settingAllowsleep.getSettingValue(plugin);

        
        SettingWorldName settingWorldName = new SettingWorldName();
        String worldName = settingWorldName.getSettingValue(plugin);
        serverWorld = Bukkit.getServer().getWorld(worldName);
        
        /*
        Bukkit.getServer().getLogger().log(Level.INFO, "List of Worlds: " + worlds.toString());
        */
        
        if(lastSetTime == 0 && serverWorld != null){
            lastSetTime = serverWorld.getFullTime();
        }
        
    }
    

    @Override
    public void run() {
        
        //DEBUG comment
        //test += 1;
        //Bukkit.getServer().getLogger().log(Level.INFO, "This is the " + test + " run of CustomTimeTask." );
        
        //Prüfen ob die ServerWorld nicht null ist.
        if(serverWorld == null){
                Bukkit.broadcastMessage(MESSAGE_PREFIX_ERROR + "Die Server-Welt konnte nicht geladen werden. CustomTime bricht ab. Bitte gib in der config den richtigen Welt-Namen an.");
                this.cancel();
        }
        
        
        //prüfe ob die Gamerule (doDayNightCycle noch auf Aus steht)
            //Wenn doDayNightCyle auf true, stoppe diese Ruannable
            // und geben Nachricht aus
        boolean doDayNightCycleState = serverWorld.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE);
        if(doDayNightCycleState){
            Bukkit.broadcastMessage(MESSAGE_PREFIX_ERROR + "doDayNightCycle wurde wieder aktiviert. CustomTime wird automatisch deaktiviert.");
            //TODO CustomTime über die MAIN stoppen und auf gestoppt setzen
            this.cancel();
        }
            
        
        //Aktuelle Tick-Zeit auf dem Server abfragen
        currentServerTickTime = serverWorld.getFullTime();
        
        //??? Ist das hier richtig?
        offset = 0;
            
        //prüfe ob sich die Tick-Zeit geändert hat
            // Wenn sich die Zeit geändert hat 
            // (Zeit wurde per externem Command oder Plugin verändert)
            // => Wenn es erlaubt ist die Zeit zu ändern (allowTimeChange)
                // Wenn die Zeit kleiner ist, als die zuletzt vom Plugin gesetzte Zeit
                    // Dann wird die gleiche Tageszeit basierend auf der aktuellen 
                    // lastSetTime berechnet.
                    // Sonst würde bei einem TimeSet0 der lokale Schwierigkeitsgrad
                    // zurückgesetzt werden (was meinst nicht absicht des Benutzers ist)
                // Wenn die extern gesetzte Zeit größer ist als die aktuelle lastSetTime
                    // lastSetTime = currentServerTickTime
                    // Dann wird lastSetTime auf die extern gesetzte Zeit angepasst.
                    // Die Zeit wird ab jetzt auf der geänderten Zeit berechnet
            // => Wenn es nicht erlaubt ist die Zeit zu ändern
                // lastSetTime bleibt auf den alten Stand vor der Zeitänderung.
                // Die Zeit wird in den folgenden berchnungne also auf basis der 
                // alten Zeit berechnet und dann gesetzt
        if(currentServerTickTime != lastSetTime ){
            if(allowTimeChange){
                if(currentServerTickTime + offset < lastSetTime){
                    long lastSetTimeDay = lastSetTime % 24000;
                    long currentTimeDay = currentServerTickTime % 24000;
                    
                    if(lastSetTimeDay > currentTimeDay){
                        offset += ((24000 - lastSetTimeDay) + currentTimeDay);
                    } else {
                        offset += (currentTimeDay - lastSetTimeDay);
                    }
                    
                } else {
                    offset += (currentServerTickTime - lastSetTime);
                }
            } else {
                //offset += (lastSetTime - currentServerTickTime);
                Bukkit.broadcastMessage(MESSAGE_PREFIX_WARNING + "Das Ändern der Zeit wurde in CustomTime deaktiviert.");
            }
        }
        
        
        
        // Prüfen ob in diesem run() die Zeit auf den nächsten Morgen gesetzt werden soll, 
        // weil alle Spieler geschlafen haben
            // dayTimeMorning legt fest welche Zeit nach dem schlafen als "Morgen" eingestellt wird.
            // berechne, welche Tageszeit gerade auf dem Server ist
                // Wenn die Ticks der aktuellen Tageszeit niedriger sind als die "Morgen"-Tick-Zeit
                    //Dann addiere einfach die fehlenden Ticks zum offset
                // Sonst
                    // Addiere zum offset die Tcks die bis 0 fehlen und die Ticks von 0 bis zur
                    // Morgen-Tick-Zeit
        if(getEndThisNight()){
            int dayTimeMorning = 23300;
            long lastSetTimeDay = (lastSetTime + offset) % 24000;
            if(lastSetTimeDay < dayTimeMorning){
                offset += dayTimeMorning - lastSetTimeDay;
            } else {
                offset += ((24000 - lastSetTimeDay) + dayTimeMorning);
            }
            clearEndThisNight();
        }
        
        

        //Aktuelle Zeit (RealLifeZeit) abfragen
        currentRealEpochTime = System.currentTimeMillis();
        
        //Wenn das Plugin gerade erst gestartet hat, dann gibt es noch keine Zeitpunkt 
        //für die letzte Verarbeitung. Darum wird der aktuelle Zeitpunkt für 
        //die letzte Verarbeitung angenommen 
        //(was für den ersten durchlauf eine differenz von 0 ergeben wird)
        if(lastTickTime == 0){
            lastTickTime = currentRealEpochTime;
        }
        
        //Die Differenz seit dem letzten gesetzten Tick und jetzt berechnen
        deltaTimeEpoch = currentRealEpochTime - lastTickTime;
        
        //DEBUG comment
        //Bukkit.getServer().getLogger().log(Level.INFO, "The currentRLTim for this run is " + currentRealEpochTime );
        //Bukkit.getServer().getLogger().log(Level.INFO, "The lastTickTime is " + lastTickTime );
        //Bukkit.getServer().getLogger().log(Level.INFO, "The deltaTime for this run is " + deltaTimeEpoch );
        
        //WHILE von der differenz noch was übrig und rest nicht false
        while(deltaTimeEpoch > 0){
            //Prüfen Ob lastSetDayTime +-Offset Tag oder Nacht ist
            if((lastSetTime+offset)%24000 <= 11999){
                //TAG
                    //Prüfen ob Restdifferenz noch für einen TagTick reicht
                        //Wenn die Zeit noch reicht
                            //Einen Tick beim Offset dazu zählen 
                            //lastTickTime auf lastTicktime + secondsPerDayTick setzen
                        //Wenn die Zeit nicht reicht
                            //differenz auf 0 setzen
                if(deltaTimeEpoch >= millisPerDayTick){
                    offset += 1;
                    //lastTickTime muss aufden Milisekunden-Stand gesetzt werden, 
                    //für den der aktuelle Tick auf dem MC-Server setht
                    lastTickTime += millisPerDayTick;
                    //deltaTime muss nagepasst werden, damit die while-Schleife nicht unendlich lange läuft
                    deltaTimeEpoch -= millisPerDayTick;
                } else {
                    deltaTimeEpoch = 0;
                }
            } else {
                //NACHT
                    //Prüfen ob Restdifferenz noch für einen NachtTick reicht
                        //Wenn die Zeit noch reicht
                            //Einen Tick beim Offset dazu zählen 
                            //lastTickTime auf lastTicktime + secondsPerNightTick setzen
                        //Wenn die Zeit nicht reicht
                            //differenz auf 0 setzen
                if(deltaTimeEpoch >= millisPerNightTick){
                    offset += 1;
                    //lastTickTime muss aufden Milisekunden-Stand gesetzt werden, 
                    //für den der aktuelle Tick auf dem MC-Server setht
                    lastTickTime += millisPerNightTick;
                    //deltaTime muss nagepasst werden, damit die while-Schleife nicht unendlich lange läuft
                    deltaTimeEpoch -= millisPerNightTick;
                } else {
                    deltaTimeEpoch = 0;
                }
            }
        }
        
        
        //Prüfen ob die GesamtTickZahl zu Hoch wird
            // Wird die Zahl zu hoch, muss -10000 * 24000 zum Ofset hinzugezählt werden
        if(lastSetTime+offset >= 2147448000){
            offset -= 1000 * 24000;
        }
        
        //DEBUG comment
        //Bukkit.getServer().getLogger().log(Level.INFO, "The new Offset is: " + offset );
                            
        
        //lastSetTime + Offset
        //offset = 0
        //setTime(lastSetTime)
        //save lastSetTime
        lastSetTime += offset;
        /*
        TODO Probiere Alternative
        String timeSetCmd = "time set " + lastSetTime;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), timeSetCmd);
        */
        /*
        serverWorld.setFullTime(lastSetTime);
        */
        serverWorld.setTime(lastSetTime);
//        Bukkit.getServer().getLogger().log(Level.INFO, "New Time is: " + Long.toString(serverWorld.getFullTime()));
        lastSetTimeSaver.saveNewLastSetTime(plugin, lastSetTime);
            
        //Reset the temporary vars
        offset = 0;
        currentServerTickTime = 0;
        currentRealEpochTime = 0;
        deltaTimeEpoch = 0;
        
        
        
        /*
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
        //}
        
    }
    
    public synchronized void cancel() throws IllegalStateException{
        
    }
    
    
    
    /**********************
     * Syncronized Setters
     *********************/
    
    public synchronized void setAllowTimeChange(boolean allowTimeChange){
        this.allowTimeChange = allowTimeChange;
    }
    
    public synchronized void updateDurationDay(){
        SettingDurationDay settingDurationDay = new SettingDurationDay();
        durationDay = settingDurationDay.getSettingValue(plugin);
        millisPerDayTick = durationDay / 12f;
    }
    
    public synchronized void updateDurationNight(){
        SettingDurationNight settingDurationNight = new SettingDurationNight();
        durationNight = settingDurationNight.getSettingValue(plugin);
        millisPerNightTick = durationNight / 12f;
    }
    
    public synchronized void setSleepAwayTheNight(boolean sleepAwayTheNight){
        this.sleepAwayTheNight = sleepAwayTheNight;
    }
    
    
    // Synchronized-Zugriff auf eine Interne Temporäre Variable
    public synchronized void endThisNight(){
        if(getSleepAwayTheNight()){
            sleepThisNight = true;
        }
    }
    
    private synchronized void clearEndThisNight(){
        this.sleepThisNight = false;
    }
    
    
    
    /**********************
     * Syncronized Getters
     *********************/
    
    private synchronized boolean getAllowTimeChange(){
        return allowTimeChange;
    }
    
    private synchronized long getDurationDay(){
        return durationDay;
    }
    
    private synchronized float getMillisPerDayTick(){
        return millisPerDayTick;
    }
    
    private synchronized long getDurationNight(){
        return durationNight;
    }
    
    private synchronized float getMillisPerNightTick(){
        return millisPerNightTick;
    }
    
    private synchronized boolean getSleepAwayTheNight(){
        return sleepAwayTheNight;
    }
    
    
    // Synchronized-Zugriff auf eine Interne Temporäre Variable
    private synchronized boolean  getEndThisNight(){
        return sleepThisNight;
    }
    
}    