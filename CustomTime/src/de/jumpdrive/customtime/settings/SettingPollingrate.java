package de.jumpdrive.customtime.settings;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author lucas
 */
public class SettingPollingrate {
    
    private final String SETTING_PATH = "settings.pollingrate";
    private final int POLLINGRATE_DEFAULT = 60;
    
    
    public int getSettingValue(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        return config.getInt(SETTING_PATH);
    }
    
    public void savePollingrateNew(JavaPlugin plugin, int pollingrate){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, pollingrate);
        plugin.saveConfig();
    }
    
    public void savePollingrateDefault(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, POLLINGRATE_DEFAULT);
        plugin.saveConfig();
    }
    
}
