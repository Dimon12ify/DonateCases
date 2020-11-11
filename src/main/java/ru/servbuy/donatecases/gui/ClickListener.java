package ru.servbuy.donatecases.gui;

import org.bukkit.event.inventory.*;
import org.bukkit.event.*;

public class ClickListener implements Listener
{
    @EventHandler
    public void onClick(final InventoryClickEvent e) {
        if (e.getClickedInventory() == null || !(e.getClickedInventory().getHolder() instanceof IHolder)) {
            return;
        }
        e.setCancelled(true);
        try {
            ((IHolder)e.getClickedInventory().getHolder()).click(e.getSlot());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
