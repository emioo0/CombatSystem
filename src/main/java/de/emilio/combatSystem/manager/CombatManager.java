package de.emilio.combatSystem.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CombatManager {

    private final ConfigManager config;

    // Combat end times (epoch millis)
    private final Map<UUID, Long> combatUntil = new ConcurrentHashMap<>();

    // Accumulated damage within current combat session (per victim)
    private final Map<UUID, Double> accumulatedDamage = new ConcurrentHashMap<>();

    // Last time we sent the "enter combat" messages (per player)
    private final Map<UUID, Long> lastCombatMsg = new ConcurrentHashMap<>();

    // Last damager to produce custom PvP death messages
    private final Map<UUID, UUID> lastDamager = new ConcurrentHashMap<>();

    // Ender pearl usage in current combat session
    private final Map<UUID, Integer> pearlsUsed = new ConcurrentHashMap<>();

    public CombatManager(Object plugin, ConfigManager config) {
        this.config = config;
    }

    public boolean isInCombat(UUID uuid) {
        Long until = combatUntil.get(uuid);
        return until != null && until > System.currentTimeMillis();
    }

    public int getRemainingSeconds(UUID uuid) {
        Long until = combatUntil.get(uuid);
        if (until == null) return 0;
        long ms = until - System.currentTimeMillis();
        return (int) Math.max(0, Math.ceil(ms / 1000.0));
    }

    public void enterOrExtendCombat(Player victim, Player attacker, double damage) {
        // Record last damager
        if (victim != null && attacker != null) {
            lastDamager.put(victim.getUniqueId(), attacker.getUniqueId());
        }

        // Accumulate damage (for scaling)
        if (victim != null) {
            accumulatedDamage.putIfAbsent(victim.getUniqueId(), 0.0);
            accumulatedDamage.compute(victim.getUniqueId(), (k, v) -> (v == null ? 0.0 : v) + Math.max(0.0, damage));
        }

        // Compute desired duration
        int seconds = config.getBaseCombatSeconds();
        double total = victim != null ? accumulatedDamage.getOrDefault(victim.getUniqueId(), 0.0) : 0.0;

        if (total >= config.getThreshold2Damage()) {
            seconds = config.getThreshold2Seconds();
        } else if (total >= config.getThreshold1Damage()) {
            seconds = config.getThreshold1Seconds();
        }

        seconds = Math.min(seconds, config.getMaxCombatSeconds());

        // Set/extend for BOTH players if present
        long newUntil = System.currentTimeMillis() + (seconds * 1000L);

        if (victim != null) {
            Long old = combatUntil.get(victim.getUniqueId());
            if (old == null || newUntil > old) {
                combatUntil.put(victim.getUniqueId(), newUntil);
            }
        }
        if (attacker != null) {
            Long old = combatUntil.get(attacker.getUniqueId());
            if (old == null || newUntil > old) {
                combatUntil.put(attacker.getUniqueId(), newUntil);
            }
        }
    }

    public boolean canSendEnterCombatMessage(UUID uuid) {
        long now = System.currentTimeMillis();
        long last = lastCombatMsg.getOrDefault(uuid, 0L);
        return now - last >= (config.getMessageCooldownSeconds() * 1000L);
    }

    public void markEnterCombatMessageSent(UUID uuid) {
        lastCombatMsg.put(uuid, System.currentTimeMillis());
    }

    public void resetSession(UUID uuid) {
        combatUntil.remove(uuid);
        accumulatedDamage.remove(uuid);
        lastCombatMsg.remove(uuid);
        pearlsUsed.remove(uuid);
        lastDamager.remove(uuid);
    }

    public void onCombatEndCleanup(UUID uuid) {
        accumulatedDamage.remove(uuid);
        lastCombatMsg.remove(uuid);
        pearlsUsed.remove(uuid);
        // keep lastDamager until death or explicit reset
    }

    public UUID getLastDamager(UUID victim) {
        return lastDamager.get(victim);
    }

    public void clearLastDamager(UUID victim) {
        lastDamager.remove(victim);
    }

    public int getPearlsUsed(UUID uuid) {
        return pearlsUsed.getOrDefault(uuid, 0);
    }

    public void incrementPearls(UUID uuid) {
        pearlsUsed.put(uuid, getPearlsUsed(uuid) + 1);
    }

    public void resetPearls(UUID uuid) {
        pearlsUsed.remove(uuid);
    }

    public void shutdown() {
        combatUntil.clear();
        accumulatedDamage.clear();
        lastCombatMsg.clear();
        pearlsUsed.clear();
        lastDamager.clear();
    }

    public Collection<Player> activeCombatPlayers() {
        List<Player> list = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (isInCombat(p.getUniqueId())) list.add(p);
        }
        return list;
    }
}
