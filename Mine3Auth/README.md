# Mine3Auth

Authentication service plugin for Minecraft servers, providing secure and efficient user authentication to ensure only authorized players can access your server.

## Prerequisite

- Mine3Lib

## Compatibility

- Java 21
- Minecraft 1.21

## Feature

Login with EVM wallet from website

## Commands & Permissions

| Commands            | Permission          | Description                                                              |
| ------------------- | ------------------- | ------------------------------------------------------------------------ |
| `/address`          | -                   | Get sender's address.                                                    |
| `/address <player>` | -                   | Get player's full address.                                               |
| `/logout`           | -                   | Logout (which will save the player's inventory and generate a new token) |
| `/mine3 reload`     | `mine3.auth.reload` | Reload configuration.                                                    |

## Technical Information

- **Login Token Storage**: Redis is used to store login tokens securely.
- **Player Cache Data**: The plugin stores player cache data by calling `void setPlayer(Address address, Player player)` from **Mine3Lib**, integrating with **Mine3Lib**’s data management system.
- **HTTP Handling**: Javalin is employed as the HTTP handler to verify signatures and act as the trigger point for player authentication.

This setup ensures secure and efficient management of player data and authentication processes within the Mine3 ecosystem.
