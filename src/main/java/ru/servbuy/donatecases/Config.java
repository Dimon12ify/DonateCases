package ru.servbuy.donatecases;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config
{
    private static File dataFile;
    public static FileConfiguration dataConfig;
    
    public static void init() {
        loadStorage();
    }
    
    public static void loadStorage() {
        if (Main.getInstance().getDataFolder().exists()) {
            Main.getInstance().getDataFolder().mkdir();
        }
        Config.dataFile = new File(Main.getInstance().getDataFolder(), "storage.yml");
        if (!Config.dataFile.exists()) {
            try {
                Config.dataFile.createNewFile();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Config.dataConfig = YamlConfiguration.loadConfiguration(Config.dataFile);
        try {
            Config.dataConfig.save(Config.dataFile);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void saveStorage() {
        try {
            Config.dataConfig.save(Config.dataFile);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void reloadStorage() {
        Config.dataConfig = YamlConfiguration.loadConfiguration(Config.dataFile);
    }
}
