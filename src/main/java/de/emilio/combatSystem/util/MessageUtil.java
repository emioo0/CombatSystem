package de.emilio.combatSystem.util;

import de.emilio.combatSystem.manager.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtil {

    private static ConfigManager config;
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    public static void init(ConfigManager cfg) {
        config = cfg;
    }

    // ----- Helpers -----

    private static Component legacy(String s) {
        if (s == null) return Component.empty();
        return LEGACY.deserialize(s);
    }

    private static Component prefixedComponent(String raw) {
        String full = config.getPrefix() + " " + (raw == null ? "" : raw);
        return legacy(full);
    }

    // ----- Send APIs -----

    public static void send(CommandSender sender, String raw) {
        sender.sendMessage(legacy(raw));
    }

    public static void sendPrefixed(CommandSender sender, String raw) {
        sender.sendMessage(prefixedComponent(raw));
    }

    public static void sendPrefixed(Player p, String raw) {
        p.sendMessage(prefixedComponent(raw));
    }

    public static void broadcastPrefixed(String raw) {
        Component comp = prefixedComponent(raw);
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(comp));
        // Optional: Konsole
        Bukkit.getConsoleSender().sendMessage(comp);
    }

    public static void sendActionbar(Player p, String text) {
        p.sendActionBar(legacy(text));
    }

    public static String formatActionbarSeconds(int secondsRemaining) {
        return String.format(config.getActionbarFormat(), secondsRemaining);
    }
}
