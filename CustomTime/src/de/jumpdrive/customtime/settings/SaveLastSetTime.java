package de.jumpdrive.customtime.settings;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author lucas
 */
public class SaveLastSetTime {
    
    private final String SAVE_PATH = "save.lastSetTime";
    private final int LAST_SET_TIME_DEFAULT = 0;
    
    
    public long getSaveValue(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        return config.getLong(SAVE_PATH);
    }
    
    public void saveNewLastSetTime(JavaPlugin plugin, long lastSetTime){
        FileConfiguration config = plugin.getConfig();
        config.set(SAVE_PATH, lastSetTime);
        plugin.saveConfig();
    }
    
    public void resetLastSetTime(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        config.set(SAVE_PATH, LAST_SET_TIME_DEFAULT);
        plugin.saveConfig();
    }
    
}
