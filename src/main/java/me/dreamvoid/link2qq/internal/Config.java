package me.dreamvoid.link2qq.internal;

import me.dreamvoid.link2qq.bukkit.BukkitPlugin;
import me.dreamvoid.link2qq.bungee.BungeePlugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Config {
    private BukkitPlugin plugin;
    private static BungeePlugin BungeePlugin;
    private static Configuration bungeeConfig;

    public static long Bot_Id;
    public static long Bot_Group;
    public static String Bot_AddBindCommand;
    public static String Bot_ConfirmBindCommand;
    public static int Bot_ConfirmCodeLength;

    public Config(BukkitPlugin plugin){
        this.plugin = plugin;
    }
    public Config(BungeePlugin bungee){
        BungeePlugin = bungee;
    }

    public void loadConfig(){
        if(!new File(plugin.getDataFolder(),"config.yml").exists()){
            plugin.saveDefaultConfig();
        }

        Bot_Id = plugin.getConfig().getLong("bot.bot-account");
        Bot_Group = plugin.getConfig().getLong("bot.group-id");
        Bot_AddBindCommand = plugin.getConfig().getString("bot.add-bind-command","添加绑定");
        Bot_ConfirmBindCommand = plugin.getConfig().getString("bot.confirm-bind-command","确认绑定");
        Bot_ConfirmCodeLength = plugin.getConfig().getInt("bot.confirm-code-length", 6);
    }

    public static void loadConfigBungee(){
        try {
            if (!BungeePlugin.getDataFolder().exists()) {
                if(!BungeePlugin.getDataFolder().mkdir()) throw new IOException();
            }
            File file = new File(BungeePlugin.getDataFolder(), "config.yml");
            if (!file.exists()) {
                try (InputStream in = BungeePlugin.getResourceAsStream("config.yml")) {
                    Files.copy(in, file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            bungeeConfig = ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).load(new File(BungeePlugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bot_Id = bungeeConfig.getLong("bot.bot-account");
        Bot_Group = bungeeConfig.getLong("bot.group-id");
        Bot_AddBindCommand = bungeeConfig.getString("bot.add-bind-command","添加绑定");
        Bot_ConfirmBindCommand = bungeeConfig.getString("bot.confirm-bind-command","确认绑定");
        Bot_ConfirmCodeLength = bungeeConfig.getInt("bot.confirm-code-length", 6);
    }
}
