package de.jumpdrive.customtime.settings;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author lucas
 */
public class SettingAutostart {
    
    private final String SETTING_PATH = "settings.autostart";
    private final boolean AUTOSTART_DEFAULT = true;
    
    public boolean getSettingValue(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        return config.getBoolean(SETTING_PATH);
    }
    
    public void saveAutostartActive(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, true);
        plugin.saveConfig();
    }
    
    public void saveAutostartDisabled(JavaPlugin plugin){
        FileConfiguration config = plugin.getConfig();
        config.set(SETTING_PATH, false);
        plugin.saveConfig();
    }
    
    public void saveModeDefault(JavaPlugin plugin){
        this.saveAutostartActive(plugin);
    }
    
}
