package de.jumpdrive.customtime;

import de.jumpdrive.customtime.eventSystem.EventListener;
import de.jumpdrive.customtime.settings.SettingAutostart;
import de.jumpdrive.customtime.tasks.CustomTimeTask;
import de.jumpdrive.customtime.settings.SettingPollingrate;
import de.jumpdrive.customtime.tasks.StartDaylightTask;
import de.jumpdrive.customtime.tasks.StopDaylightTask;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author lucas
 */
public class CustomTimeMain extends JavaPlugin {
    
    private BukkitTask customTimeBukkitTask;
    private CustomTimeTask customTimeTask;
    private CommandHandler commandHandler;
    private EventListener eventListener;
    
    private final CommandTabComplete tabCompletion = new CommandTabComplete();
    
    @Override
    public void onEnable() {
        // Check wether a config file is existent. If not write the standart config file
        this.saveDefaultConfig();
        
        commandHandler = new CommandHandler(this);
        eventListener = new EventListener(this);
        
        if(new SettingAutostart().getSettingValue(this)){
            // Start customtime
            customTimeStart();
        } else {
            // Output an hint that customtime is not active
        }
            
    }

    @Override
    public void onDisable() {
        if(customTimeBukkitTask != null){
            stopCustomTimeTask();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender,
            Command command,
            String label,
            String[] args) {
        return commandHandler.processCommand(sender, command, label, args);
    }
    
    
    public void customTimeStart(){
        //wenn noch kein CustomTimeTask läuft
            //Den Task zum Stoppen von doDayNight starten. Der Task muss dan hier zurückrufen und den Customtime Task starten
        // Benachrichtigung ausgeben, dass Customtime schon laufen sollte
        if(customTimeBukkitTask != null){
            //TODO Warnung ausgeben, dass der Task wahrscheinlich schon läuft
        } else {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new StopDaylightTask(this));
            //TODO Meldung ausgeben, dass gestartet wird
        }
    }
    
    public void customTimeStop(){
        //prüfen ob Customtime läuft
            //Den CustomTime Task Stoppen, und dann einen Start DayNightCycle Task starten
        // Wenn Customtime nicht läuft, Benachrichtigun ausgeben
        if(customTimeBukkitTask != null){
            //TODO Meldung ausgeben, dass CustomTime gestop wird
            stopCustomTimeTask();
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new StartDaylightTask(this));
        } else {
            //TODO Warnung ausgeben, dass der Task scheinbar nicht läuft
        }
    }
    
    public synchronized void startCustomTimeTask(){
        //Pollingrate abfragen
        //Task erstellen
        //Task starten
        SettingPollingrate settingPollingrate = new SettingPollingrate();
        int pollingRate = settingPollingrate.getSettingValue(this);
        customTimeTask = new CustomTimeTask(this);
        customTimeBukkitTask = getServer().getScheduler().runTaskTimer(this, customTimeTask, 4L, pollingRate);
        
    }
    
    public synchronized void stopCustomTimeTask(){
        //Task stoppen
        customTimeBukkitTask.cancel();
        customTimeBukkitTask = null;
        customTimeTask = null;
    }
    
    public synchronized void sleepAwayTheNight(){
        if(customTimeBukkitTask != null){
            customTimeTask.sleepThisNight();
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
        
        return tabCompletion.tabComplete(sender, cmd, label, args);
        
    }
    
}
