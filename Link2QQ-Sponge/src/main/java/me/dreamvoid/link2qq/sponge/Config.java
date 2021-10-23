package me.dreamvoid.link2qq.sponge;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

public class Config {
    private final SpongePlugin plugin;
    private static Config Instance;

    public static File PluginDir;

    public static List<Long> Bot_Id;
    public static List<Long> Bot_Group;
    public static String Bot_AddBindCommand;
    public static String Bot_ConfirmBindCommand;
    public static int Bot_ConfirmCodeLength;

    public Config(SpongePlugin plugin){
        Instance = this;
        this.plugin = plugin;
        PluginDir = plugin.getDataFolder();
    }
    public void loadConfig() throws IOException {
        if(!PluginDir.exists() && !PluginDir.mkdirs()) throw new RuntimeException("Failed to create data folder!");
        File file = new File(plugin.getDataFolder(), "link2qq.yml");
        if (!file.exists()) {
            try (InputStream is = plugin.getClass().getResourceAsStream("/link2qq.yml")) {
                assert is != null;
                Files.copy(is, file.toPath());
            }
        }

        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream(file);
        Map<String, Object> obj = (Map<String, Object>) yaml.load(inputStream);

        Map<String, Object> bot = !Objects.isNull(obj.get("bot")) ? (Map<String, Object>) obj.get("bot") : new HashMap<>();
        Bot_Id = !Objects.isNull(bot.get("bot-accounts:")) ? (List<Long>) bot.get("bot-accounts") : Arrays.asList(123456L, 789012L);
        Bot_Group = !Objects.isNull(bot.get("group-ids")) ? (List<Long>) bot.get("group-ids") : Arrays.asList(123456L, 789012L);
        Bot_AddBindCommand = !Objects.isNull(bot.get("add-bind-command")) ? (String) bot.get("add-bind-command") : "添加绑定";
        Bot_ConfirmBindCommand = !Objects.isNull(bot.get("confirm-bind-command")) ? (String) bot.get("confirm-bind-command") : "添加绑定";
        Bot_ConfirmCodeLength = !Objects.isNull(bot.get("confirm-code-length")) ? (Integer) bot.get("confirm-code-length") : 6;
    }

    public static void reloadConfig() throws IOException{
        Instance.loadConfig();
    }
}
