package de.jumpdrive.customtime.eventSystem;

import de.jumpdrive.customtime.CustomTimeMain;
import de.jumpdrive.customtime.settings.SettingAllowsleep;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

/**
 * @author lucas
 */
public class EventListener implements Listener{
    
    private final CustomTimeMain plugin;
    
    private boolean allowSleep;
    
    public EventListener (CustomTimeMain plugin){
        this.plugin = plugin;
        
        SettingAllowsleep settingAllowsleep = new SettingAllowsleep();
        allowSleep = settingAllowsleep.getSettingValue(plugin);
        
        register();
    }
    
    private void register() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    
    //Wenn ein Spieler in Bett geht, wird geprüft, ob das Schlafen erlaubt ist
        //Ist das Schlafen erlaubt, wird eine Liste aller Spieler die sich in
        // der selben Welt befinden, geladen.
        // Dann wird geprüft, ob jeder Spieler in dieser Welt schläft.
            // Wenn jeder Spieler in dieser Welt schläft, wird 
            // ein Befehl aufgerufen, der den CustomTimeTask dazu bring, 
            // die Zeit auf den nächsten Morgen zu setzten
    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerBedEnter(PlayerBedEnterEvent event){
        
        //DEBUG comment
        Bukkit.getServer().getLogger().log(Level.INFO, "Ein Spieler ist ins Bett gegangen" );
        
        if(allowSleep){
            List<Player> playerList = event.getPlayer().getWorld().getPlayers();

            boolean sleepNight = true;
            for(Player player:playerList){
                
                if(!player.isSleeping() && player.getUniqueId().compareTo(event.getPlayer().getUniqueId()) != 0){
                    
                    //DEBUG comment
                    String logString = "Der Spieler " + player.getDisplayName() + " wird NICHT als schlafend gewertet";
                    Bukkit.getServer().getLogger().log(Level.INFO, logString );
                    
                    sleepNight = false;
                }
            }
            
            if(sleepNight){
                
                //DEBUG comment
                Bukkit.getServer().getLogger().log(Level.INFO, "Aller Spieler scheinen zu schlafen." );
                
                plugin.sleepAwayTheNight();
            }
        }
    }
    
    
    
}
