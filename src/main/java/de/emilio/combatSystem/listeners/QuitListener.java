package de.emilio.combatSystem.listeners;

import de.emilio.combatSystem.manager.CombatManager;
import de.emilio.combatSystem.manager.ConfigManager;
import de.emilio.combatSystem.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final ConfigManager config;
    private final CombatManager combat;

    public QuitListener(ConfigManager config, CombatManager combat) {
        this.config = config;
        this.combat = combat;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (combat.isInCombat(p.getUniqueId())) {
            if (config.isCombatLogKill()) {
                p.setHealth(0.0);
                MessageUtil.broadcastPrefixed(
                        config.getMsgCombatLogBroadcast().replace("%player%", p.getName()));
            }
        }
        combat.resetSession(p.getUniqueId());
    }
}
