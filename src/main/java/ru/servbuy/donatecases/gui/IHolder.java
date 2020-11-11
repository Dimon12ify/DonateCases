package ru.servbuy.donatecases.gui;

import org.bukkit.inventory.*;

import java.util.*;

public class IHolder implements InventoryHolder
{
    private Map<Integer, ClickHolder> clicks;
    private Inventory inv;
    
    public IHolder() {
        this.clicks = new HashMap<>();
    }
    
    public Inventory getInventory() {
        return this.inv;
    }
    
    public void click(final int slot) {
        if (this.clicks.containsKey(slot)) {
            this.clicks.get(slot).click();
        }
    }
    
    public void setInv(final Inventory inv) {
        this.inv = inv;
    }
    
    public void setClicks(final HashMap<Integer, ClickHolder> clics) {
        this.clicks = clics;
    }
}
