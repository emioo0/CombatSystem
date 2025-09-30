package de.emilio.combatSystem.manager;

import de.emilio.combatSystem.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final Main plugin;
    private FileConfiguration cfg;

    private String prefix;
    private boolean pvpEnabled;

    private String msgAttacker;
    private String msgVictim;
    private int messageCooldownSeconds;

    private String msgActionbarFormat;

    private int baseCombatSeconds;
    private int threshold1Seconds;
    private int threshold2Seconds;
    private double threshold1Damage;
    private double threshold2Damage;
    private int maxCombatSeconds;

    private String msgCombatLogBroadcast;
    private String msgPvPDeathBroadcast;
    private String msgCombatEnded;

    private boolean combatLogKill;
    private boolean hideVanillaDeath;

    private boolean limitPearlsInCombat;
    private int pearlsLimit;
    private String msgPearlsRemaining;
    private String msgPearlsNoMore;

    private boolean disableTridentInCombat;
    private String msgTridentBlocked;

    private String msgPvpEnabled;
    private String msgPvpDisabled;
    private String msgPvpCurrentlyOff;
    private String msgReloaded;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.cfg = plugin.getConfig();

        prefix = cfg.getString("messages.prefix", "&f[&6&lᴄᴏᴍʙᴀᴛꜱʏꜱᴛᴇᴍ&f]");
        pvpEnabled = cfg.getBoolean("settings.pvp-enabled", true);

        msgAttacker = cfg.getString("messages.attacker",
                "&7Du hast &a%target% &7angegriffen und bist jetzt in Combat!");
        msgVictim = cfg.getString("messages.victim",
                "&7Du wurdest von &c%attacker% &7angegriffen, und bist jetzt in Combat!");
        messageCooldownSeconds = cfg.getInt("settings.message-cooldown-seconds", 60);

        msgActionbarFormat = cfg.getString("messages.actionbar-format",
                "&cIm Kampf! &7(%s s übrig)");

        baseCombatSeconds = cfg.getInt("combat-duration.base", 30);
        threshold1Seconds = cfg.getInt("combat-duration.threshold1.seconds", 100);
        threshold2Seconds = cfg.getInt("combat-duration.threshold2.seconds", 150);
        threshold1Damage = cfg.getDouble("combat-duration.threshold1.damage", 6.0);
        threshold2Damage = cfg.getDouble("combat-duration.threshold2.damage", 12.0);
        maxCombatSeconds = cfg.getInt("combat-duration.max", 150);

        msgCombatLogBroadcast = cfg.getString("messages.combatlog-broadcast",
                "&c%player% &fhat sich im Kampf ausgeloggt und ist gestorben!");
        msgPvPDeathBroadcast = cfg.getString("messages.pvp-death-broadcast",
                "&c%victim% &fwurde von &a%killer% &fbesiegt!");
        msgCombatEnded = cfg.getString("messages.combat-ended",
                "&aDu bist wieder sicher, Der Kampf ist vorbei!");

        combatLogKill = cfg.getBoolean("settings.combatlog.kill-player", true);
        hideVanillaDeath = cfg.getBoolean("settings.death.hide-vanilla", true);

        limitPearlsInCombat = cfg.getBoolean("settings.enderpearls.limit-in-combat", true);
        pearlsLimit = cfg.getInt("settings.enderpearls.limit", 16);
        msgPearlsRemaining = cfg.getString("messages.pearls-remaining",
                "&eDu hast noch &f%remaining% Enderperlen &eübrig.");
        msgPearlsNoMore = cfg.getString("messages.pearls-no-more",
                "&cDu kannst im Combat keine weiteren Enderperlen verwenden.");

        disableTridentInCombat = cfg.getBoolean("settings.trident.disable-in-combat", true);
        msgTridentBlocked = cfg.getString("messages.trident-blocked",
                "&cDu kannst im Combat keinen Dreizack verwenden.");

        msgPvpEnabled = cfg.getString("messages.pvp-enabled", "&7PvP wurde &aaktiviert&7.");
        msgPvpDisabled = cfg.getString("messages.pvp-disabled", "&7PvP wurde &cdeaktiviert&7.");
        msgPvpCurrentlyOff = cfg.getString("messages.pvp-currently-off", "&cPvP ist aktuell deaktiviert.");
        msgReloaded = cfg.getString("messages.reloaded", "&aConfig neu geladen.");

        plugin.saveConfig();
    }

    public void reload() { load(); }

    public FileConfiguration raw() { return cfg; }

    public String prefixed(String message) {
        return prefix + " " + (message == null ? "" : message);
    }

    public String getPrefix() { return prefix; }

    public boolean isPvpEnabled() { return pvpEnabled; }
    public void setPvpEnabled(boolean state) {
        this.pvpEnabled = state;
        cfg.set("settings.pvp-enabled", state);
        plugin.saveConfig();
    }

    public String getMsgAttacker() { return msgAttacker; }
    public String getMsgVictim() { return msgVictim; }
    public int getMessageCooldownSeconds() { return messageCooldownSeconds; }

    public String getActionbarFormat() { return msgActionbarFormat; }

    public int getBaseCombatSeconds() { return baseCombatSeconds; }
    public int getThreshold1Seconds() { return threshold1Seconds; }
    public int getThreshold2Seconds() { return threshold2Seconds; }
    public double getThreshold1Damage() { return threshold1Damage; }
    public double getThreshold2Damage() { return threshold2Damage; }
    public int getMaxCombatSeconds() { return maxCombatSeconds; }

    public String getMsgCombatLogBroadcast() { return msgCombatLogBroadcast; }
    public String getMsgPvPDeathBroadcast() { return msgPvPDeathBroadcast; }
    public String getMsgCombatEnded() { return msgCombatEnded; }

    public boolean isCombatLogKill() { return combatLogKill; }
    public boolean isHideVanillaDeath() { return hideVanillaDeath; }

    public boolean isLimitPearlsInCombat() { return limitPearlsInCombat; }
    public int getPearlsLimit() { return pearlsLimit; }
    public String getMsgPearlsRemaining() { return msgPearlsRemaining; }
    public String getMsgPearlsNoMore() { return msgPearlsNoMore; }

    public boolean isDisableTridentInCombat() { return disableTridentInCombat; }
    public String getMsgTridentBlocked() { return msgTridentBlocked; }

    public String getMsgPvpEnabled() { return msgPvpEnabled; }
    public String getMsgPvpDisabled() { return msgPvpDisabled; }
    public String getMsgPvpCurrentlyOff() { return msgPvpCurrentlyOff; }
    public String getMsgReloaded() { return msgReloaded; }
}
