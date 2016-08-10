package de.jumpdrive.customtime.settings;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author lucas
 */
public class SettingAllowTimeChange {
    
    private final String SETTING_PATH = "settings.allowtimechange";
    private final boolean ALLOWTIMECHNAGE_DEFAULT = true;
    
    
    public boolean getSettingValue(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        return config.getBoolean(SETTING_PATH);
    }
    
    public void saveAllowTimeChange(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, true);
        plugin.saveConfig();
    }
    
    public void saveDenyTimeChange(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, false);
        plugin.saveConfig();
    }
    
    public void saveAllowTimeChangeDefault(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, ALLOWTIMECHNAGE_DEFAULT);
        plugin.saveConfig();
    }
    
}
