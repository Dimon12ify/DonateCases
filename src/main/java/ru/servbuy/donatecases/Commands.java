package ru.servbuy.donatecases;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.servbuy.donatecases.title.Title;
import ru.servbuy.donatecases.userdata.UserData;
import ru.servbuy.donatecases.utils.StringUtil;

import java.io.File;
import java.util.List;

public class Commands implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("cases.admin")) {
            sender.sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.NO_PERMISSIONS").replace("{player}", sender.getName())));
            return true;
        }
        if (args.length == 0) {
            for (final String line : Main.getInstance().getConfig().getStringList("MESSAGES.HELP")) {
                sender.sendMessage(StringUtil.colorize(line.replace("{player}", sender.getName())));
            }
            return true;
        }
        switch (args[0]) {
            case "delete": {
                if (args.length < 4) {
                    sender.sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.COMMAND_DELETE_USE").replace("{player}", sender.getName())));
                    break;
                }
                if (!StringUtils.isNumeric(args[3])) {
                    sender.sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.NOT_NUMERIC").replace("{player}", sender.getName())));
                    break;
                }
                UserData.removeCases(args[1], args[2], Integer.parseInt(args[3]));
                sender.sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.COMMAND_DELETE_SUCCESS").replace("{player}", args[1]).replace("{case}", args[2]).replace("{amount}", args[3])));
                break;
            }
            case "set": {
                if (args.length < 4) {
                    sender.sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.COMMAND_SET_USE").replace("{player}", sender.getName())));
                    break;
                }
                if (!StringUtils.isNumeric(args[3])) {
                    sender.sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.NOT_NUMERIC").replace("{player}", sender.getName())));
                    break;
                }
                UserData.setCases(args[1], args[2], Integer.parseInt(args[3]));
                sender.sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.COMMAND_SET_SUCCESS").replace("{player}", args[1]).replace("{case}", args[2]).replace("{amount}", args[3])));
                Bukkit.getPlayer(args[1]).sendMessage("&f[&ci&f] &b&n" + sender.getName() + "&f установил вам &c&l{amount} {case}&f кейсов".replace("{case}", args[2]).replace("{amount}", args[3]));
                Title.sendTitle(Bukkit.getPlayer(args[1]), 20, 40, 20,
                        StringUtil.colorize("&b&n" + sender.getName() + "&f установил вам"),
                        StringUtil.colorize("&c&l{amount} {case}&f кейсов".replace("{case}", args[2]).replace("{amount}", args[3])));
                break;
            }
            case "givekey": {
                if (args.length < 4) {
                    sender.sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.COMMAND_GIVE_USE").replace("{player}", sender.getName())));
                    break;
                }
                if (!StringUtils.isNumeric(args[3])) {
                    sender.sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.NOT_NUMERIC").replace("{player}", sender.getName())));
                    break;
                }
                UserData.addCases(args[1], args[2], Integer.parseInt(args[3]));
                sender.sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.COMMAND_GIVE_SUCCESS").replace("{player}", args[1]).replace("{case}", args[2]).replace("{amount}", args[3])));
                Bukkit.getPlayer(args[1]).sendMessage(StringUtil.colorize("&f[&ci&f] &b&n" + sender.getName() + "&f выдал вам &c&l{amount} {case}&f кейсов".replace("{case}", args[2]).replace("{amount}", args[3])));
                Title.sendTitle(Bukkit.getPlayer(args[1]), 20, 40, 20,
                        StringUtil.colorize("&b&n" + sender.getName() + "&f выдал вам"),
                        StringUtil.colorize("&c&l{amount} {case}&f кейсов".replace("{case}", args[2]).replace("{amount}", args[3])));
                break;
            }
            case "setcase": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("You are not player");
                    break;
                }
                if (args.length < 2) {
                    sender.sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.COMMAND_SETCASE_USE").replace("{player}", sender.getName())));
                    break;
                }
                final Player p = (Player)sender;
                final Location loc = p.getLocation().clone().add(0.0, -1.0, 0.0);
                final List<String> cases = Main.getInstance().getConfig().getStringList("case-locations");
                cases.add(loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ() + ";" + (args[1].equalsIgnoreCase("z") ? "z" : "x"));
                Main.getInstance().getConfig().set("case-locations", cases);
                Main.getInstance().saveConfig();
                Main.getInstance().reloadConfig();
                Main.getInstance().reloadStorage();
                Main.getInstance().reloadCases();
                sender.sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.COMMAND_SETCASE_SUCCESS").replace("{player}", sender.getName()).replace("{direction}", args[1])));
                break;
            }
            case "reload": {
                if (!new File(Main.getInstance().getDataFolder(), "config.yml").exists())
                    Main.getInstance().getConfig().options().copyDefaults(true);
                Main.getInstance().reloadConfig();
                Main.getInstance().reloadCases();
                Main.getInstance().reloadStorage();
                Main.getInstance().saveConfig();
                sender.sendMessage(ChatColor.GREEN + "Plugin was successfully reloaded");
            }
            default:
                break;
        }
        return false;
    }
}
