package me.dreamvoid.link2qq.bungee;

import me.dreamvoid.link2qq.bungee.commands.link2qq;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.api.MiraiMC;
import me.dreamvoid.miraimc.bungee.event.MiraiFriendMessageEvent;
import me.dreamvoid.miraimc.bungee.event.MiraiGroupMessageEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;

public class BungeePlugin extends Plugin implements Listener {
    @Override
    public void onLoad() {
        new Config(this);
    }

    @Override
    public void onEnable() {
        Config.loadConfigBungee();
        ProxyServer.getInstance().getPluginManager().registerCommand(this,new link2qq(this,"link2qq"));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    public void onFriendMessageReceive(MiraiFriendMessageEvent e){
        if(Config.Bot_Id.contains(e.getBotID())){
            getProxy().getScheduler().runAsync(this, () -> {
                String[] args = e.getMessageContent().split(" ");
                if(args[0].equals(Config.Bot_AddBindCommand)){
                    if(args.length >= 2){
                        Utils.qqBind.remove(e.getSenderID());
                        Utils.qqCode.remove(e.getSenderID());
                        Utils.qqBind.put(e.getSenderID(),args[1]);
                        Utils.qqCode.put(e.getSenderID(),Utils.getRandomString(Config.Bot_ConfirmCodeLength));
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
                if(args[0].equals(Config.Bot_ConfirmBindCommand)){
                    if(args.length >= 3){
                        String name = args[1];
                        String code = args[2];
                        if(getProxy().getPlayer(name) != null && Utils.playerBind.get(name) != null && Utils.playerCode.get(name) != null && Utils.playerBind.get(name) == (e.getSenderID()) && Utils.playerCode.get(name).equals(code)){
                            MiraiMC.addBinding(getProxy().getPlayer(name).getUniqueId().toString(), e.getSenderID());
                            MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("已成功添加绑定！如需更换绑定，请直接发起新的绑定；如需取消绑定，请联系管理员！");
                        } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("无法核对您的信息，请检查您的输入或重新发起绑定！");
                    } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("参数不足，请检查消息内容！");
                }
            });
        }
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e){
        if(Config.Bot_Id.contains(e.getBotID()) && Config.Bot_Group.contains(e.getGroupID())){
            getProxy().getScheduler().runAsync(this, () -> {
                String[] args = e.getMessageContent().split(" ");
                if(args[0].equals(Config.Bot_AddBindCommand)){
                    if(args.length >= 2){
                        Utils.qqBind.remove(e.getSenderID());
                        Utils.qqCode.remove(e.getSenderID());
                        Utils.qqBind.put(e.getSenderID(),args[1]);
                        Utils.qqCode.put(e.getSenderID(),Utils.getRandomString(Config.Bot_ConfirmCodeLength));
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
                if(args[0].equals(Config.Bot_ConfirmBindCommand)){
                    if(args.length >= 3){
                        String name = args[1];
                        String code = args[2];
                        if(getProxy().getPlayer(name) != null && Utils.playerBind.get(name) != null && Utils.playerCode.get(name) != null && Utils.playerBind.get(name) == (e.getSenderID()) && Utils.playerCode.get(name).equals(code)){
                            MiraiMC.addBinding(getProxy().getPlayer(name).getUniqueId().toString(), e.getSenderID());
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
