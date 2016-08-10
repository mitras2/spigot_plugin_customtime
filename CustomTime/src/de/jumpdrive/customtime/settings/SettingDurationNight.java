package de.jumpdrive.customtime.settings;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author lucas
 */
public class SettingDurationNight {
    
    private final String SETTING_PATH = "settings.durationNight";
    private final int DURATION_NIGHT_DEFAULT = 720;
    
    
    public int getSettingValue(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        return config.getInt(SETTING_PATH);
    }
    
    public void saveDurationNightNew(JavaPlugin plugin, int durationNight){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, durationNight);
        plugin.saveConfig();
    }
    
    public void saveDurationNightDefault(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, DURATION_NIGHT_DEFAULT);
        plugin.saveConfig();
    }
    
}
