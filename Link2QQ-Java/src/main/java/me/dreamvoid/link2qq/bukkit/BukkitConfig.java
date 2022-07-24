package me.dreamvoid.link2qq.bukkit;

import me.dreamvoid.link2qq.Config;

public class BukkitConfig {
    private final BukkitPlugin plugin;

    public BukkitConfig(BukkitPlugin plugin){
        this.plugin = plugin;
    }

    public void loadConfig(){
        plugin.saveDefaultConfig();

        Config.Bot.BotAccounts = plugin.getConfig().getLongList("bot.bot-accounts");
        Config.Bot.GroupIds = plugin.getConfig().getLongList("bot.group-ids");
        Config.Bot.AddBindCommand = plugin.getConfig().getString("bot.add-bind-command","添加绑定");
        Config.Bot.ConfirmBindCommand = plugin.getConfig().getString("bot.confirm-bind-command","确认绑定");
        Config.Bot.ConfirmCodeLength = plugin.getConfig().getInt("bot.confirm-code-length", 6);
    }
}
