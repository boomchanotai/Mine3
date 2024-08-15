package com.boomchanotai.mine3AuthProxy.config;

import java.io.File;
import java.nio.file.Path;

import com.boomchanotai.core.logger.Logger;
import com.boomchanotai.mine3AuthProxy.Mine3AuthProxy;
import com.velocitypowered.api.plugin.annotation.DataDirectory;

import dev.dejvokep.boostedyaml.YamlDocument;

public class Config {
    private static YamlDocument config;

    public static String AUTH_SERVER;

    public static void loadConfig(@DataDirectory Path dataDirectory) {
        try {
            config = YamlDocument.create(new File(dataDirectory.toFile(), "config.yml"),
                    Mine3AuthProxy.getPlugin().getClass().getResourceAsStream("/config.yml"));

            config.update();
            config.save();
        } catch (Exception e) {
            Logger.warning("Error loading config file", e.getMessage());
        }

        AUTH_SERVER = config.getString("auth_server");
    }
}
