package me.dreamvoid.link2qq.bungee;

import com.google.gson.Gson;
import me.dreamvoid.link2qq.SerializableConfig;
import me.dreamvoid.link2qq.Utils;
import me.dreamvoid.link2qq.bungee.command.link2qq;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.api.MiraiMC;
import me.dreamvoid.miraimc.bungee.event.message.passive.MiraiFriendMessageEvent;
import me.dreamvoid.miraimc.bungee.event.message.passive.MiraiGroupMessageEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;

public class BungeePlugin extends Plugin implements Listener {
    public SerializableConfig config;

    @Override
    public void onLoad() {
        loadConfig();
    }

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this,new link2qq(this,"link2qq"));
    }

    @EventHandler
    public void onFriendMessageReceive(MiraiFriendMessageEvent e){
        if(config.bot.botAccounts.contains(e.getBotID())){
            getProxy().getScheduler().runAsync(this, () -> {
                String[] args = e.getMessage().split(" ");
                if(args[0].equals(config.bot.addBindCommand)){
                    if(args.length >= 2){
                        Utils.qqBind.put(e.getSenderID(),args[1]);
                        Utils.qqCode.put(e.getSenderID(),Utils.getRandomString(config.bot.confirmCodeLength));
                        for (String s : Arrays.asList("请在游戏内输入指令以完成绑定流程：", "/link2qq verify " + e.getSenderID() + " " + Utils.qqCode.get(e.getSenderID()))) {
                            MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage(s);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                    } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("参数不足，请检查消息内容！");
                }
                if(args[0].equals(config.bot.confirmBindCommand)){
                    if(args.length >= 3){
                        String name = args[1];
                        String code = args[2];
                        if(getProxy().getPlayer(name) != null && Utils.playerBind.get(name) != null && Utils.playerCode.get(name) != null && Utils.playerBind.get(name) == (e.getSenderID()) && Utils.playerCode.get(name).equals(code)){
                            MiraiMC.addBind(getProxy().getPlayer(name).getUniqueId(), e.getSenderID());
                            MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("已成功添加绑定！");
                            Utils.playerBind.remove(name);
                            Utils.playerCode.remove(name);
                        } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("无法核对您的信息，请检查您的输入或重新发起绑定！");
                    } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("参数不足，请检查消息内容！");
                }
            });
        }
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e){
        if(config.bot.botAccounts.contains(e.getBotID()) && config.bot.groupIds.contains(e.getGroupID())){
            getProxy().getScheduler().runAsync(this, () -> {
                String[] args = e.getMessage().split(" ");
                if(args[0].equals(config.bot.addBindCommand)){
                    if(args.length >= 2){
                        Utils.qqBind.put(e.getSenderID(),args[1]);
                        Utils.qqCode.put(e.getSenderID(),Utils.getRandomString(config.bot.confirmCodeLength));
                        for (String s : Arrays.asList("请在游戏内输入指令以完成绑定流程：", "/link2qq verify " + e.getSenderID() + " " + Utils.qqCode.get(e.getSenderID()))) {
                            MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage(s);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                    } else MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage("参数不足，请检查消息内容！");
                }
                if(args[0].equals(config.bot.confirmBindCommand)){
                    if(args.length >= 3){
                        String name = args[1];
                        String code = args[2];
                        if(getProxy().getPlayer(name) != null && Utils.playerBind.get(name) != null && Utils.playerCode.get(name) != null && Utils.playerBind.get(name) == (e.getSenderID()) && Utils.playerCode.get(name).equals(code)){
                            MiraiMC.addBind(getProxy().getPlayer(name).getUniqueId(), e.getSenderID());
                            MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage("已成功添加绑定！");
                            Utils.playerBind.remove(name);
                            Utils.playerCode.remove(name);
                        } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("无法核对您的信息，请检查您的输入或重新发起绑定！");
                    } else MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage("参数不足，请检查消息内容！");
                }
            });
        }
    }

    public void loadConfig() {
        if (!getDataFolder().exists() && !getDataFolder().mkdir()) {
            getLogger().warning("Failed to create data folder!");
        }

        File configFile = new File(getDataFolder(), "config.yml");
        try {
            if (!configFile.exists()) {
                try (InputStream in = getResourceAsStream("config.yml")) {
                    Files.copy(in, configFile.toPath());
                }
            }
            config = new Gson().fromJson(new Yaml().load(Files.newInputStream(configFile.toPath())).toString(), SerializableConfig.class); // 先转为json，再用gson解析，权宜之计
        } catch (IOException e) {
            getLogger().severe("Failed to load configuration file, reason: " + e);
        }
    }
}
