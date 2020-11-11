package ru.servbuy.donatecases.gui;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.servbuy.donatecases.Main;
import ru.servbuy.donatecases.structure.Case;
import ru.servbuy.donatecases.userdata.UserData;
import ru.servbuy.donatecases.utils.StringUtil;

import java.util.Arrays;
import java.util.List;

public class InventoryManager
{
    public static void open(final Player p, final Location loc) {
        final GUIBuilder gui = new GUIBuilder(Main.getInstance().getConfig().getInt("GUI.ROWS"), Main.getInstance().getConfig().getString("GUI.TITLE"));
        for (final String section : Main.getInstance().getConfig().getConfigurationSection("GUI.DEFAULT_ITEMS").getValues(false).keySet()) {
            gui.setItem(Main.getInstance().getConfig().getInt("GUI.DEFAULT_ITEMS." + section + ".SLOT"),
                    new ItemBuilder(Main.getInstance().getConfig().getString("GUI.DEFAULT_ITEMS." + section + ".ID"))
                            .name(Main.getInstance().getConfig().getString("GUI.DEFAULT_ITEMS." + section + ".NAME"))
                            .setLore(p, Main.getInstance().getConfig().getStringList("GUI.DEFAULT_ITEMS." + section + ".LORE"))
                            .amount(Main.getInstance().getConfig().getInt("GUI.DEFAULT_ITEMS." + section + ".AMOUNT"))
                            .build());
        }
        FillWinHistory(gui);
        final String[] fakeMessage = Main.getInstance().getConfig().getStringList("MESSAGES.FAKE_MESSAGE").toArray(new String[0]);
        final String[] fakeUse = Main.getInstance().getConfig().getStringList("MESSAGES.FAKE_USE").toArray(new String[0]);
        gui.setItem(Main.getInstance().getConfig().getInt("GUI.FAKE_CHANCE.SLOT"),
                new ItemBuilder(Main.getInstance().getConfig().getString("GUI.FAKE_CHANCE.ID"))
                        .name(Main.getInstance().getConfig().getString("GUI.FAKE_CHANCE.NAME"))
                        .setLore(p, Main.getInstance().getConfig().getStringList("GUI.FAKE_CHANCE.LORE"))
                        .build(),
                () -> {
                if (Main.getInstance().getCache().contains(p.getName())) {
                    Arrays.stream(fakeMessage).forEach(msg ->
                            p.sendMessage(StringUtil.colorize(msg.replace("{player}", p.getName()))));
                }
                else {
                    Main.getInstance().getCache().add(p.getName());
                    Arrays.stream(fakeUse).forEach(msg ->
                            p.sendMessage(StringUtil.colorize(msg.replace("{player}", p.getName()))));
                }
            p.closeInventory();
                });
        addCases(p, gui, loc);
        gui.open(p);
    }

    private static void FillWinHistory(GUIBuilder gui) {
    int rows = Main.getInstance().getConfig().getInt("GUI.ROWS");
    List<String> prizes = Main.getInstance().getConfig().getStringList("HISTORY.PRIZES");
    for (int slot = 9 * (rows - 1); slot < 9 * (rows - 1) + prizes.size(); slot++) {
            String history = prizes.get(slot % 9);
            String playerName = history.split(";")[0];
            String prize = history.split(";")[1];
            String date = history.split(";")[2];
            String time = history.split(";")[3];
            gui.setItem(slot,
                    new ItemBuilder("397:3;" + playerName)
                            .name(Main.getInstance().getConfig().getString("HISTORY.FORMAT.NAME")
                                    .replace("{player}",playerName))
                            .setLore(Main.getInstance().getConfig().getStringList("HISTORY.FORMAT.LORE"))
                            .replaceLore("{player}", playerName)
                            .replaceLore("{prize}", prize)
                            .replaceLore("{date}", date)
                            .replaceLore("{time}", time)
                            .build());
        }
    }

    private static void addCases(final Player p, final GUIBuilder gui, final Location loc) {
        for (final String section : Main.getInstance().getConfig().getConfigurationSection("GUI.CASES_ITEMS").getValues(false).keySet()) {
            gui.setItem(Main.getInstance().getConfig().getInt("GUI.CASES_ITEMS." + section + ".SLOT"),
                    new ItemBuilder(Main.getInstance().getConfig().getString("GUI.CASES_ITEMS." + section + ".ID"))
                            .name(Main.getInstance().getConfig().getString("GUI.CASES_ITEMS." + section + ".NAME"))
                            .setLore(p, Main.getInstance().getConfig().getStringList("GUI.CASES_ITEMS." + section + ".LORE"))
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
