package ru.servbuy.donatecases.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import ru.servbuy.donatecases.userdata.UserData;
import ru.servbuy.donatecases.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemBuilder
{
    private final ItemStack item;
    
    public ItemBuilder(final String full_id) {
        ItemStack stack = null;
        String id = full_id;
        String skull_texture = "";
        if (full_id.contains(";")) {
            skull_texture = full_id.split(";")[1];
            id = full_id.split(";")[0];
        }
        if (id.contains(":")) {
            if (id.split(":")[0].equalsIgnoreCase("373")) {
                stack = new ItemStack(Material.POTION);
                final Potion potion = new Potion(PotionType.getByEffect(PotionEffectType.getById(Integer.parseInt(id.split(":")[1]))));
                potion.setSplash(true);
                potion.apply(stack);
            }
            else if (id.split(":")[0].equalsIgnoreCase("397") && id.split(":")[1].equalsIgnoreCase("3")) {
                stack = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
                final SkullMeta headMeta = (SkullMeta)stack.getItemMeta();
                if (skull_texture.length() > 16) {
                    final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                    profile.getProperties().put("textures", new Property("textures", skull_texture));
                    try {
                        final Field profileField = headMeta.getClass().getDeclaredField("profile");
                        profileField.setAccessible(true);
                        profileField.set(headMeta, profile);
                    }
                    catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    headMeta.setOwner(skull_texture);
                }
                stack.setItemMeta(headMeta);
            }
            else {
                stack = new ItemStack(Material.getMaterial(Integer.valueOf(id.split(":")[0])), 1, Byte.parseByte(id.split(":")[1]));
            }
        }
        else {
            stack = new ItemStack(Material.getMaterial(Integer.valueOf(id)));
        }
        this.item = stack;
    }
    
    public ItemBuilder name(final String name) {
        final ItemMeta itm = this.item.getItemMeta();
        itm.setDisplayName(StringUtil.colorize(name));
        this.item.setItemMeta(itm);
        return this;
    }
    
    public ItemBuilder setLore(final List<String> ls) {
        final ItemMeta itm = this.item.getItemMeta();
        final List<String> lore = new ArrayList<>();
        for (final String line : ls) {
            lore.add(StringUtil.colorize(line));
        }
        itm.setLore(lore);
        this.item.setItemMeta(itm);
        return this;
    }
    
    public ItemBuilder setLore(final Player p, final List<String> ls) {
        final List<String> lore = new ArrayList<>();
        for (final String line : ls) {
            lore.add(line.replace("{player}", p.getName()));
        }
        this.setLore(lore);
        return this;
    }
    
    public ItemBuilder amount(final int amount) {
        this.item.setAmount(amount);
        return this;
    }
    
    public ItemBuilder replaceCasesVariables(final Player p, String name) {
        final List<String> lore = this.item.getItemMeta().getLore();
        final List<String> formatedLore = new ArrayList<>();
        for (String s : lore) {
            if (s.contains("{keys}")) {
                s = s.replace("{keys}", String.valueOf(UserData.getCases(p.getName(), name)));
            }
            formatedLore.add(s);
        }
        this.setLore(formatedLore);
        return this;
    }

    public ItemBuilder replaceLore(final String in, final String out) {
        final List<String> lore = item.getItemMeta().getLore();
        this.setLore(lore.stream().map(x -> x = x.replace(in, out)).collect(Collectors.toList()));
        return this;
    }
    
    public ItemStack build() {
        return this.item;
    }
}
