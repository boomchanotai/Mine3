# Mine3

Authenticate minecraft server with EVM Wallet

Features:

- Login with EVM Wallet Website using:
  - [React](https://react.dev/)
  - [VITE](https://vitejs.dev/guide/)
  - [Rainbowkit](https://www.rainbowkit.com/)
  - [Wagmi](https://wagmi.sh/)
- Support for storing player data in a database, including:

1. Address
2. Xp Level
3. Xp Exp
4. Health
5. Food Level
6. Gamemode
7. Flying speed
8. Walking speed
9. Is flying
10. Is op
11. Potion effects
12. Last location

Commands:

- `/address` - Get sender's address.
- `/address <player>` - Get player's address.
- `/logout` - Logout (which will save the player's inventory) and generate a new token.
- `/mine3 setspawn` - Set spawn points when a player is not logged in.
- `/mine3 spawn` - Go to Mine3's spawn points.

**Note:** Mine3 spawn points are not the same as Essentials' spawn points.

This plugin is a proof of concept to enhance the Minecraft experience with the Web3 world. There are many tasks to complete before it can be used in production, such as replacing player names with addresses and handling every command that currently uses player names to use addresses instead. and there's some question for further improvement:

- Should we replace player names with addresses?
- How can we handle /tp, /give, /op, /gamemode with addresses only?
- How can we build an ecosystem with all plugins that support Web3, such as an interface for storing player data?

## Compatibility

Currently support only Spigot-1.21

- Java 21
- Minecraft 1.21

## Quickstart

We've already provided `docker-compose.yml` for additional services for your Minecraft server, including PostgreSQL, Redis, and a frontend to sign your EVM wallet.

1. Install [docker](https://www.docker.com/)
2. Go to your **Mine3** directory using:

```bash
cd /your_folder_path/Mine3
```

3. Run `docker-compose.yml`:

```bash
docker compose up -d
```

4. Create your own Spigot server
5. Move `Mine3-1.0-SNAPSHOT.jar` to your `plugins` folder
6. Start your server.
7. Configure your **Mine3** settings in `plugins/Mine3/config.yml` as follows:

- Set your Redis host and port.
- Set your PostgreSQL URL.
- Set your HTTP server port.
- Set `auth.website_token_base_url` to your server domain or IP address.

## Authors

- [BoomChanotai](https://github.com/boomchanotai)

## Screenshots

See demo video [here](https://www.youtube.com/watch?v=nqnzk2r-nzQ).

![login](/screenshots/mine3-1.png)
![sign-wallet](/screenshots/mine3-2.png)
![login-success](/screenshots/mine3-3.png)
