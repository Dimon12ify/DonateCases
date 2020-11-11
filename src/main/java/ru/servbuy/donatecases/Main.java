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
import ru.servbuy.donatecases.structure.Case;
import ru.servbuy.donatecases.structure.Item;
import ru.servbuy.donatecases.structure.ItemType;
import ru.servbuy.donatecases.utils.StringUtil;

import java.util.*;

public class Main extends JavaPlugin
{
    private static Main instance;
    @Getter private List<Case> cases;
    private Map<Location, Boolean> casesLocations;
    private Map<Location, Hologram> opened;
    @Getter private List<String> cache;
    
    public static Main getInstance() {
        return Main.instance;
    }
    
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
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        Config.init();
        this.cases = new ArrayList<>();
        this.casesLocations = new HashMap<>();
        this.opened = new HashMap<>();
        this.cache = new ArrayList<>();
        this.loadCases();
        this.getServer().getPluginManager().registerEvents(new ClickListener(), this);
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        this.getCommand("cases").setExecutor(new Commands());
        final List<String> fakeBroadcast = getInstance().getConfig().getStringList("MESSAGES.FAKE_BROADCAST");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(getInstance(), () -> {
            this.cache.clear();
            Bukkit.getOnlinePlayers().forEach(pl ->
                    pl.sendMessage(fakeBroadcast.stream().map(y ->
                    y = StringUtil.colorize(y.replace("{player}", pl.getName()))).toArray(String[]::new)));
        }, 0L, getConfig().getInt("GUI.FAKE_CHANCE.UPDATE_TIME") * 20);
    }
    
    public void reloadCases() {
        this.cases.clear();
        this.loadCases();
    }
    
    public void loadCases() {
        for (final String caseName : this.getConfig().getConfigurationSection("CASES").getValues(false).keySet()) {
            final List<String> itemsName = new ArrayList<>(getConfig().getConfigurationSection("CASES." + caseName + ".ITEMS").getValues(false).keySet());
            final Item[] items = new Item[itemsName.size()];
            for (int index = 0; index < itemsName.size(); index++) {
                final String[] commands = new String[this.getConfig().getStringList("CASES." + caseName + ".ITEMS." + itemsName.get(index) + ".COMMANDS").size()];
                for (int command = 0; command < commands.length; ++command) {
                    commands[command] = this.getConfig().getStringList("CASES." + caseName + ".ITEMS." + itemsName.get(index) + ".COMMANDS").get(command);
                }
                final ItemType type = ItemType.valueOf(this.getConfig().getString("CASES." + caseName + ".ITEMS." + itemsName.get(index) + ".TYPE"));
                String group = null;
                if (type == ItemType.GROUP) {
                    group = this.getConfig().getString("CASES." + caseName + ".ITEMS." + itemsName.get(index) + ".GROUP");
                }
                items[index] = new Item(StringUtil.colorize(this.getConfig().getString("CASES." + caseName + ".ITEMS." + itemsName.get(index) + ".TITLE")), commands, new ItemBuilder(this.getConfig().getString("CASES." + caseName + ".ITEMS." + itemsName.get(index) + ".ID")).build(), group, this.getConfig().getString("CASES." + caseName + ".ITEMS." + itemsName.get(index) + ".GROUP_COMMAND"), this.getConfig().getInt("CASES." + caseName + ".ITEMS." + itemsName.get(index) + ".CHANCE"), type);
            }
            cases.add(new Case(items, caseName, Sound.valueOf(this.getConfig().getString("CASES." + caseName + ".ANIMATION_SETTINGS.ROTATE_SOUND")), Sound.valueOf(this.getConfig().getString("CASES." + caseName + ".ANIMATION_SETTINGS.END_SOUND")), this.getConfig().getDouble("CASES." + caseName + ".ANIMATION_SETTINGS.ITEMS_SPEED"), this.getConfig().getDouble("CASES." + caseName + ".ANIMATION_SETTINGS.PARTICLES_SPEED"), this.getConfig().getInt("CASES." + caseName + ".ANIMATION_SETTINGS.ROTATE_TIME"), this.getConfig().getDouble("CASES." + caseName + ".ANIMATION_SETTINGS.RADIUS"), this.getConfig().getBoolean("CASES." + caseName + ".ANIMATION_SETTINGS.USE_FROSTLORD")));
        }
        if (getInstance().getConfig().getStringList("CASES_LOCATIONS") != null) {
            for (final String location : getInstance().getConfig().getStringList("CASES_LOCATIONS")) {
                final String[] locat = location.split(";");
                final Location loc = new Location(Bukkit.getWorld(locat[0]), Integer.parseInt(locat[1]), Integer.parseInt(locat[2]), Integer.parseInt(locat[3]));
                final Hologram hd = HologramsAPI.createHologram(this, loc.clone().add(0.5, 2.0, 0.5));
                for (final String line : getInstance().getConfig().getStringList("DEFAULT_HOLOGRAM")) {
                    hd.appendTextLine(StringUtil.colorize(line));
                }
                this.opened.put(loc, hd);
                this.casesLocations.put(loc, locat[4].equalsIgnoreCase("x"));
            }
        }
    }
}
