package ru.servbuy.donatecases.structure;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.servbuy.donatecases.Main;
import ru.servbuy.donatecases.utils.MathUtil;
import ru.servbuy.donatecases.utils.RunCommand;
import ru.servbuy.donatecases.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class CaseRunnable extends BukkitRunnable {
    private String name;
    private double radius;
    private boolean frostlordUse;
    private final boolean directionX;
    private Player p;
    double t;
    Location loc;
    Location frostlord;
    List<Hologram> Holograms;
    int time;
    int endTime;
    int offset;
    int particlesoffset;
    int tick;
    double m;
    boolean cancelRotate;
    Hologram winHologram;
    Item winItem;
    Case donatecase;

    public CaseRunnable (Case donatecase, Location caseLocation, Player p)
    {
        this.name = donatecase.getName();
        this.donatecase = donatecase;
        this.radius = donatecase.getRadius();
        this.frostlordUse = donatecase.isFrostlordUse();
        this.directionX =  Main.getInstance().getDirection(caseLocation);
        this.p = p;
        loc = caseLocation.clone().add(0.5, 0.0, 0.5);
        Holograms = Lists.newArrayList();
        endTime = -1;
        m = 1.0;
        frostlord = loc.clone().add(0.0, -1.0, 0.0);
        cancelRotate = false;
        winItem = MathUtil.getWinItem(donatecase.getItems());
    }

    public void run() {
        if (frostlordUse) {
            t += 0.19634954084936207;
            for (double phi = 0.0; phi <= 2 * Math.PI; phi += 1.5707963267948966) {
                final double x = 0.2 * (4 * Math.PI - t) * Math.cos(t + phi);
                final double y = 0.2 * t;
                final double z = 0.2 * (4 * Math.PI - t) * Math.sin(t + phi);
                frostlord.add(x, y, z);
                frostlord.getWorld().spigot().playEffect(frostlord, Effect.SNOW_SHOVEL, 0, 0, 0.0f, 0.0f, 0.0f, 1.0f, 0, 50);
                frostlord.subtract(x, y, z);
                if (t >= 4 * Math.PI) {
                    frostlord.add(x, y, z);
                    frostlord.getWorld().spigot().playEffect(frostlord, Effect.SNOW_SHOVEL, 0, 0, 0.0f, 0.0f, 0.0f, 1.0f, 25, 50);
                    frostlordUse = false;
                    return;
                }
            }
        } else {
            if (tick >= (2 + 2*(1-m))/m && !cancelRotate) {
                if (Holograms.stream().anyMatch(x -> Math.abs(x.getY() - (loc.getY() + 1.0 + radius)) <= 0.095)) {
                    loc.getWorld().playSound(loc, donatecase.getRotateSound(), 1.0f, 1.0f);
                    this.loc.getWorld().playEffect(loc.clone().add(0.0, radius + 0.25, 0.0), Effect.HEART, 10);
                }
                tick = 0;
            }
            if (this.endTime == -2) {
                endTime = time + 1;
            }
            int index = 0;
            final double c1 = Math.toRadians(particlesoffset % 360.0);
            for (double d = 0.0; d < 2 * Math.PI; d += Math.PI / 2) {
                Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.fromRGB(0, 252, 0), Color.AQUA};
                final double a = d + c1;
                final double cos = Math.cos(a);
                final double sin = Math.sin(a);
                Color color = colors[index % 4];
                ++index;
                final float r = (float) (color.getRed() * 2 / 255.0 - 1.0);
                final float g = (float) (color.getGreen() * 2 / 255.0 - 1.0);
                final float b = (float) (color.getBlue() * 2 / 255.0 - 1.0);
                final Location particle = directionX
                        ? loc.clone().add(0.0, sin * radius + 0.5, cos * radius)
                        : loc.clone().add(cos * radius, sin * radius + 0.5, 0.0);
                this.loc.getWorld().spigot().playEffect(particle, Effect.COLOURED_DUST, 0, 1, r, g, b, 1.0f, 0, 20);
                this.loc.getWorld().spigot().playEffect(particle, Effect.COLOURED_DUST, 0, 1, r, g, b, 1.0f, 0, 20);
            }
            particlesoffset += (int) donatecase.getParticlesSpeed();
            if (!cancelRotate && Holograms.size() < donatecase.getItems().length
                    && Math.toRadians(offset) >= 2 * Math.PI / donatecase.getItems().length * Holograms.size()) {
                final Hologram hologram = HologramsAPI.createHologram(Main.getInstance(), loc.clone().add(0.0, 0.5, 0.0));
                Item item = donatecase.getItems()[Holograms.size()];
                hologram.appendTextLine(item.getTitle());
                hologram.appendItemLine(item.getIcon());
                Holograms.add(hologram);
                winHologram = item.equals(winItem)? hologram : winHologram;
                return;
            }
            if (!cancelRotate) {
                int currentItemIndex = 0;
                final double cO = Math.toRadians(offset % 360.0);
                for (double d2 = 0.0; d2 < 2 * Math.PI && currentItemIndex < this.Holograms.size(); d2 += 2 * Math.PI / donatecase.getItems().length) {
                    final double a2 = d2 + cO;
                    final double cos2 = Math.cos(a2);
                    final double sin2 = Math.sin(a2);
                    final Hologram hologram = Holograms.get(currentItemIndex++);
                    final Location holoLocation = directionX
                            ? loc.clone().add(0.0, sin2 * radius + 1.0, cos2 * radius)
                            : loc.clone().add(cos2 * radius, sin2 * radius + 1.0, 0.0);
                    hologram.teleport(holoLocation);
                }
                if (Holograms.size() == donatecase.getItems().length) {
                    endTime = endTime == -1 ? time + donatecase.getRotateTime() * 20 : endTime;
                    if (time >= endTime) {
                        if (winHologram.getLocation().clone().distance(loc.clone().add(0.0, radius + 1.0, 0.0)) <= 0.05) {
                            cancelRotate = true;
                            winHologram.teleport(loc.clone().add(0.0, radius + 1.0, 0.0));
                            loc.getWorld().playSound(this.loc, donatecase.getEndSound(), 1.0f, 1.0f);
                            if (this.winItem.getType() == ItemType.GROUP) {
                                final String group = Bukkit.getServicesManager().getRegistration(Permission.class).getProvider().getPrimaryGroup(p);
                                if (Main.getInstance().getConfig().getBoolean("CASES." + name + ".LEVEL_GROUPS.ENABLE")
                                        && Main.getInstance().getConfig().getInt("CASES." + name + ".LEVEL_GROUPS.LEVELS." + group)
                                        < Main.getInstance().getConfig().getInt("CASES." + name + ".LEVEL_GROUPS.LEVELS." + winItem.getGroup())) {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), winItem.getGroupCommand()
                                            .replace("{player}", p.getName())
                                            .replace("{group}", winItem.getGroup()));
                                }
                            }
                            RunCommand.run(p, winItem.getCommands());
                            final List<String> winHistory = new ArrayList<>();
                            winHistory.add(p.getName() + ";" + winItem.getTitle() + ";" + TimeUtil.getDate() + ";" + TimeUtil.getTime());
                            for (final String prize : Main.getInstance().getConfig().getStringList("HISTORY.PRIZES")) {
                                winHistory.add(prize);
                                if (winHistory.size() == 9) {
                                    break;
                                }
                            }
                            Main.getInstance().getConfig().set("HISTORY.PRIZES", winHistory);
                            Main.getInstance().saveConfig();
                            Main.getInstance().reloadConfig();
                            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                                Holograms.forEach(x -> {x.teleport(loc); x.delete();});
                                this.cancel();
                                Main.getInstance().getHologram(loc.add(-0.5, 0, -0.5)).getVisibilityManager().setVisibleByDefault(true);
                            }, 60L);
                            return;
                        }
                        endTime = time + 1;
                    }
                    final int remaining = endTime - time;
                    m = (40.0 + remaining) / 200.0;
                }
                this.offset += (int) (donatecase.getSpeed() * m);
                ++time;
            }
            ++tick;
        }
    }
}
