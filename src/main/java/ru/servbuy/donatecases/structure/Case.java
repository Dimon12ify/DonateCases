package ru.servbuy.donatecases.structure;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ru.servbuy.donatecases.Main;
import ru.servbuy.donatecases.utils.StringUtil;

public class Case {
    @Getter private final Item[] items;
    @Getter private final String name;
    @Getter private final AnimSettings animSettings;

    public Case(final Item[] items, final String name, AnimSettings animSettings) {
        this.items = items;
        this.name = name;
        this.animSettings = animSettings;
    }

    public void open(final Player p, final Location block_location) {
        if (Main.getInstance().getHologram(block_location) != null) {
            Main.getInstance().getHologram(block_location).getVisibilityManager().setVisibleByDefault(false);
        }
        Bukkit.getOnlinePlayers().stream().filter(OfflinePlayer::isOnline)
                .forEach(player -> player.sendMessage(
                        StringUtil.colorize(Main.getInstance().getConfig()
                                .getStringList("cases." + this.name + ".open-message"), p.getName())));
        new CaseRunnable(this, block_location, p)
                .runTaskTimer(Main.getInstance(), 0 , 1L);
    }
}
