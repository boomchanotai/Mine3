# Mine3Standard

Similar to the EssentialX plugin, Mine3Standard provides standard commands to support address-based operations.

## Prerequisite

- Mine3Lib

## Compatibility

- Java 21
- Minecraft 1.21

## Commands & Permission

| Commands                          | Permission                    | Description                     |
| --------------------------------- | ----------------------------- | ------------------------------- |
| `/tp <address>`                   | `mine3.tp`                    | Teleport to player.             |
| `/tp <address> <address>`         | `mine3.tp.others`             | Teleport player to player.      |
| `/tphere <address>`               | `mine3.tphere`                | Teleport player to you.         |
| `/tpa <address>`                  | `mine3.tpa`                   | Request Teleport to player.     |
| `/tpahere <address>`              | `mine3.tpahere`               | Request Teleport player to you. |
| `/tpaccept`                       | `mine3.tpaccept`              | Accept teleport request.        |
| `/tpacancel`                      | `mine3.tpacancel`             | Cancel teleport request.        |
| `/give <address> <item> <amount>` | `mine3.give`                  | Give item to player.            |
| `/i <item> <amount>`              | `mine3.give`                  | Give item to command user.      |
| `/gamemode <gamemode>`            | `mine3.gamemode`              | Set gamemode.                   |
| `/gamemode <gamemode> <address>`  | `mine3.gamemode.others`       | Set gamemode to player.         |
| `/broadcast <message>`            | `mine3.broadcast`             | Set gamemode to player.         |
| `/burn`                           | `mine3.burn`                  | Burn player.                    |
| `/burn <address>`                 | `mine3.burn.others`           | Burn other player.              |
| `/clear`                          | `mine3.clearinventory`        | Clear inventory player.         |
| `/clear <address>`                | `mine3.clearinventory.others` | Clear inventory player.         |
| `/feed`                           | `mine3.feed`                  | Feed player.                    |
| `/feed <address>`                 | `mine3.feed.others`           | Feed other player.              |
| `/heal`                           | `mine3.heal`                  | Heal player.                    |
| `/heal <address>`                 | `mine3.heal.others`           | Heal other player.              |
| `/vanish`                         | `mine3.vanish`                | Vanish player.                  |
| `/vanish <address>`               | `mine3.vanish.others`         | Vanish other player.            |
| `/fly`                            | `mine3.fly`                   | Fly player.                     |
| `/fly <address>`                  | `mine3.fly.others`            | Fly other player.               |
| `/god`                            | `mine3.god`                   | Set god player.                 |
| `/god <address>`                  | `mine3.god.others`            | Set god other player.           |
| `/speed`                          | `mine3.speed`                 | Set speed player.               |
| `/speed <address>`                | `mine3.speed.others`          | Set speed other player.         |
| `/kill`                           | `mine3.kill`                  | Kill player.                    |
| `/kill <address>`                 | `mine3.kill.others`           | Kill other player.              |
| `/kick <address>`                 | `mine3.kick`                  | Kick player.                    |
| `/kick <address> <reason>`        | `mine3.kick`                  | Kick player with reason.        |
| `/invsee <address>`               | `mine3.invsee`                | Inspect player's inventory.     |
| `/enderchest <address>`           | `mine3.enderchest`            | Inspect player's enderchest.    |
| `/setspawn`                       | `mine3.setspawn`              | Set spawn point                 |
| `/spawn`                          | `mine3.spawn`                 | Go to spawn.                    |
| `/op <address>`                   | `mine3.op`                    | Make player as operator.        |
| `/deop <address>`                 | `mine3.deop`                  | Remove player as operator.      |
| `/standard reload`                | `mine3.standard.reload`       | Reload configuration.           |

## Technical Information

- **Database Interaction**: Mine3Standard does not have its own database entry points, such as Redis or PostgreSQL.
- **Data Access**: Instead, it retrieves data from **Mine3Lib**'s repositories, leveraging the centralized data management provided by **Mine3Lib**.

This design ensures consistency and reduces redundancy by using the existing data infrastructure provided by Mine3Lib.
