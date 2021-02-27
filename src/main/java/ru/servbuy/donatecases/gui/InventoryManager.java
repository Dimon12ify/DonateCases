package ru.servbuy.donatecases.gui;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.servbuy.donatecases.Main;
import ru.servbuy.donatecases.structure.Case;
import ru.servbuy.donatecases.userdata.UserData;
import ru.servbuy.donatecases.utils.StringUtil;

import java.util.List;

public class InventoryManager
{
    public static void open(final Player p, final Location loc) {
        final GUIBuilder gui = new GUIBuilder(Main.getInstance().getConfig().getInt("gui.rows"),
                Main.getInstance().getConfig().getString("gui.title"));
        for (final String section : Main.getInstance().getConfig().getConfigurationSection("gui.default-items").getValues(false).keySet()) {
            gui.setItem(Main.getInstance().getConfig().getInt("gui.default-items." + section + ".slot"),
                    new ItemBuilder(Main.getInstance().getConfig().getString("gui.default-items." + section + ".id"))
                            .name(Main.getInstance().getConfig().getString("gui.default-items." + section + ".name"))
                            .setLore(p, Main.getInstance().getConfig().getStringList("gui.default-items." + section + ".lore"))
                            .amount(Main.getInstance().getConfig().getInt("gui.default-items." + section + ".amount"))
                            .build());
        }
        FillWinHistory(gui);
        addCases(p, gui, loc);
        gui.open(p);
    }

    private static void FillWinHistory(GUIBuilder gui) {
    int rows = Main.getInstance().getConfig().getInt("gui.rows");
    List<String> prizes = Main.getInstance().getConfig().getStringList("history.prizes");
    for (int slot = 9 * (rows - 1); slot < 9 * (rows - 1) + prizes.size(); slot++) {
            String history = prizes.get(slot % 9);
            String playerName = history.split(";")[0];
            String prize = history.split(";")[1];
            String date = history.split(";")[2];
            String time = history.split(";")[3];
            gui.setItem(slot,
                    new ItemBuilder("397:3;" + playerName)
                            .name(Main.getInstance().getConfig().getString("history.format.name")
                                    .replace("{player}",playerName))
                            .setLore(Main.getInstance().getConfig().getStringList("history.format.lore"))
                            .replaceLore("{player}", playerName)
                            .replaceLore("{prize}", prize)
                            .replaceLore("{date}", date)
                            .replaceLore("{time}", time)
                            .build());
        }
    }

    private static void addCases(final Player p, final GUIBuilder gui, final Location loc) {
        for (final String section : Main.getInstance().getConfig().getConfigurationSection("gui.case-items").getValues(false).keySet()) {
            gui.setItem(Main.getInstance().getConfig().getInt("gui.case-items." + section + ".slot"),
                    new ItemBuilder(Main.getInstance().getConfig().getString("gui.case-items." + section + ".id"))
                            .name(Main.getInstance().getConfig().getString("gui.case-items." + section + ".name"))
                            .setLore(p, Main.getInstance().getConfig().getStringList("gui.case-items." + section + ".lore"))
                            .replaceCasesVariables(p, section)
                            .build()
                    , () -> openCase(p, section, loc));
        }
    }
    
    private static void openCase(final Player p, final String caseName, final Location loc) {
        final Case donateCase = Main.getInstance().getCase(caseName);
        if (donateCase == null) {
            p.sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.CASE_NOT_LOADED").replace("{player}", p.getName()).replace("{case}", caseName)));
            return;
        }
        if (UserData.getCases(p.getName(), caseName) < 1) {
            p.sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.NO_CASES").replace("{player}", p.getName()).replace("{case}", caseName)));
            return;
        }
        p.closeInventory();
        UserData.removeCases(p.getName(), caseName, 1);
        donateCase.open(p, loc);
    }
}
