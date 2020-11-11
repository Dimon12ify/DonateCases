package ru.servbuy.donatecases.structure;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.servbuy.donatecases.Main;
import ru.servbuy.donatecases.utils.StringUtil;

public class Case {
    @Getter private final Item[] items;
    @Getter private final String name;
    @Getter private final Sound rotateSound;
    @Getter private final Sound endSound;
    @Getter private final double speed;
    @Getter private final double particlesSpeed;
    @Getter private final int rotateTime;
    @Getter private final double radius;
    @Getter private final boolean frostlordUse;

    public Case(final Item[] items, final String name, final Sound rotateSound, final Sound endSound, final double speed, final double particlesSpeed, final int rotateTime, final double radius, final boolean frostlordUse) {
        this.items = items;
        this.name = name;
        this.rotateSound = rotateSound;
        this.endSound = endSound;
        this.speed = speed;
        this.particlesSpeed = particlesSpeed;
        this.rotateTime = rotateTime;
        this.radius = radius;
        this.frostlordUse = frostlordUse;
    }

    public void open(final Player p, final Location block_location) {
        if (Main.getInstance().getHologram(block_location) != null) {
            Main.getInstance().getHologram(block_location).getVisibilityManager().setVisibleByDefault(false);
        }
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (player != null && player.isOnline()) {
                for (final String line : Main.getInstance().getConfig().getStringList("CASES." + this.name + ".OPEN_MESSAGES")) {
                    player.sendMessage(StringUtil.colorize(line.replace("{player}", p.getName())));
                }
            }
        }
        new CaseRunnable(this, block_location, p)
                .runTaskTimer(Main.getInstance(), 0 , 1L);
    }
}
