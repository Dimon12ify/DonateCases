package ru.servbuy.donatecases.listeners;

import ru.servbuy.donatecases.Main;
import ru.servbuy.donatecases.gui.InventoryManager;
import ru.servbuy.donatecases.utils.StringUtil;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.event.*;

public class EventListener implements Listener
{
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInteract(final PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            for (final Location loc : Main.getInstance().getCasesLocations()) {
                if (e.getClickedBlock().getLocation().getBlockX() == loc.getBlockX() && e.getClickedBlock().getLocation().getBlockY() == loc.getBlockY() && e.getClickedBlock().getLocation().getBlockZ() == loc.getBlockZ()) {
                    e.setCancelled(true);
                    if (!Main.getInstance().getHologram(e.getClickedBlock().getLocation()).getVisibilityManager().isVisibleByDefault()) {
                        e.getPlayer().sendMessage(StringUtil.colorize(Main.getInstance().getConfig().getString("MESSAGES.CASE_IS_OPEN").replace("{player}", e.getPlayer().getName())));
                    }
                    else {
                        InventoryManager.open(e.getPlayer(), loc);
                    }
                }
            }
        }
    }
}
