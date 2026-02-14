package com.example.customenchants.registrar;

import com.example.customenchants.CustomEnchantsPlugin;
import com.example.customenchants.enchantments.LightningEdge;
import com.example.customenchants.enchantments.Lifesteal;
import com.example.customenchants.enchantments.ShadowStep;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentRegistrar {
    public static LightningEdge LIGHTNING;
    public static Lifesteal LIFESTEAL;
    public static ShadowStep SHADOWSTEP;

    public static void registerAll(CustomEnchantsPlugin plugin) {
        try {
            LIGHTNING = new LightningEdge(new NamespacedKey(plugin, "lightning_edge"));
            LIFESTEAL = new Lifesteal(new NamespacedKey(plugin, "lifesteal"));
            SHADOWSTEP = new ShadowStep(new NamespacedKey(plugin, "shadow_step"));

            // Register them if not already present
            registerIfAbsent(LIGHTNING);
            registerIfAbsent(LIFESTEAL);
            registerIfAbsent(SHADOWSTEP);
        } catch (Exception ex) {
            plugin.getLogger().severe("Failed to register custom enchantments: " + ex.getMessage());
        }
    }

    private static void registerIfAbsent(Enchantment enchantment) {
        try {
            if (Enchantment.getByKey(enchantment.getKey()) == null) {
                Enchantment.registerEnchantment(enchantment);
            }
        } catch (IllegalArgumentException ignored) {
            // already registered by another plugin or server restart
        }
    }
}
