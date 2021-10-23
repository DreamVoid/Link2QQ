package me.dreamvoid.link2qq.nukkit;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.TextFormat;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.api.MiraiMC;
import me.dreamvoid.miraimc.nukkit.event.MiraiFriendMessageEvent;
import me.dreamvoid.miraimc.nukkit.event.MiraiGroupMessageEvent;

import java.util.Arrays;

public class NukkitPlugin extends PluginBase implements Listener {
    private Config PluginConfig;

    @Override
    public void onLoad() {
        PluginConfig = new Config(this);
    }

    @Override
    public void onEnable() {
        PluginConfig.loadConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onFriendMessageReceive(MiraiFriendMessageEvent e) {
        if (Config.Bot_Id.contains(e.getBotID())) {
            getServer().getScheduler().scheduleAsyncTask(this, new AsyncTask() {
                @Override
                public void onRun() {
                    String[] args = e.getMessageContent().split(" ");
                    if (args[0].equals(Config.Bot_AddBindCommand)) {
                        if (args.length >= 2) {
                            Utils.qqBind.remove(e.getSenderID());
                            Utils.qqCode.remove(e.getSenderID());
                            Utils.qqBind.put(e.getSenderID(), args[1]);
                            Utils.qqCode.put(e.getSenderID(), Utils.getRandomString(Config.Bot_ConfirmCodeLength));
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
                    if (args[0].equals(Config.Bot_ConfirmBindCommand)) {
                        if (args.length >= 3) {
                            String name = args[1];
                            String code = args[2];
                            if (Utils.playerBind.get(name) != null && Utils.playerCode.get(name) != null && Utils.playerBind.get(name) == (e.getSenderID()) && Utils.playerCode.get(name).equals(code)) {
                                MiraiMC.addBinding(getServer().getOfflinePlayer(name).getUniqueId().toString(), e.getSenderID());
                                MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("已成功添加绑定！如需更换绑定，请直接发起新的绑定；如需取消绑定，请联系管理员！");
                            } else
                                MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("无法核对您的信息，请检查您的输入或重新发起绑定！");
                        } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("参数不足，请检查消息内容！");
                    }
                }
            });
        }
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e) {
        if (Config.Bot_Id.contains(e.getBotID()) && Config.Bot_Group.contains(e.getGroupID())) {
            getServer().getScheduler().scheduleAsyncTask(this, new AsyncTask() {
                @Override
                public void onRun() {
                    String[] args = e.getMessageContent().split(" ");
                    if (args[0].equals(Config.Bot_AddBindCommand)) {
                        if (args.length >= 2) {
                            Utils.qqBind.remove(e.getSenderID());
                            Utils.qqCode.remove(e.getSenderID());
                            Utils.qqBind.put(e.getSenderID(), args[1]);
                            Utils.qqCode.put(e.getSenderID(), Utils.getRandomString(Config.Bot_ConfirmCodeLength));
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
                    if (args[0].equals(Config.Bot_ConfirmBindCommand)) {
                        if (args.length >= 3) {
                            String name = args[1];
                            String code = args[2];
                            if (Utils.playerBind.get(name) != null && Utils.playerCode.get(name) != null && Utils.playerBind.get(name) == (e.getSenderID()) && Utils.playerCode.get(name).equals(code)) {
                                MiraiMC.addBinding(getServer().getOfflinePlayer(name).getUniqueId().toString(), e.getSenderID());
                                MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage("已成功添加绑定！如需更换绑定，请直接发起新的绑定；如需取消绑定，请联系管理员！");
                                Utils.playerBind.remove(name);
                                Utils.playerCode.remove(name);
                            } else
                                MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("无法核对您的信息，请检查您的输入或重新发起绑定！");
                        } else MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage("参数不足，请检查消息内容！");
                    }

                }
            });
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 0){
            switch (args[0].toLowerCase()){
                case "bind": {
                    if(args.length >=2 && sender instanceof Player){
                        Player player = (Player)sender;
                        long qqId = Long.parseLong(args[1]);
                        Utils.playerBind.remove(player.getName());
                        Utils.playerCode.remove(player.getName());
                        Utils.playerBind.put(player.getName(),qqId);
                        Utils.playerCode.put(player.getName(),Utils.getRandomString(Config.Bot_ConfirmCodeLength));
                        String verify = Config.Bot_ConfirmBindCommand + " "+ player.getName() + " "+ Utils.playerCode.get(player.getName());

                        player.sendMessage(TextFormat.colorize('&',"&e请使用你的QQ私聊机器人或向机器人所在群发送以下消息："));
                        player.sendMessage(TextFormat.AQUA + verify);
                        player.sendMessage(TextFormat.colorize('&',"&e以完成验证流程 &7(点击可复制)"));
                    }
                    break;
                }
                case "verify":{
                    if(args.length >=3 && sender instanceof Player) {
                        Player player = (Player) sender;
                        long qqId = Long.parseLong(args[1]);
                        String code = args[2];
                        if(Utils.qqBind.get(qqId) != null && Utils.qqCode.get(qqId) != null && Utils.qqBind.get(qqId).equalsIgnoreCase(player.getName()) && Utils.qqCode.get(qqId).equals(code)){
                            MiraiMC.addBinding(player.getUniqueId().toString(),qqId);
                            for (String s : Arrays.asList("&a已成功添加绑定！", "&a如需取消绑定，请联系管理员！")) {
                                player.sendMessage(TextFormat.colorize('&',s));
                            }
                        } else player.sendMessage(TextFormat.colorize('&',"&c无法核对您的信息，请检查您的输入或重新发起绑定！"));
                    }
                    break;
                }
                case "reload":{
                    if(sender.hasPermission("miraimc.command.link2qq.reload")){
                        PluginConfig.loadConfig();
                        Utils.qqCode.clear();
                        Utils.qqBind.clear();
                        Utils.playerBind.clear();
                        Utils.playerCode.clear();
                        sender.sendMessage(TextFormat.colorize('&',"&a配置文件已重新加载，并已清空待验证队列！"));
                    } else sender.sendMessage(TextFormat.colorize('&',"&c你没有足够的权限执行此命令！"));
                    break;
                }
                default:{
                    sender.sendMessage(TextFormat.colorize('&',"&6&lLink&b&l2&e&lQQ&r &a插件菜单"));
                    sender.sendMessage(TextFormat.colorize('&',"&6/link2qq bind <QQ号>:&r 添加一个绑定"));
                    sender.sendMessage(TextFormat.colorize('&',"&6/link2qq verify <QQ号> <绑定码>:&r 确认绑定"));
                    sender.sendMessage(TextFormat.colorize('&',"&6/link2qq reload:&r 重新加载配置文件"));
                    break;
                }
            }
        } else sender.sendMessage("This server is running "+getDescription().getName()+" version "+getDescription().getVersion()+" by "+ getDescription().getAuthors().toString().replace("[","").replace("]","")+" (MiraiMC version "+getServer().getPluginManager().getPlugin("MiraiMC").getDescription().getVersion()+")");
        return true;
    }
}