# Mine3Lib

Central library for Mine3 plugins, allowing other developers to use our standards to integrate web3 services with our infrastructure.

## Prerequisite

- Mine3Lib

## Compatibility

- Java 21
- Minecraft 1.21

## Library

Repositories for address actions

Package `com.boomchanotai.mine3Lib.repositories.PlayerRepository`:

- `ArrayList<Address> getAllAddress()` - get all online address from redis (allows you to connect more than one server and use same redis to check if player already in some server)
- `ArrayList<Address> getOnlinePlayers()` - get all online players from server
- `Address getAddress(UUID uuid)` - get player's address
- `Player getPlayer(Address address)` - get player from address
- `void setPlayer(Address address, Player player)` - add player to redis (use when new player authenticated)
- `void removePlayer(Address address)` - remove player from redis (use when player disconnect)
- `void clearPlayer()` - remove all player in redis

Events

- `PlayerAuthEvent` - trigger when user authenticated

## Guide for implementation

1. **To integrate with Mine3 plugins**:

   - After a player has logged in, the plugin should call the method `void setPlayer(Address address, Player player)`.
   - This action will:
     - Store the player's data in Redis.
     - Add the player to a cache array in Mine3Lib.
     - Trigger the `PlayerAuthEvent`, signaling that the player has been authenticated.

2. **Compatibility with Mine3Permission**:

   - To ensure compatibility with Mine3Permission, it's crucial to trigger the `PlayerAuthEvent` by calling `void setPlayer(Address address, Player player)`.
   - This event is essential as Mine3Permission assigns permissions to the player based on the triggered event.

## Commands & Permission

| Commands           | Permission         | Description           |
| ------------------ | ------------------ | --------------------- |
| `/mine3lib reload` | `mine3.lib.reload` | Reload configuration. |

## Technical Information

- **Player Cache Data**: Uses Redis to store player cache data, enabling quick access and efficient data management.
- **Horizontal Server Scaling**: Mine3Lib supports horizontal server scaling with a stateless architecture. By using the same Redis instance, it can check whether a player is already on the server, facilitating seamless scaling across multiple server instances.

This approach ensures scalability and efficient resource management within the Mine3 infrastructure.
