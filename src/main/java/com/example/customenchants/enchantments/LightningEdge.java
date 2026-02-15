package com.example.customenchants.enchantments;

import org.bukkit.enchantments.Rarity;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.NamespacedKey;
import java.util.Set;

public class LightningEdge extends Enchantment {
    private static final NamespacedKey KEY = NamespacedKey.minecraft("lightning_edge");
    public LightningEdge() {
        super();
    }
    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }
    @Override
    public NamespacedKey getKey() {
        return KEY;
    }
    @Override
    public Set<EquipmentSlot> getActiveSlots() {
        return Set.of(EquipmentSlot.HAND);
    }

    @Override
    public float getDamageIncrease(int level, org.bukkit.entity.EntityType entityType) {
        // This enchantment does not increase damage directly
        return 0f;
    }

    /**
     * Deprecated, for Paper 1.20.6 compatibility.
     */
    @Override
    @Deprecated
    public float getDamageIncrease(int level, org.bukkit.entity.EntityCategory entityCategory) {
        return 0f;
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
