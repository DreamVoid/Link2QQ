package me.dreamvoid.link2qq.sponge;

import me.dreamvoid.link2qq.Config;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

public class SpongeConfig {
    private final SpongePlugin plugin;
    private static SpongeConfig Instance;

    public static File PluginDir;

    public SpongeConfig(SpongePlugin plugin){
        Instance = this;
        this.plugin = plugin;
        PluginDir = plugin.getDataFolder();
    }
    public void loadConfig() throws IOException {
        if(!PluginDir.exists() && !PluginDir.mkdirs()) throw new RuntimeException("Failed to create data folder!");
        File file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream is = plugin.getClass().getResourceAsStream("/config-sponge.yml")) {
                assert is != null;
                Files.copy(is, file.toPath());
            }
        }

        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream(file);
        //yaml.loadAs(inputStream, Config.class);

        Map<String, Object> obj = (Map<String, Object>) yaml.load(inputStream);

        Map<String, Object> bot = !Objects.isNull(obj.get("bot")) ? (Map<String, Object>) obj.get("bot") : new HashMap<>();
        Config.Bot.BotAccounts = !Objects.isNull(bot.get("bot-accounts:")) ? (List<Long>) bot.get("bot-accounts") : Arrays.asList(123456L, 789012L);
        Config.Bot.GroupIds = !Objects.isNull(bot.get("group-ids")) ? (List<Long>) bot.get("group-ids") : Arrays.asList(123456L, 789012L);
        Config.Bot.AddBindCommand = !Objects.isNull(bot.get("add-bind-command")) ? (String) bot.get("add-bind-command") : "添加绑定";
        Config.Bot.ConfirmBindCommand = !Objects.isNull(bot.get("confirm-bind-command")) ? (String) bot.get("confirm-bind-command") : "添加绑定";
        Config.Bot.ConfirmCodeLength = !Objects.isNull(bot.get("confirm-code-length")) ? (Integer) bot.get("confirm-code-length") : 6;

        System.out.println(Config.Bot.ConfirmCodeLength);
    }

    public static void reloadConfig() throws IOException{
        Instance.loadConfig();
    }
}
