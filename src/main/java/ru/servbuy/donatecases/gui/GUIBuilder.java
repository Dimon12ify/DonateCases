package ru.servbuy.donatecases.gui;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.servbuy.donatecases.utils.StringUtil;

import java.util.HashMap;

public class GUIBuilder
{
    @Getter private final Inventory inventory;
    private final HashMap<Integer, ClickHolder> clicks;
    
    public GUIBuilder(final int rows, final String title) {
        this.clicks = new HashMap<>();
        this.inventory = Bukkit.createInventory(new IHolder(), rows * 9, StringUtil.colorize(title));
    }
    
    public void setItem(final int slot, final ItemStack item, final ClickHolder click) {
        this.inventory.setItem(slot, item);
        if (click != null) {
            this.clicks.put(slot, click);
            ((IHolder)this.inventory.getHolder()).setClicks(this.clicks);
        }
    }
    
    public void setItem(final int slot, final ItemStack item) {
        this.inventory.setItem(slot, item);
    }
    
    public void open(final Player p) {
        p.closeInventory();
        p.openInventory(this.inventory);
    }
}
