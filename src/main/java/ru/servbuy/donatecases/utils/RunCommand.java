package ru.servbuy.donatecases.utils;

import ru.servbuy.donatecases.title.Title;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.command.*;

public class RunCommand
{
    public static void run(final Player p, final String[] commands) {
        for (String command : commands) {
            command = StringUtil.colorize(command.replace("{player}", p.getName())).substring(2);
            final String[] cmd = command.split(";");
            final String s;
            switch (s = cmd[0]) {
                case "bc": {
                    for (final Player all : Bukkit.getOnlinePlayers()) {
                        if (all != null && all.isOnline()) {
                            all.sendMessage(cmd[1]);
                        }
                    }
                    break;
                }
                case "op": {
                    if (p != null && p.isOnline()) {
                        p.setOp(true);
                        p.chat(cmd[1]);
                        p.setOp(false);
                        break;
                    }
                    break;
                }
                case "tm": {
                    if (p != null && p.isOnline()) {
                        Title.sendTitle(p, 20, 40, 20, cmd[1], cmd[2]);
                        break;
                    }
                    break;
                }
                case "tell": {
                    if (p != null && p.isOnline()) {
                        p.sendMessage(cmd[1]);
                        break;
                    }
                    break;
                }
                case "console": {
                    if (p != null && p.isOnline()) {
                        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), cmd[1]);
                        break;
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }
}
