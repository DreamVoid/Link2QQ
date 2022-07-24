package me.dreamvoid.link2qq.bungee;

import me.dreamvoid.link2qq.Config;
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

import java.io.IOException;
import java.util.Arrays;

public class BungeePlugin extends Plugin implements Listener {
    BungeeConfig config;

    @Override
    public void onLoad() {
        config = new BungeeConfig(this);
    }

    @Override
    public void onEnable() {
        try {
            config.loadConfigBungee();
        } catch (IOException e) {
            getLogger().warning("读取配置文件时出现异常，原因：" + e);
        }
        ProxyServer.getInstance().getPluginManager().registerCommand(this,new link2qq(this,"link2qq"));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    public void onFriendMessageReceive(MiraiFriendMessageEvent e){
        if(Config.Bot.BotAccounts.contains(e.getBotID())){
            getProxy().getScheduler().runAsync(this, () -> {
                String[] args = e.getMessage().split(" ");
                if(args[0].equals(Config.Bot.AddBindCommand)){
                    if(args.length >= 2){
                        Utils.qqBind.remove(e.getSenderID());
                        Utils.qqCode.remove(e.getSenderID());
                        Utils.qqBind.put(e.getSenderID(),args[1]);
                        Utils.qqCode.put(e.getSenderID(),Utils.getRandomString(Config.Bot.ConfirmCodeLength));
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
                if(args[0].equals(Config.Bot.ConfirmBindCommand)){
                    if(args.length >= 3){
                        String name = args[1];
                        String code = args[2];
                        if(getProxy().getPlayer(name) != null && Utils.playerBind.get(name) != null && Utils.playerCode.get(name) != null && Utils.playerBind.get(name) == (e.getSenderID()) && Utils.playerCode.get(name).equals(code)){
                            MiraiMC.addBind(getProxy().getPlayer(name).getUniqueId(), e.getSenderID());
                            MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("已成功添加绑定！如需更换绑定，请直接发起新的绑定；如需取消绑定，请联系管理员！");
                        } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("无法核对您的信息，请检查您的输入或重新发起绑定！");
                    } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("参数不足，请检查消息内容！");
                }
            });
        }
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e){
        if(Config.Bot.BotAccounts.contains(e.getBotID()) && Config.Bot.GroupIds.contains(e.getGroupID())){
            getProxy().getScheduler().runAsync(this, () -> {
                String[] args = e.getMessage().split(" ");
                if(args[0].equals(Config.Bot.AddBindCommand)){
                    if(args.length >= 2){
                        Utils.qqBind.remove(e.getSenderID());
                        Utils.qqCode.remove(e.getSenderID());
                        Utils.qqBind.put(e.getSenderID(),args[1]);
                        Utils.qqCode.put(e.getSenderID(),Utils.getRandomString(Config.Bot.ConfirmCodeLength));
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
                if(args[0].equals(Config.Bot.ConfirmBindCommand)){
                    if(args.length >= 3){
                        String name = args[1];
                        String code = args[2];
                        if(getProxy().getPlayer(name) != null && Utils.playerBind.get(name) != null && Utils.playerCode.get(name) != null && Utils.playerBind.get(name) == (e.getSenderID()) && Utils.playerCode.get(name).equals(code)){
                            MiraiMC.addBind(getProxy().getPlayer(name).getUniqueId(), e.getSenderID());
                            MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage("已成功添加绑定！如需更换绑定，请直接发起新的绑定；如需取消绑定，请联系管理员！");
                            Utils.playerBind.remove(name);
                            Utils.playerCode.remove(name);
                        } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("无法核对您的信息，请检查您的输入或重新发起绑定！");
                    } else MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage("参数不足，请检查消息内容！");
                }
            });
        }
    }
}
