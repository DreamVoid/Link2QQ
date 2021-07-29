package me.dreamvoid.link2qq.internal;

import me.dreamvoid.link2qq.bukkit.BukkitPlugin;

import java.io.File;

public class Config {
    private BukkitPlugin plugin;

    public static long Bot_Id;
    public static long Bot_Group;
    public static String Bot_AddBindCommand;
    public static String Bot_ConfirmBindCommand;
    public static int Bot_ConfirmCodeLength;

    public Config(BukkitPlugin plugin){
        this.plugin = plugin;
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
}
