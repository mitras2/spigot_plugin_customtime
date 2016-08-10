package de.jumpdrive.customtime.settings;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author lucas
 */
public class SettingAllowsleep {
    
    private final String SETTING_PATH = "settings.allowsleep";
    private final boolean ALLOWSLEEP_DEFAULT = true;
    
    
    public boolean getSettingValue(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        return config.getBoolean(SETTING_PATH);
    }
    
    public void saveAllowsleep(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, true);
        plugin.saveConfig();
    }
    
    public void saveDenysleep(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, false);
        plugin.saveConfig();
    }
    
    public void saveAllowsleepDefault(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, ALLOWSLEEP_DEFAULT);
        plugin.saveConfig();
    }
    
}
