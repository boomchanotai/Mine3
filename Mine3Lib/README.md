# Mine3Lib

Central library for Mine3 plugins, allowing other developers to use our standards to integrate web3 services with our infrastructure.

## Prerequisite

- Mine3Lib

## Compatibility

- Java 21
- Minecraft 1.21

## Library

Repositories for address actions

- `ArrayList<Address> getAllAddress()` - get all online address from redis (allows you to connect more than one server and use same redis to check if player already in some server)
- `ArrayList<Address> getOnlinePlayers()` - get all online players from server
- `Address getAddress(UUID uuid)` - get player's address
- `Player getPlayer(Address address)` - get player from address
- `void setPlayer(Address address, Player player)` - add player to redis (use when new player authenticated)
- `void removePlayer(Address address)` - remove player from redis (use when player disconnect)
- `void clearPlayer()` - remove all player in redis

Events

- `PlayerAuthEvent` - trigger when user authenticated

  **Note: If you want to implement your own authentication plugin after login success please trigger `PlayerAuthEvent` to use with Permision plugin**

## Commands & Permission

| Commands           | Permission         | Description           |
| ------------------ | ------------------ | --------------------- |
| `/mine3lib reload` | `mine3.lib.reload` | Reload configuration. |
