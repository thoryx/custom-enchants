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
        LIGHTNING = new LightningEdge();
        LIFESTEAL = new Lifesteal();
        SHADOWSTEP = new ShadowStep();
        // Paper 1.20.6+ automatically registers custom enchantments on plugin load if they are constructed
    }
}
