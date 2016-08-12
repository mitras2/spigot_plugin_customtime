package de.jumpdrive.customtime;

import de.jumpdrive.customtime.helper.Helper;
import de.jumpdrive.customtime.helper.prefixes;
import de.jumpdrive.customtime.settings.SettingAutostart;
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
    private Helper h = new Helper();
    
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
                    if(h.compare(args[0], new String[] {"on", "start", "1"})){
                        if(args.length == 1){
                            customtime_on();
                        } else if(args.length > 1 && h.isHelp(args[1])) {
                            customtime_on_help(sender, args[0]);
                        }
                        // ON | START | 1
                    } else if(h.compare(args[0], new String[] {"off", "stop", "0"})){
                        // OFF | STOP | 0
                        customtime_off();
                        return true;
                    } else if (args[0].equalsIgnoreCase("autostart")){
                        if(args.length > 1){
                            if(h.compare(args[1], new String[] {"true", "1", "on"})){
                                customtime_autostart_true();
                                return true;
                            }
                            if(h.compare(args[1], new String[] {"false", "0", "off"})){
                                customtime_autostart_false();
                                return true;
                            }
                            if(h.compare(args[1], new String[] {"help"})){
                                customtime_autostart_help(sender);
                                return true;
                            }
                            //Form: customtime autostart crap
                            customtime_autostart(sender);
                            return true;
                        } else {
                            customtime_autostart(sender);
                            return true;
                        }
                    } else if(args[0].equalsIgnoreCase("pollingrate")){
                        if(args.length > 1){
                            if(h.compare(args[1], new String[] {"help"})){
                                customtime_pollingrate_help(sender);
                                return true;
                            }
                            //TODO reparieren !!!
                            if(Integer.parseInt(args[1]) > 0){
                                
                            }
                            //Form: customtime pollingrate CRAP
                            customtime_pollingrate(sender);
                        } else {
                            customtime_pollingrate(sender);
                            return true;
                        }
                    } else if(args[0].equalsIgnoreCase("help")){
                        // HELP
                        customtime_help(sender);
                    } else if(args[0].equalsIgnoreCase("test")){
                        // TEST !!!
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
    
    
    private void messageBroadcast(String message){
        Bukkit.getServer().broadcastMessage(message);
    }
    
    private void log(Level logLevel, String message){
        plugin.getLogger().log(logLevel, message);
    }
    
    
    
    private void customtime(CommandSender sender) {
        String sdttext = "Usage: /realtime <help:on:off>";
        sender.sendMessage(ChatColor.RED + sdttext);
    }
    
    private void customtime_on(){
        plugin.customTimeStart();
        messageBroadcast(prefixes.PREFIX_INFO_HIGH + "CustomTime started");
    }
    
    private void customtime_on_help(CommandSender sender, String commandArg){
        String[] on_help = {
            "Turn customtime on. Stops the normal daylightcycle to run the custom time.",
            "This does NOT enable the autostart of customtime on reboot of the server. Use 'customtime autostart true' to enable the autostart",
            "Usage: /customtime " + commandArg
        };
        h.messagesToSender(sender, on_help);
    }
    
    private void customtime_off(){
        plugin.customTimeStop();
        messageBroadcast(prefixes.PREFIX_INFO_HIGH + "CustomTime stoped. Back to vanilla time.");
    }
    
    private void customtime_help(CommandSender sender) {
        String helpText = "Realtime syncs the server Day-Night cycle to the day-night cycle of the servers local time.";
        sender.sendMessage(ChatColor.RED + helpText);
        customtime(sender);
    }
    
    private void customtime_autostart(CommandSender sender){
        String[] autostart_help = {
            "Enables or disables the autostart of the customtime-plugin for every server start and restart.",
            "This does NOT turn enable or disable the customtime plugin right now!",
            "Usage: /customtime autostart <help:true:false>"
        };
        h.messagesToSender(sender, autostart_help);
    }
    
    private void customtime_autostart_true(){
        SettingAutostart settingAutostart = new SettingAutostart();
        settingAutostart.saveAutostartActive(plugin);
    }
    
    private void customtime_autostart_false(){
        SettingAutostart settingAutostart = new SettingAutostart();
        settingAutostart.saveAutostartDisabled(plugin);
    }
    
    private void customtime_autostart_help(CommandSender sender){
        String[] autostart_help = {
            "Enables the autostart of the customtime-plugin for every server start and restart.",
            "This does NOT turn customtime on / enable it. Use 'customtime on' to enable the plugin now.",
            "Usage: /customtime autostart true"
        };
        h.messagesToSender(sender, autostart_help);
    }
    
    private void customtime_pollingrate(CommandSender sender){
        
    }
    
    private void customtime_pollingrate_int(CommandSender sender){
        
    }
    
    private void customtime_pollingrate_help(CommandSender sender){
        
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
