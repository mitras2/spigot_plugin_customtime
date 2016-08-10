package de.jumpdrive.customtime.settings;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author lucas
 */
public class SettingWorldName {
    
    private final String SETTING_PATH = "settings.worldname";
    private final String WOLRDNAME_DEFAULT = "world";
    
    
    public String getSettingValue(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        return config.getString(SETTING_PATH);
    }
    
    public void saveWorldNameNew(JavaPlugin plugin, String worldName){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, worldName);
        plugin.saveConfig();
    }
    
    public void saveWorldNameDefault(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, WOLRDNAME_DEFAULT);
        plugin.saveConfig();
    }
    
}
