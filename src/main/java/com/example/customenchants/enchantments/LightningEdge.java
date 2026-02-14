package com.example.customenchants.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class LightningEdge extends Enchantment {
    public LightningEdge(NamespacedKey key) {
        super(key);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item != null && item.getType().name().endsWith("_SWORD");
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return other.equals(Enchantment.KNOCKBACK) || other.equals(Enchantment.FIRE_ASPECT);
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEAPON;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "Lightning Edge";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }
}
