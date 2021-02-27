package ru.servbuy.donatecases.gui;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class IHolder implements InventoryHolder
{
    @Setter(AccessLevel.PUBLIC)
    private Map<Integer, ClickHolder> clicks;
    @Setter(AccessLevel.PUBLIC)
    @Getter
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
}
