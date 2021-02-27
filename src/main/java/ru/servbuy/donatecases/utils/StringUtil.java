package ru.servbuy.donatecases.utils;

import org.bukkit.ChatColor;

import java.util.List;

public class StringUtil
{
    public static String colorize(final String text) {
        return ChatColor.translateAlternateColorCodes('&', "&f" + text);
    }

    public static String[] colorize(final List<String> strings, String playerName) {
        return strings.stream().map(str -> colorize(str.replace("{player}", playerName))).toArray(String[]::new);
    }
}
