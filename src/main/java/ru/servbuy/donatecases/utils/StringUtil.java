package ru.servbuy.donatecases.utils;

import org.bukkit.*;

public class StringUtil
{
    public static String colorize(final String text) {
        return ChatColor.translateAlternateColorCodes('&', "&f" + text);
    }
}
