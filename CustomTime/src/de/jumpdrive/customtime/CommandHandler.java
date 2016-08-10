package de.jumpdrive.customtime;

import de.jumpdrive.customtime.helper.prefixes;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

/**
 *
 * @author lucas
 */
public class CommandHandler {
    
    private final CustomTimeMain plugin;
    
    private final prefixes prefixes = new prefixes();
    
    public CommandHandler(CustomTimeMain plugin){
        this.plugin = plugin;
    }
    
    public boolean processCommand(CommandSender sender,
            Command command, String label, String[] args){
        try {
            if (command.getName().equalsIgnoreCase("customtime")) {
                if(!(args.length >= 1)){
                    //ARGS sdt out (Hinweis auf Help)
                    customtime(sender);
                } else {
                    if(args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("1")){
                        // ON | START | 1
                        customtime_on(sender);
                    } else if(args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("0")){
                        // OFF | STOP | 0
                        customtime_off(sender);
                    } else if(args[0].equalsIgnoreCase("help")){
                        // HELP
                        customtime_help(sender);
                    } else {
                        // Unknow Argument
                        customtime(sender);
                    }
                }
                return true;
            }
            return false;
        } catch (CommandException e) {
            Bukkit.getLogger().warning("ERROR! onCommand did not work well...");
            return false;
        }
    }
    
    
    private void messageSender(CommandSender sender, String message){
        sender.sendMessage(message);
    }
    
    private void messageBroadcast(String message){
        Bukkit.getServer().broadcastMessage(message);
    }
    
    private void log(Level logLevel, String message){
        plugin.getLogger().log(logLevel, message);
    }
    
    
    
    
    private void customtime_help(CommandSender sender) {
        String helpText = "Realtime syncs the server Day-Night cycle to the day-night cycle of the servers local time.";
        sender.sendMessage(ChatColor.RED + helpText);
        customtime(sender);
    }
    
    private void customtime_on(CommandSender sender){
        plugin.customTimeStart();
        messageBroadcast(prefixes.PREFIX_INFO_HIGH + "CustomTime started");
    }
    
    private void customtime_off(CommandSender sender){
        plugin.customTimeStop();
        messageBroadcast(prefixes.PREFIX_INFO_HIGH + "CustomTime stoped. Back to vanilla time.");
    }
    
    private void customtime(CommandSender sender) {
        String sdttext = "Usage: /realtime <help:on:off>";
        sender.sendMessage(ChatColor.RED + sdttext);
    }
    
    
    ////////////////////////////////////////////////////////////////////////
    // Zeug zur Verarbeitung von Commands
    /*
    private void cmdRealTimeOn(CommandSender sender) {
        if(realTimeTask == null){
            
            int offset = new SettingRealtimeOffset().getSettingValue(this);
            realTimeTask = new RealTimeTask(1, offset);
            
            int pollingRate = new SettingRealtimePollingrate().getSettingValue(this);
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new StopDaylightTask(this, realTimeTask, pollingRate));
            
            
            sender.sendMessage("The DayNight cycle is now in realtime.");
        } else {
            sender.sendMessage("RealTime should be already running.");
        }
    }
    
    
    private void cmdRealTimeOff(CommandSender sender) {
        if(realTimeTask != null){
            realTimeTask.cancel();
            realTimeTask = null;
        } 
        
        String startDayNightCmd = "gamerule doDaylightCycle true";
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), startDayNightCmd);
        sender.sendMessage("The DayNight cycle is back in vanilla timing.");
    }

    private void cmdRealTimeStd(CommandSender sender) {
        String helptext = "Usage: /realtime <on:off:help>";
        sender.sendMessage(ChatColor.RED + helptext);
    }

    private void cmdRealTimeHelp(CommandSender sender) {
        String stdText = "Realtime syncs the server Day-Night cycle to the day-night cycle of the servers local time.";
        sender.sendMessage(ChatColor.RED + stdText);
        cmdRealTimeStd(sender);
    }
    */
    
}
