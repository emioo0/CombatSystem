package de.emilio.combatSystem.listeners;

import de.emilio.combatSystem.manager.CombatManager;
import de.emilio.combatSystem.manager.ConfigManager;
import de.emilio.combatSystem.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class DeathListener implements Listener {

    private final ConfigManager config;
    private final CombatManager combat;

    public DeathListener(ConfigManager config, CombatManager combat) {
        this.config = config;
        this.combat = combat;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();

        if (config.isHideVanillaDeath()) {
            event.deathMessage(null);
        }

        UUID killerUUID = combat.getLastDamager(victim.getUniqueId());
        Player killer = killerUUID != null ? Bukkit.getPlayer(killerUUID) : null;

        if (killer != null && killer.isOnline()) {
            // Broadcast Custom PvP-Death
            String msg = config.getMsgPvPDeathBroadcast()
                    .replace("%victim%", victim.getName())
                    .replace("%killer%", killer.getName());
            MessageUtil.broadcastPrefixed(msg);

            // Killer sofort aus Combat nehmen
            combat.resetSession(killer.getUniqueId());
            MessageUtil.sendPrefixed(killer, "&aDu bist wieder sicher, Der Kampf ist vorbei!");
        }

        // Opfer-Session aufräumen
        combat.resetSession(victim.getUniqueId());
        combat.clearLastDamager(victim.getUniqueId());
    }
}
