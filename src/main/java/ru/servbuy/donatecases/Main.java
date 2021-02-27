package ru.servbuy.donatecases;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.servbuy.donatecases.gui.ClickListener;
import ru.servbuy.donatecases.gui.ItemBuilder;
import ru.servbuy.donatecases.listeners.EventListener;
import ru.servbuy.donatecases.structure.AnimSettings;
import ru.servbuy.donatecases.structure.Case;
import ru.servbuy.donatecases.structure.Item;
import ru.servbuy.donatecases.structure.ItemType;
import ru.servbuy.donatecases.utils.StringUtil;

import java.io.File;
import java.util.*;

public class Main extends JavaPlugin
{
    @Getter private static Main instance;
    @Getter private List<Case> cases;
    private Map<Location, Boolean> casesLocations;
    private Map<Location, Hologram> opened;
    @Getter private List<String> cache;
    
    public Case getCase(final String caseName) {
        return cases.stream().filter(x -> x.getName().equalsIgnoreCase(caseName)).findAny().orElse(null);
    }
    
    public Set<Location> getCasesLocations() {
        return this.casesLocations.keySet();
    }
    
    public boolean getDirection(final Location loc) {
        return this.casesLocations.get(loc);
    }
    
    public Set<Location> getOpened() {
        return this.opened.keySet();
    }
    
    public Hologram getHologram(final Location loc) {
        return this.opened.get(loc);
    }
    
    public FileConfiguration getStorage() {
        return Config.dataConfig;
    }
    
    public void saveStorage() {
        Config.saveStorage();
    }
    
    public void reloadStorage() {
        Config.reloadStorage();
    }
    
    public void onEnable() {
        Main.instance = this;
        if (!new File(getDataFolder(), "config.yml").exists())
            getConfig().options().copyDefaults(true);
        saveConfig();
        Config.init();
        cases = new ArrayList<>();
        casesLocations = new HashMap<>();
        opened = new HashMap<>();
        cache = new ArrayList<>();
        loadCases();
        getServer().getPluginManager().registerEvents(new ClickListener(), this);
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getCommand("dc").setExecutor(new Commands());
    }
    
    public void reloadCases() {
        this.cases.clear();
        this.loadCases();
    }
    
    public void loadCases() {
        for (final String caseName : this.getConfig().getConfigurationSection("cases").getValues(false).keySet()) {
            final List<String> itemsName = new ArrayList<>(getConfig().getConfigurationSection("cases." + caseName + ".items").getValues(false).keySet());
            final Item[] items = new Item[itemsName.size()];
            for (int index = 0; index < itemsName.size(); index++) {
                final String[] commands = new String[this.getConfig().getStringList("cases." + caseName + ".items." + itemsName.get(index) + ".commands").size()];
                for (int command = 0; command < commands.length; ++command) {
                    commands[command] = this.getConfig().getStringList("cases." + caseName + ".items." + itemsName.get(index) + ".commands").get(command);
                }
                final ItemType type = ItemType.valueOf(this.getConfig().getString("cases." + caseName + ".items." + itemsName.get(index) + ".type"));
                String group = null;
                if (type == ItemType.GROUP) {
                    group = this.getConfig().getString("cases." + caseName + ".items." + itemsName.get(index) + ".group");
                }
                items[index] = new Item(
                        StringUtil.colorize(this.getConfig().getString("cases." + caseName + ".items." + itemsName.get(index) + ".title")),
                        commands,
                        new ItemBuilder(this.getConfig().getString("cases." + caseName + ".items." + itemsName.get(index) + ".id")).build(),
                        group,
                        getConfig().getString("cases." + caseName + ".items." + itemsName.get(index) + ".group-command"),
                        getConfig().getInt("cases." + caseName + ".items." + itemsName.get(index) + ".chance"),
                        type);
            }
            cases.add(new Case(items,
                    caseName, new AnimSettings(
                        Sound.valueOf(this.getConfig().getString("cases." + caseName + ".animation-settings.rotate-sound")),
                        Sound.valueOf(this.getConfig().getString("cases." + caseName + ".animation-settings.end-sound")),
                        getConfig().getDouble("cases." + caseName + ".animation-settings.items-speed"),
                        getConfig().getDouble("cases." + caseName + ".animation-settings.particles-speed"),
                        getConfig().getInt("cases." + caseName + ".animation-settings.rotate-time"),
                        getConfig().getDouble("cases." + caseName + ".animation-settings.radius"),
                        getConfig().getBoolean("cases." + caseName + ".animation-settings.use-frostlord"))
            ));
        }
        if (getInstance().getConfig().getStringList("case-locations") != null) {
            for (final String location : getInstance().getConfig().getStringList("case-locations")) {
                final String[] locat = location.split(";");
                final Location loc = new Location(Bukkit.getWorld(locat[0]), Integer.parseInt(locat[1]), Integer.parseInt(locat[2]), Integer.parseInt(locat[3]));
                final Hologram hd = HologramsAPI.createHologram(this, loc.clone().add(0.5, 2.0, 0.5));
                for (final String line : getInstance().getConfig().getStringList("hologram")) {
                    hd.appendTextLine(StringUtil.colorize(line));
                }
                this.opened.put(loc, hd);
                this.casesLocations.put(loc, locat[4].equalsIgnoreCase("x"));
            }
        }
    }
}
