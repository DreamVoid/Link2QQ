package me.dreamvoid.link2qq.bungee;

import me.dreamvoid.link2qq.Config;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BungeeConfig {
    private static BungeePlugin BungeePlugin;

    private static BungeeConfig Instance;

    public BungeeConfig(BungeePlugin bungee){
        BungeePlugin = bungee;
        Instance = this;
    }

    public void loadConfigBungee() throws IOException {
        if (!BungeePlugin.getDataFolder().exists() && !BungeePlugin.getDataFolder().mkdir()) throw new RuntimeException("Failed to create data folder!");

        File file = new File(BungeePlugin.getDataFolder(), "config.yml");
        if (!file.exists()) try (InputStream in = BungeePlugin.getResourceAsStream("config.yml")) {
            Files.copy(in, file.toPath());
        }
        Configuration config = ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).load(new File(BungeePlugin.getDataFolder(), "config.yml"));

        Config.Bot.BotAccounts = config.getLongList("bot.bot-accounts");
        Config.Bot.GroupIds = config.getLongList("bot.group-ids");
        Config.Bot.AddBindCommand = config.getString("bot.add-bind-command","添加绑定");
        Config.Bot.ConfirmBindCommand = config.getString("bot.confirm-bind-command","确认绑定");
        Config.Bot.ConfirmCodeLength = config.getInt("bot.confirm-code-length", 6);
    }

    public static void reloadConfigBungee() throws IOException{
        Instance.loadConfigBungee();
    }
}
