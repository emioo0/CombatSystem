package de.emilio.combatSystem.tasks;

import de.emilio.combatSystem.manager.CombatManager;
import de.emilio.combatSystem.manager.ConfigManager;
import de.emilio.combatSystem.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ActionBarTask extends BukkitRunnable {

    private final ConfigManager config;
    private final CombatManager combat;

    public ActionBarTask(ConfigManager config, CombatManager combat) {
        this.config = config;
        this.combat = combat;
    }

    @Override
    public void run() {
        List<Player> toClear = new ArrayList<>();
        for (Player p : combat.activeCombatPlayers()) {
            int remaining = combat.getRemainingSeconds(p.getUniqueId());
            if (remaining <= 0) {
                toClear.add(p);
                continue;
            }

            MessageUtil.sendActionbar(p, MessageUtil.formatActionbarSeconds(remaining));
        }
        // Combat-Ende: einmalige Chat-Nachricht + Cleanup
        for (Player p : toClear) {
            MessageUtil.sendPrefixed(p, "&aDu bist wieder sicher, Der Kampf ist vorbei!");
            combat.onCombatEndCleanup(p.getUniqueId());
        }
    }
}
