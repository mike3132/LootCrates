package me.mike3132.lootcrates.ItemManager;

import me.mike3132.lootcrates.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class NormalCrate {

    private final Main plugin;
    private ItemStack crate;

    public NormalCrate(Main plugin) {
        this.plugin = plugin;
        this.createCrate();
    }

    public ItemStack getCrate() {
        return this.crate;
    }


    private void createCrate() {
        ItemStack item = new ItemStack(Material.CHEST, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        for (String realLore : this.plugin.getConfig().getStringList("Normal_Crate_Lore")) {
            lore.add(Main.chatColor("" + realLore));
        }

        meta.setDisplayName(Main.chatColor("" + this.plugin.getConfig().getString("Normal_Crate_Name")));
        meta.setLore(lore);
        item.setItemMeta(meta);
        crate = item;
    }

}
