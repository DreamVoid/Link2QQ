package me.dreamvoid.link2qq.nukkit;

import java.io.File;
import java.util.List;

public class Config {
    private final NukkitPlugin plugin;

    public static List<Long> Bot_Id;
    public static List<Long> Bot_Group;
    public static String Bot_AddBindCommand;
    public static String Bot_ConfirmBindCommand;
    public static int Bot_ConfirmCodeLength;

    public Config(NukkitPlugin plugin){
        this.plugin = plugin;
    }

    public void loadConfig(){
        if(!new File(plugin.getDataFolder(),"config.yml").exists()){
            plugin.saveDefaultConfig();
        }

        Bot_Id = plugin.getConfig().getLongList("bot.bot-accounts");
        Bot_Group = plugin.getConfig().getLongList("bot.group-ids");
        Bot_AddBindCommand = plugin.getConfig().getString("bot.add-bind-command","添加绑定");
        Bot_ConfirmBindCommand = plugin.getConfig().getString("bot.confirm-bind-command","确认绑定");
        Bot_ConfirmCodeLength = plugin.getConfig().getInt("bot.confirm-code-length", 6);
    }
}
