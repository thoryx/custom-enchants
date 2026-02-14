package com.example.customenchants.commands;

import com.example.customenchants.registrar.EnchantmentRegistrar;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * /giveenchant <name> [level]
 * Applies a custom enchantment to the item in the player's main hand.
 */
public class GiveEnchantCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /giveenchant <lightning|lifesteal|shadow> [level]");
            return true;
        }

        String name = args[0].toLowerCase();
        Enchantment target = null;
        switch (name) {
            case "lightning":
            case "lightningedge":
            case "lightning_edge":
                target = EnchantmentRegistrar.LIGHTNING;
                break;
            case "lifesteal":
                target = EnchantmentRegistrar.LIFESTEAL;
                break;
            case "shadow":
            case "shadowstep":
            case "shadow_step":
                target = EnchantmentRegistrar.SHADOWSTEP;
                break;
            default:
                player.sendMessage(ChatColor.RED + "Unknown enchantment: " + name);
                return true;
        }

        int level = target.getStartLevel();
        if (args.length >= 2) {
            try {
                level = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
                player.sendMessage(ChatColor.RED + "Invalid level number.");
                return true;
            }
        }

        if (level < target.getStartLevel() || level > target.getMaxLevel()) {
            player.sendMessage(ChatColor.RED + "Level must be between " + target.getStartLevel() + " and " + target.getMaxLevel());
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "Hold an item in your main hand to enchant.");
            return true;
        }

        // Check whether the enchant can be applied to this item
        if (!target.canEnchantItem(item)) {
            player.sendMessage(ChatColor.RED + "That enchant cannot be applied to this item.");
            return true;
        }

        // Prevent incompatible vanilla enchants from combining
        for (Enchantment e : item.getEnchantments().keySet()) {
            if (target.conflictsWith(e) || e.conflictsWith(target)) {
                player.sendMessage(ChatColor.RED + "Cannot combine " + target.getName() + " with existing enchant: " + e.getName());
                return true;
            }
        }

        try {
            item.addEnchantment(target, level);
        } catch (IllegalArgumentException ex) {
            player.sendMessage(ChatColor.RED + "Failed to apply enchant: " + ex.getMessage());
            return true;
        }

        // Add custom lore line (preserve other lore)
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.hasLore() ? meta.getLore() : new java.util.ArrayList<>();
            // remove existing CE line for this enchant
            lore.removeIf(s -> s != null && s.startsWith("§7[CE] ") && s.toLowerCase().contains(target.getName().toLowerCase()));
            lore.add("§7[CE] " + target.getName() + " " + level);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        player.sendMessage(ChatColor.GREEN + "Applied " + target.getName() + " " + level + " to the item in your main hand.");
        return true;
    }
}
