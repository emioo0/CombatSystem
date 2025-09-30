package de.emilio.combatSystem.listeners;

import de.emilio.combatSystem.manager.CombatManager;
import de.emilio.combatSystem.manager.ConfigManager;
import de.emilio.combatSystem.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    private final ConfigManager config;
    private final CombatManager combat;

    public DamageListener(ConfigManager config, CombatManager combat) {
        this.config = config;
        this.combat = combat;
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;

        Player attacker = null;

        // Resolve attacker (direct or projectile)
        if (event.getDamager() instanceof Player p) {
            attacker = p;
        } else if (event.getDamager() instanceof org.bukkit.entity.Projectile proj && proj.getShooter() instanceof Player p) {
            attacker = p;
        }

        if (attacker == null) return;

        // Global PvP switch
        if (!config.isPvpEnabled()) {
            event.setCancelled(true);
            if (attacker.isOnline()) {
                MessageUtil.sendPrefixed(attacker, config.getMsgPvpCurrentlyOff());
            }
            return;
        }

        // Enter/extend combat for both players based on damage
        double finalDamage = event.getFinalDamage();
        combat.enterOrExtendCombat(victim, attacker, finalDamage);

        // Enter-combat messages with cooldown
        if (combat.canSendEnterCombatMessage(attacker.getUniqueId())) {
            MessageUtil.sendPrefixed(attacker, config.getMsgAttacker().replace("%target%", victim.getName()));
            combat.markEnterCombatMessageSent(attacker.getUniqueId());
        }
        if (combat.canSendEnterCombatMessage(victim.getUniqueId())) {
            MessageUtil.sendPrefixed(victim, config.getMsgVictim().replace("%attacker%", attacker.getName()));
            combat.markEnterCombatMessageSent(victim.getUniqueId());
        }
    }
}
