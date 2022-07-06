package me.mike3132.lootcrates;

import me.mike3132.lootcrates.Commands.GiveCommand;
import me.mike3132.lootcrates.EventHandler.NormalCrateEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static String chatColor(String chatColor) {
        return ChatColor.translateAlternateColorCodes('&', chatColor);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(chatColor("" + getConfig().getString("Prefix") + "&2&lENABLED"));

        //Event Registers
        Bukkit.getPluginManager().registerEvents(new NormalCrateEvents(this), this);

        //Command Register
        registerGive();

        //Config loader
        saveDefaultConfig();
        getConfig();
    }

    //Command Initializers
    public void registerGive() {
        new GiveCommand(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(chatColor("" + getConfig().getString("Prefix") + "&4&lDISABLED"));
    }
}
