package com.example.customenchants.listeners;

import com.example.customenchants.CustomEnchantsPlugin;
import com.example.customenchants.enchantments.Lifesteal;
import com.example.customenchants.enchantments.LightningEdge;
import com.example.customenchants.enchantments.ShadowStep;
import com.example.customenchants.registrar.EnchantmentRegistrar;
import com.example.customenchants.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handles combat-related custom enchant behavior:
 * - Lightning Edge (per-target cooldown)
 * - Lifesteal
 * - Shadow Step (sneak + hit teleport behind target)
 */
public class CombatListener implements Listener {
    private final CustomEnchantsPlugin plugin;

    // lightning per-attacker -> (target -> lastStrikeMillis)
    private final Map<UUID, Map<UUID, Long>> lightningCooldowns = new HashMap<>();

    // shadow step per-player cooldown (ms)
    private final Map<UUID, Long> shadowCooldowns = new HashMap<>();

    private static final long LIGHTNING_COOLDOWN_MS = 5_000L;
    private static final long SHADOW_COOLDOWN_MS = 10_000L;

    public CombatListener(CustomEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity target = event.getEntity();

        if (!(damager instanceof Player)) return;
        Player attacker = (Player) damager;

        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        if (weapon == null) return;

        // Shadow Step: sneak + hit
        int shadowLevel = ItemUtils.getCustomEnchantLevel(weapon, EnchantmentRegistrar.SHADOWSTEP);
        if (shadowLevel > 0 && attacker.isSneaking() && target instanceof LivingEntity) {
            handleShadowStep(attacker, (LivingEntity) target);
        }

        // Lightning Edge: only when hitting players
        int lightningLevel = ItemUtils.getCustomEnchantLevel(weapon, EnchantmentRegistrar.LIGHTNING);
        if (lightningLevel > 0 && target instanceof Player) {
            handleLightningEdge(attacker, (Player) target, lightningLevel);
        }

        // Lifesteal: heal attacker for percentage of damage dealt
        int lifestealLevel = ItemUtils.getCustomEnchantLevel(weapon, EnchantmentRegistrar.LIFESTEAL);
        if (lifestealLevel > 0 && event.getDamage() > 0 && attacker instanceof Player) {
            handleLifesteal(attacker, event.getDamage(), lifestealLevel);
        }
    }

    private void handleLightningEdge(Player attacker, Player target, int level) {
        UUID a = attacker.getUniqueId();
        UUID t = target.getUniqueId();
        long now = System.currentTimeMillis();

        lightningCooldowns.putIfAbsent(a, new HashMap<>());
        Map<UUID, Long> perTarget = lightningCooldowns.get(a);
        Long last = perTarget.get(t);
        if (last != null && now - last < LIGHTNING_COOLDOWN_MS) return;

        // Chance increases with level: 12% per level (L1=12%, L3=36%)
        double chance = 0.12 * level;
        if (Math.random() < chance) {
            Location loc = target.getLocation();
            // strike lightning (may cause damage)
            loc.getWorld().strikeLightning(loc);
            perTarget.put(t, now);
        }
    }

    private void handleLifesteal(Player attacker, double damage, int level) {
        // Percentage healed per level: 0.04 (4%) per level
        double percent = 0.04 * level;
        double healAmount = damage * percent;

        double health = attacker.getHealth();
        double maxHealth = attacker.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();

        double newHealth = Math.min(maxHealth, health + healAmount);
        if (newHealth > health) {
            attacker.setHealth(newHealth);
        }
    }

    private void handleShadowStep(Player attacker, LivingEntity target) {
        UUID uuid = attacker.getUniqueId();
        long now = System.currentTimeMillis();
        Long last = shadowCooldowns.get(uuid);
        if (last != null && now - last < SHADOW_COOLDOWN_MS) return;

        // Teleport attacker behind target
        Location tloc = target.getLocation();
        // direction of target; behind = -direction
        org.bukkit.util.Vector behind = tloc.getDirection().normalize().multiply(-1.2).setY(0);
        Location dest = tloc.clone().add(behind);
        dest.setPitch(attacker.getLocation().getPitch());
        dest.setYaw(target.getLocation().getYaw());

        // ensure we teleport slightly above ground to avoid suffocation
        dest.add(0, 0.5, 0);
        Bukkit.getScheduler().runTask(plugin, () -> attacker.teleport(dest));
        shadowCooldowns.put(uuid, now);
    }
}
