# Mine3Auth

Authentication service plugin for Minecraft servers, providing secure and efficient user authentication to ensure only authorized players can access your server.

## Prerequisite

- Mine3Lib

## Compatibility

- Java 21
- Minecraft 1.21

## Feature

1. Login with EVM wallet from website
2. Support for storing player data in a database, including:
   - Address
   - ENS Domain (v2)
   - Xp Level
   - Xp Exp
   - Health
   - Food Level
   - Gamemode
   - Flying speed
   - Walking speed
   - Allow Flight (v2)
   - Is flying
   - Is op
   - Is banned (v2)
   - Potion effects
   - Last location
3. Support ban address
4. Rename player to short address so, every player will show as address only.

## Commands & Permissions

| Commands            | Permission            | Description                                                              |
| ------------------- | --------------------- | ------------------------------------------------------------------------ |
| `/address`          | -                     | Get sender's address.                                                    |
| `/address <player>` | -                     | Get player's full address.                                               |
| `/logout`           | -                     | Logout (which will save the player's inventory and generate a new token) |
| `/mine3 spawn`      | -                     | Go to Mine3's spawn points.                                              |
| `/mine3 setspawn`   | `mine3.auth.setspawn` | Set spawn points when a player is not logged in.                         |
| `/mine3 reload`     | `mine3.auth.reload`   | Reload configuration.                                                    |
| `/ban <address>`    | `mine3.ban`           | Ban address.                                                             |
| `/pardon <address>` | `mine3.pardon`        | Pardon address.                                                          |
