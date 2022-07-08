package me.mike3132.lootcrates.EventHandler;

import me.mike3132.lootcrates.ItemManager.NormalCrate;
import me.mike3132.lootcrates.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NormalCrateEvents implements Listener {
    private final Main plugin;

    public NormalCrateEvents(Main plugin) {
        this.plugin = plugin;
    }

    private static List<UUID> playerCooldown = new ArrayList<>();

    public static List<Location> lootLocation = new ArrayList<>();



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPlace(BlockPlaceEvent bb) {
        Player player = bb.getPlayer();
        Block block = bb.getBlock();
        Location playerLocation = player.getLocation();
        Location chestLocation = block.getLocation();
        int i = 0;
        NormalCrate normalCrate = new NormalCrate(this.plugin);
        ItemStack crate = normalCrate.getCrate();

        if (!(bb.isCancelled())) {
            if (player.getInventory().getItemInMainHand().isSimilar(crate)) {

                playerLocation.getWorld().playEffect(playerLocation, Effect.END_GATEWAY_SPAWN, 1);

                Firework firework = chestLocation.getWorld().spawn(chestLocation, Firework.class);
                FireworkMeta data = (FireworkMeta) firework.getFireworkMeta();
                data.addEffect(FireworkEffect.builder().withColor(Color.FUCHSIA).with(FireworkEffect.Type.BALL_LARGE).withFlicker().build());
                data.setPower(0);
                firework.setFireworkMeta(data);

                chestLocation.getWorld().spawnParticle(Particle.REDSTONE, chestLocation.add(1, 0, 0), 10, new Particle.DustOptions(Color.RED, 5));
                chestLocation.getWorld().spawnParticle(Particle.REDSTONE, chestLocation.add(-1, 0, 0), 10, new Particle.DustOptions(Color.BLUE, 5));
                chestLocation.getWorld().spawnParticle(Particle.REDSTONE, chestLocation.add(0, 0, 1), 10, new Particle.DustOptions(Color.LIME, 5));
                chestLocation.getWorld().spawnParticle(Particle.REDSTONE, chestLocation.add(1, 0, 0), 10, new Particle.DustOptions(Color.ORANGE, 5));
                i++;

                block.setType(Material.AIR);


                FallingBlock fallingBlock = chestLocation.getWorld().spawnFallingBlock(chestLocation.add(-1, 25, -1), Material.BEACON.createBlockData());


                if (!(playerCooldown.contains(player.getUniqueId()))) {
                    playerCooldown.add(player.getUniqueId());
                    player.sendMessage(Main.chatColor("" + this.plugin.getConfig().getString("Prefix") + "&6&lYou placed a crate"));
                } else {
                    player.sendTitle(Main.chatColor("&4You are on cool down"), Main.chatColor("&2Enjoy some fireworks"), 10, 10, 10);
                    playerLocation.getWorld().playEffect(playerLocation, Effect.ANVIL_BREAK, 1);
                    bb.setCancelled(true);
                    fallingBlock.remove();
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        if (playerCooldown.contains(player.getUniqueId())) {
                            playerCooldown.remove(player.getUniqueId());
                            String message = Main.chatColor("&a&l[&2&lCooldown Removed&a&l]");
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                        }
                    }
                }, 50L);

                Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
                    public void run() {
                        if (block.getType().equals(Material.BEACON)) {
                            block.setType(Material.CHEST);
                            block.getLocation().getWorld().spawnParticle(Particle.LAVA, block.getLocation(), 100);
                            if (!(lootLocation.contains(chestLocation))) {
                                lootLocation.add(block.getLocation());
                                player.sendMessage(Main.chatColor("&4LootCrates &f-> &2&lYour loot is being generated"));
                                player.sendMessage(Main.chatColor("&4LootCrates &f-> &6Click the chest to get your loot"));
                            }
                        }
                    }
                }, 45L);
            }
        } else {
            player.sendMessage(Main.chatColor("" + this.plugin.getConfig().getString("Prefix") + "&4&lError: &cAirdrop canceled because you can't place blocks here"));
        }
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent pic) {
        if (pic.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = pic.getClickedBlock();
            Player player = pic.getPlayer();
            Location blockLocation = block.getLocation();

            if (block.getType().equals(Material.CHEST)) {
                if (lootLocation.contains(blockLocation)) {
                    lootLocation.remove(blockLocation);


                    for (String item : this.plugin.getConfig().getConfigurationSection("NormalLoot.items").getKeys(false)) {
                        int random_number = (int) (Math.random() * 2) + 1;
                        if (random_number == 1) {
                            int randomAmount = (int) (Math.random() * this.plugin.getConfig().getInt("NormalLoot.items." + item)) + 1;
                            ItemStack items = new ItemStack(Material.getMaterial(item.toUpperCase()), randomAmount);

                            Chest chest = (Chest) block.getState();
                            chest.getBlockInventory().addItem(items);

                        }
                    }
                    for (String command : this.plugin.getConfig().getStringList("NormalLoot.commands")) {
                        int random_command = (int) (Math.random() * 2) + 1;
                        if (random_command == 1) {
                            command = command.replace("%player%", player.getName());
                            Bukkit.dispatchCommand(plugin.getServer().getConsoleSender(), command);
                            break;
                        }
                    }
                }
            }
        }

    }
}
