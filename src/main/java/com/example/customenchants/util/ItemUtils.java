package com.example.customenchants.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * Small utilities for item/enchantment handling.
 */
public final class ItemUtils {
    private ItemUtils() {}

    public static int getCustomEnchantLevel(ItemStack item, Enchantment enchantment) {
        if (item == null || enchantment == null) return 0;
        try {
            return item.getEnchantmentLevel(enchantment);
        } catch (Exception ex) {
            return 0;
        }
    }
}
