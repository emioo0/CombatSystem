package de.emilio.combatSystem.commands;

import de.emilio.combatSystem.manager.CombatManager;
import de.emilio.combatSystem.manager.ConfigManager;
import de.emilio.combatSystem.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombatCommand implements CommandExecutor, TabCompleter {

    private final ConfigManager config;
    @SuppressWarnings("unused")
    private final CombatManager combat;

    public CombatCommand(ConfigManager config, CombatManager combat) {
        this.config = config;
        this.combat = combat;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sendHelp(sender, label);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "pvpon" -> {
                if (!sender.hasPermission("combatsystem.admin")) {
                    MessageUtil.sendPrefixed(sender, "&cDazu hast du keine Rechte.");
                    return true;
                }
                config.setPvpEnabled(true);
                Bukkit.getOnlinePlayers().forEach(p -> MessageUtil.sendPrefixed(p, config.getMsgPvpEnabled()));
                return true;
            }
            case "pvpoff" -> {
                if (!sender.hasPermission("combatsystem.admin")) {
                    MessageUtil.sendPrefixed(sender, "&cDazu hast du keine Rechte.");
                    return true;
                }
                config.setPvpEnabled(false);
                Bukkit.getOnlinePlayers().forEach(p -> MessageUtil.sendPrefixed(p, config.getMsgPvpDisabled()));
                return true;
            }
            case "reload" -> {
                if (!sender.hasPermission("combatsystem.admin")) {
                    MessageUtil.sendPrefixed(sender, "&cDazu hast du keine Rechte.");
                    return true;
                }
                config.reload();
                MessageUtil.sendPrefixed(sender, config.getMsgReloaded());
                return true;
            }
            case "help" -> {
                sendHelp(sender, label);
                return true;
            }
            default -> {
                sendHelp(sender, label);
                return true;
            }
        }
    }

    private void sendHelp(CommandSender sender, String label) {
        MessageUtil.sendPrefixed(sender, "&7Verfügbare Befehle:");
        MessageUtil.send(sender, "&f/" + label + " pvpon &7- PvP global aktivieren");
        MessageUtil.send(sender, "&f/" + label + " pvpoff &7- PvP global deaktivieren");
        MessageUtil.send(sender, "&f/" + label + " reload &7- Config neu laden");
        MessageUtil.send(sender, "&f/" + label + " help &7- Diese Hilfe anzeigen");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> base = Arrays.asList("pvpon", "pvpoff", "reload", "help");
            List<String> out = new ArrayList<>();
            String start = args[0].toLowerCase();
            for (String s : base) {
                if (s.startsWith(start)) out.add(s);
            }
            return out;
        }
        return List.of();
    }
}
