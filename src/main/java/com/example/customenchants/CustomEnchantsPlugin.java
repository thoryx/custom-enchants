package com.example.customenchants;

import com.example.customenchants.listeners.CombatListener;
import com.example.customenchants.registrar.EnchantmentRegistrar;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomEnchantsPlugin extends JavaPlugin {
    private static CustomEnchantsPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        // Register custom enchantments
        EnchantmentRegistrar.registerAll(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new com.example.customenchants.listeners.EnchantLoreListener(), this);

        // Register command executors
        getCommand("giveenchant").setExecutor(new com.example.customenchants.commands.GiveEnchantCommand());

        getLogger().info("CustomEnchants enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomEnchants disabled");
    }

    public static CustomEnchantsPlugin getInstance() {
        return instance;
    }
}
