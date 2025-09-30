package de.emilio.combatSystem.listeners;

import de.emilio.combatSystem.manager.CombatManager;
import de.emilio.combatSystem.manager.ConfigManager;
import de.emilio.combatSystem.util.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Trident;

public class ProjectileListener implements Listener {

    private final ConfigManager config;
    private final CombatManager combat;

    public ProjectileListener(ConfigManager config, CombatManager combat) {
        this.config = config;
        this.combat = combat;
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player player)) return;

        // Ender pearl limiting
        if (config.isLimitPearlsInCombat() && event.getEntity() instanceof EnderPearl) {
            if (combat.isInCombat(player.getUniqueId())) {
                int used = combat.getPearlsUsed(player.getUniqueId());
                if (used >= config.getPearlsLimit()) {
                    event.setCancelled(true);
                    MessageUtil.sendPrefixed(player, config.getMsgPearlsNoMore());
                } else {
                    combat.incrementPearls(player.getUniqueId());
                    int remaining = config.getPearlsLimit() - combat.getPearlsUsed(player.getUniqueId());
                    MessageUtil.sendPrefixed(player, config.getMsgPearlsRemaining().replace("%remaining%", String.valueOf(remaining)));
                }
            }
            return;
        }

        // Trident blocking during combat
        if (config.isDisableTridentInCombat() && event.getEntity() instanceof Trident) {
            if (combat.isInCombat(player.getUniqueId())) {
                event.setCancelled(true);
                MessageUtil.sendPrefixed(player, config.getMsgTridentBlocked());
            }
        }
    }
}
