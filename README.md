# CombatSystem

A configurable Minecraft combat system plugin for **Spigot/Paper 1.21.x**.  
It provides a fair PvP experience with combat timers, custom messages, and protections against combat logging.

---

## ✨ Features
- **Combat Timer**: Actionbar countdown that scales with damage taken (up to 150 seconds).
- **Combat Logging**: Players who log out during combat are killed, with a broadcast message.
- **Custom Death Messages**: Replace vanilla kill messages with configurable PvP death messages.
- **Ender Pearl Limits**: Restrict the number of pearls usable while in combat (default: 16).
- **Trident Restriction**: Prevent trident usage while in combat (configurable).
- **Global PvP Toggle**: Enable or disable PvP server-wide with `/combat pvpon` or `/combat pvpoff`.
- **Configurable Messages**: All messages and prefixes are fully configurable in `config.yml`.

---

## ⚙️ Commands
| Command              | Description                        | Permission             |
|----------------------|------------------------------------|------------------------|
| `/combat pvpon`      | Enable global PvP                  | `combatsystem.admin`   |
| `/combat pvpoff`     | Disable global PvP                 | `combatsystem.admin`   |
| `/combat reload`     | Reload the configuration file      | `combatsystem.admin`   |
| `/combat help`       | Show command help                  | `combatsystem.use`     |

---

## 📂 Configuration
The plugin generates a `config.yml` on first startup.  
Inside you can configure:
- Prefix and messages
- Combat duration scaling
- Combat logging behavior
- Ender pearl and trident restrictions
- PvP toggle defaults

---

## 🔧 Installation
1. Download the plugin JAR from the releases page or build it yourself.
2. Place the JAR file into your server’s `plugins/` folder.
3. Restart the server.
4. Edit `config.yml` to your liking and use `/combat reload` to apply changes.

---

## 📝 License
This project is released under the MIT License.
