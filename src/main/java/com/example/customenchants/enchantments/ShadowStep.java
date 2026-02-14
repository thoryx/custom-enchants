package com.example.customenchants.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class ShadowStep extends Enchantment {
    public ShadowStep(NamespacedKey key) {
        super(key);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item != null && item.getType().name().endsWith("_SWORD");
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return other.equals(Enchantment.SWEEPING_EDGE) || other.equals(Enchantment.KNOCKBACK);
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEAPON;
    }

    @Override
    public int getMaxLevel() {
        return 1; // only needs single level; behavior gated by cooldown
    }

    @Override
    public String getName() {
        return "Shadow Step";
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
