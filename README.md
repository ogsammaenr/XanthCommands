# XanthCommands

**XanthCommands** is a modular and customizable Minecraft plugin built for Paper servers. It provides players with helpful utility commands, cooldown-based permissions, and integration with custom enchanting table locations.

## Features

- `/fly`: Toggles flight mode for the player.
- `/repairhand`: Repairs the item in the player's main hand (with cooldown and permission tiers).
- `/repairall`: Repairs all damaged items in the player's inventory (with cooldown and permission tiers).
- `/enchant`: Accesses a configurable enchanting table system based on per-world locations.
- `/craft`: Opens a virtual crafting table.
- `/anvil`: Opens a virtual anvil.
- `/xanthcommands reload`: Reloads the plugin configuration.
- `/xanthcommands version`: Displays the current plugin version.
- `/xanthcommands setenchantingtable`: Saves the player's location as the enchanting table position for the current world.
- SQLite-based persistent cooldown system.
- Multiple permission levels with configurable cooldown durations.
- Per-world enchanting table support.

## Configuration Example

```yaml
cooldowns:
  repairhand:
    level1:
      permission: "xanthcommands.cooldown.repairhand1"
      duration: 60
    level2:
      permission: "xanthcommands.cooldown.repairhand2"
      duration: 180
    level3:
      permission: "xanthcommands.cooldown.repairhand3"
      duration: 360
