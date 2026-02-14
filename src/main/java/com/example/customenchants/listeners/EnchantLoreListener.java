package com.example.customenchants.listeners;

import com.example.customenchants.registrar.EnchantmentRegistrar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Ensures items with our custom enchantments display a small lore line and preserve glow.
 */
public class EnchantLoreListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        updateHeld(event.getPlayer().getInventory().getItemInMainHand());
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        updateHeld(item);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        updateHeld(event.getCurrentItem());
    }

    private void updateHeld(ItemStack item) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();

        // Remove any previous custom-enchant lines we added
        lore.removeIf(s -> s.startsWith("§7[CE] "));

        int l1 = item.getEnchantmentLevel(EnchantmentRegistrar.LIGHTNING);
        int l2 = item.getEnchantmentLevel(EnchantmentRegistrar.LIFESTEAL);
        int l3 = item.getEnchantmentLevel(EnchantmentRegistrar.SHADOWSTEP);

        if (l1 > 0) lore.add("§7[CE] Lightning Edge " + l1);
        if (l2 > 0) lore.add("§7[CE] Lifesteal " + l2);
        if (l3 > 0) lore.add("§7[CE] Shadow Step " + l3);

        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}
