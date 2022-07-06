package me.mike3132.lootcrates.Commands;

import me.mike3132.lootcrates.ItemManager.NormalCrate;
import me.mike3132.lootcrates.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand implements CommandExecutor {
    private final Main plugin;

    public GiveCommand(Main plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("LootCrate").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("LootCrate")) {

            if (sender.hasPermission("LootCrate.Give")) {
                if (args.length == 0) {
                    sender.sendMessage(Main.chatColor("" + this.plugin.getConfig().getString("Prefix") + "&2&lPlease use /lootcrate give"));
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("Give")) {
                        sender.sendMessage(Main.chatColor("" + this.plugin.getConfig().getString("Prefix") + "&2Now choose a player"));
                    } else {
                        sender.sendMessage(Main.chatColor("" + this.plugin.getConfig().getString("Prefix") + "&4Plese choose a player name"));
                    }
                }
                if (args.length == 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        sender.sendMessage(Main.chatColor("" + this.plugin.getConfig().getString("Prefix") + "&2Player found " + "&6Now choose a crate " + "&2Available crates &f= &eDefault"));
                    } else {
                        sender.sendMessage(Main.chatColor("" + this.plugin.getConfig().getString("Prefix") + "&4Player not found"));
                    }
                }
                if (args.length == 3) {
                    if (args[2].equalsIgnoreCase("Normal")) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            sender.sendMessage(Main.chatColor("" + this.plugin.getConfig().getString("Prefix") + "&2You have given " + target.getName() + " &aA default crate"));
                            NormalCrate normalCrate = new NormalCrate(this.plugin);
                            ItemStack crate = normalCrate.getCrate();
                            target.getInventory().addItem(crate);
                        } else {
                            sender.sendMessage(Main.chatColor("" + this.plugin.getConfig().getString("Prefix") + "&4Player not found"));
                        }
                    } else {
                        sender.sendMessage(Main.chatColor("" + this.plugin.getConfig().getString("Prefix") + "&4Crate not found"));
                    }
                }
            } else {
                sender.sendMessage(Main.chatColor("" + this.plugin.getConfig().getString("About")));
            }
            if (args.length ==1) {
                if (sender.hasPermission("LootCrate.Reload")) {
                    if (args[0].equalsIgnoreCase("Reload")) {
                        this.plugin.reloadConfig();
                        sender.sendMessage(Main.chatColor("" + this.plugin.getConfig().getString("Prefix") + "&2Config reloaded in " + String.valueOf(System.currentTimeMillis() - 1) + "ms"));
                    }
                } else {
                    sender.sendMessage(Main.chatColor("" + this.plugin.getConfig().getString("About")));
                }
            }

        }
        return false;
    }
}

