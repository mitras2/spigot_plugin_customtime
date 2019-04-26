package de.jumpdrive.customtime.settings;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author lucas
 */
public class SettingDurationDay {
    
    private final String SETTING_PATH = "settings.durationDay";
    private final int DURATION_DAY_DEFAULT = 3600;
    
    
    public long getSettingValue(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        return config.getLong(SETTING_PATH);
    }
    
    public void saveDurationDayNew(JavaPlugin plugin, long durationDay){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, durationDay);
        plugin.saveConfig();
    }
    
    public void saveDurationDayDefault(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, DURATION_DAY_DEFAULT);
        plugin.saveConfig();
    }
    
}
