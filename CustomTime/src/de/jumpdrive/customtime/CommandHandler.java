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
        if(!sender.isOp()){
            //User is not allowed to use the custom time command
            sender.sendMessage(ChatColor.RED + "Only server-operators are allowed to use this command");
            return true;
        }
        try {
            if (command.getName().equalsIgnoreCase("customtime")) {
                if(!(args.length >= 1)){
                    //ARGS sdt out (Hinweis auf Help)
                    customtime(sender);
                } else {
                    if(h.compare(args[0], new String[] {"on", "start"})){
                        if(args.length == 1){
                            customtime_on();
                        } else if(args.length > 1 && h.isHelp(args[1])) {
                            customtime_on_help(sender, args[0]);
                        }
                        // ON | START | 1
                    } else if(h.compare(args[0], new String[] {"off", "stop"})){
                        // OFF | STOP | 0
                        customtime_off();
                        return true;
                    } else if (args[0].equalsIgnoreCase("autostart")){
                        if(args.length > 1){
                            if(h.compare(args[1], new String[] {"true", "on"})){
                                customtime_autostart_true(sender);
                                return true;
                            }
                            if(h.compare(args[1], new String[] {"false", "off"})){
                                customtime_autostart_false(sender);
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
                    } else if(args[0].equalsIgnoreCase("allowSleep")){
                        if(args.length == 2){
                            if(h.compare(args[1], new String[] {"true"})){
                                customtime_allowSleep_true(sender);
                                return true;
                            }
                            if(h.compare(args[1], new String[] {"false"})){
                                customtime_allowSleep_false(sender);
                                return true;
                            }
                            if(h.compare(args[1], new String[] {"help"})){
                                customtime_allowSleep_help(sender);
                                return true;
                            }
                            //Form: customtime allowSleep CRAP
                            customtime_allowSleep_help(sender);
                        } else {
                            //Wrong number of arguments
                            customtime_allowSleep_help(sender);
                            return true;
                        }
                    } else if(args[0].equalsIgnoreCase("duration")){
                        if(args.length >= 2){
                            if(h.compare(args[1], new String[] {"day"})){
                                if(args.length == 3){
                                    if(h.isInteger(args[2]) && Long.parseLong(args[2]) > 0){
                                        customtime_duration_day_long(sender, Long.parseLong(args[2]));
                                        return true;
                                    }
                                    //Not a number or it is 'help'
                                    customtime_duration_day_help(sender);
                                } else {
                                    //Wrong number of arguments
                                    customtime_duration_day(sender);
                                    return true;
                                }
                            }
                            if(h.compare(args[1], new String[] {"night"})){
                                if(args.length == 3){
                                    if(h.isInteger(args[2]) && Long.parseLong(args[2]) > 0){
                                        customtime_duration_night_long(sender, Long.parseLong(args[2]));
                                        return true;
                                    }
                                    //Not a number or it is 'help'
                                    customtime_duration_night_help(sender);
                                } else {
                                    //Wrong number of arguments
                                    customtime_duration_night(sender);
                                    return true;
                                }
                            }
                            if(h.compare(args[1], new String[] {"help"})){
                                customtime_duration_help(sender);
                                return true;
                            }
                            //Form: customtime allowSleep CRAP
                            customtime_duration(sender);
                        } else {
                            //Wrong number of arguments
                            customtime_duration(sender);
                            return true;
                        }
                    } else if(args[0].equalsIgnoreCase("pollingrate")){
                        if(args.length > 1){
                            if(h.compare(args[1], new String[] {"help"})){
                                customtime_pollingrate_help(sender);
                                return true;
                            }
                            if(h.isInteger(args[1]) && Integer.parseInt(args[1]) > 0){
                                customtime_pollingrate_int(sender, Integer.parseInt(args[1]));
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
        String sdttext = "Usage: /customtime <help:on:off>";
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
        String helpText = "The 'Customtime' plugin allows to define how long one day and one night on this Minecraft server should be.";
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
    
    private void customtime_autostart_true(CommandSender sender){
        SettingAutostart settingAutostart = new SettingAutostart();
        settingAutostart.saveAutostartActive(plugin);
        sender.sendMessage("Enabled 'autostart' for customtime. The custom day-night-cycle will automatically be started upon server start/restart");
    }
    
    private void customtime_autostart_false(CommandSender sender){
        SettingAutostart settingAutostart = new SettingAutostart();
        settingAutostart.saveAutostartDisabled(plugin);
        sender.sendMessage("Disabled 'autostart' for customtime. The custom day-night-cycle will not be started upon server start/restart");
    }
    
    private void customtime_autostart_help(CommandSender sender){
        this.customtime_autostart(sender);
    }

    private void customtime_allowSleep_true(CommandSender sender){
        plugin.setAllowSleep(true);
        sender.sendMessage("Sleeping enabled. If all players go to bed the night will be skipped.");
    }

    private void customtime_allowSleep_false(CommandSender sender){
        plugin.setAllowSleep(false);
        sender.sendMessage("Sleeping disabled. Even if all players go to bed the night will NOT be skipped.");
    }

    private void customtime_allowSleep_help(CommandSender sender){
        String[] allowSleep_help = {
                "Enables to skip the night when all player lie in their beds.",
                "Usage: /customtime allowSleep <true:false>"
        };
        h.messagesToSender(sender, allowSleep_help);
    }

    private void customtime_duration(CommandSender sender){
        String[] duration_help = {
                "Set the duration of night and day in the customtime plugin.",
                "The duration needs to be given in seconds.",
                "Usage: /customtime duration <day:night> <int:help>"
        };
        h.messagesToSender(sender, duration_help);
    }

    private void customtime_duration_help(CommandSender sender){
        this.customtime_duration(sender);
    }

    private void customtime_duration_day(CommandSender sender){
        String[] duration_day_help = {
                "Set the duration of one day in the customtime plugin.",
                "The duration needs to be specified in seconds",
                "Usage: /customtime duration day <int:long>"
        };
        h.messagesToSender(sender, duration_day_help);
    }

    private void customtime_duration_day_help(CommandSender sender) {
        this.customtime_duration_day(sender);
    }

    private void customtime_duration_day_long(CommandSender sender, long durationDay){
        plugin.setDuationDay(sender, durationDay);
    }

    private void customtime_duration_night(CommandSender sender){
        String[] duration_day_help = {
                "Set the duration of one night in the customtime plugin.",
                "The duration needs to be specified in seconds",
                "Usage: /customtime duration night <int:long>"
        };
        h.messagesToSender(sender, duration_day_help);
    }

    private void customtime_duration_night_help(CommandSender sender){
        this.customtime_duration_night(sender);
    }

    private void customtime_duration_night_long(CommandSender sender, long durationNight){
        plugin.setDurationNight(sender, durationNight);
    }
    
    private void customtime_pollingrate(CommandSender sender){
        String[] allowSleep_help = {
                "Sets the polling rate for the plugin. The polling rate determines, how often the plugin updates the time-of-day on the server.",
                "The polling rate is specified in 'server ticks', where 20 ticks mean the in-game time is updated once per second.",
                "Usage: /customtime pollingrate <int>"
        };
        h.messagesToSender(sender, allowSleep_help);
    }
    
    private void customtime_pollingrate_int(CommandSender sender, int pollingrate){
        plugin.setPollingRate(sender, pollingrate);
    }
    
    private void customtime_pollingrate_help(CommandSender sender){
        this.customtime_pollingrate(sender);
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
