# Mine3

A group of open-source Minecraft plugins that enable Minecraft to interact with the Web3 world. These plugins allow authentication with an EVM wallet, use address-based commands like `/tp <address>`, and seamlessly connect Minecraft with Web3 services.

Mine3 offers four modules you can choose from:

1. **Mine3Lib**: A central library for Mine3 plugins, providing standards for integrating Web3 services with our infrastructure - [documents](/Mine3Lib/README.md)
2. **Mine3Auth**: An authentication service plugin for Minecraft servers, ensuring secure and efficient user authentication - [documents](/Mine3Auth/README.md)
3. **Mine3Permission**: A permission manager plugin for Mine3, driven by the address concept, designed to manage player permissions effectively -[documents](/Mine3Permission/README.md)
4. **Mine3Standard**: Similar to the EssentialX plugin, providing standard commands to support address-based operations - [documents](/Mine3Standard//README.md)

## Quickstart

We've already provided `docker-compose.yml` for additional services for your Minecraft server, including PostgreSQL, Redis, and a frontend to sign your EVM wallet.

1. Install [docker](https://www.docker.com/)
2. Config `docker-compose.yml`

   - Register & Create new project in [Rainbowkit](https://www.rainbowkit.com/)
   - Set Rainbowkit project id at `VITE_PROJECT_ID` in `services.mine3_frontend.build.args`

3. Run `docker-compose.yml`:

   ```bash
   docker compose up -d
   ```

4. Create your own Spigot server
5. Move Mine3 plugins to your `plugins` folder
6. Start your server.
7. Config all **Mine3** plugins in plugins folder

   - Set your Redis host and port.
   - Set your PostgreSQL URL.
   - Set your HTTP server port.
   - Set `auth.website_token_base_url` to your server domain or IP address. (frontend url)

## Authors

- [BoomChanotai](https://github.com/boomchanotai)

## Screenshots

See demo video [here](https://www.youtube.com/watch?v=nqnzk2r-nzQ).

![login](/screenshots/mine3-1.png)
![sign-wallet](/screenshots/mine3-2.png)
![login-success](/screenshots/mine3-3.png)
