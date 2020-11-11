package ru.servbuy.donatecases.structure;

import lombok.Getter;
import org.bukkit.inventory.*;

public class Item
{
    @Getter private final String title;
    @Getter private final String[] commands;
    @Getter private final ItemStack icon;
    @Getter private final String group;
    @Getter private final String groupCommand;
    @Getter private final int chance;
    @Getter private final ItemType type;
    
    public Item(final String title, final String[] commands, final ItemStack icon, final String group,
                final String groupCommand, final int chance, final ItemType type) {
        this.title = title;
        this.commands = commands;
        this.icon = icon;
        this.chance = chance;
        this.group = group;
        this.groupCommand = groupCommand;
        this.type = type;
    }
}
