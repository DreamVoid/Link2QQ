package me.dreamvoid.link2qq.bukkit;

import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.api.MiraiMC;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiFriendMessageEvent;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class BukkitPlugin extends JavaPlugin implements Listener {
    private Config PluginConfig;

    @Override
    public void onLoad() {
        PluginConfig = new Config(this);
    }

    @Override
    public void onEnable() {
        PluginConfig.loadConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() { }

    @EventHandler
    public void onFriendMessageReceive(MiraiFriendMessageEvent e){
        if(Config.Bot_Id.contains(e.getBotID())){
            new BukkitRunnable() {
                @Override
                public void run() {
                    String[] args = e.getMessage().split(" ");
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
                            if(Utils.playerBind.get(name) != null && Utils.playerCode.get(name) != null && Utils.playerBind.get(name) == (e.getSenderID()) && Utils.playerCode.get(name).equals(code)){
                                MiraiMC.addBind(Bukkit.getOfflinePlayer(name).getUniqueId(), e.getSenderID());
                                MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("已成功添加绑定！如需更换绑定，请直接发起新的绑定；如需取消绑定，请联系管理员！");
                            } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("无法核对您的信息，请检查您的输入或重新发起绑定！");
                        } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("参数不足，请检查消息内容！");
                    }
                }
            }.runTaskAsynchronously(this);
        }
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e){
        if(Config.Bot_Id.contains(e.getBotID()) && Config.Bot_Group.contains(e.getGroupID())){
            new BukkitRunnable() {
                @Override
                public void run() {
                    String[] args = e.getMessage().split(" ");
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
                            if(Utils.playerBind.get(name) != null && Utils.playerCode.get(name) != null && Utils.playerBind.get(name) == (e.getSenderID()) && Utils.playerCode.get(name).equals(code)){
                                MiraiMC.addBind(Bukkit.getOfflinePlayer(name).getUniqueId(), e.getSenderID());
                                MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage("已成功添加绑定！如需更换绑定，请直接发起新的绑定；如需取消绑定，请联系管理员！");
                                Utils.playerBind.remove(name);
                                Utils.playerCode.remove(name);
                            } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("无法核对您的信息，请检查您的输入或重新发起绑定！");
                        } else MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage("参数不足，请检查消息内容！");
                    }
                }
            }.runTaskAsynchronously(this);
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
                        TextComponent message = new TextComponent(ChatColor.AQUA + verify);
                        message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, verify));
                        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("点击置入编辑框以复制")).create()));

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&e请使用你的QQ私聊机器人或向机器人所在群发送以下消息："));
                        player.spigot().sendMessage(message);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&e以完成验证流程 &7(点击可复制)"));
                    }
                    break;
                }
                case "verify":{
                    if(args.length >=3 && sender instanceof Player) {
                        Player player = (Player) sender;
                        long qqId = Long.parseLong(args[1]);
                        String code = args[2];
                        if(Utils.qqBind.get(qqId) != null && Utils.qqCode.get(qqId) != null && Utils.qqBind.get(qqId).equalsIgnoreCase(player.getName()) && Utils.qqCode.get(qqId).equals(code)){
                            MiraiMC.addBind(player.getUniqueId(),qqId);
                            for (String s : Arrays.asList("&a已成功添加绑定！", "&a如需取消绑定，请联系管理员！")) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',s));
                            }
                        } else player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&c无法核对您的信息，请检查您的输入或重新发起绑定！"));
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
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&a配置文件已重新加载，并已清空待验证队列！"));
                    } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&c你没有足够的权限执行此命令！"));
                    break;
                }
                default:{
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6&lLink&b&l2&e&lQQ&r &a插件菜单"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/link2qq bind <QQ号>:&r 添加一个绑定"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/link2qq verify <QQ号> <绑定码>:&r 确认绑定"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/link2qq reload:&r 重新加载配置文件"));
                    break;
                }
            }
        } else sender.sendMessage("This server is running "+getDescription().getName()+" version "+getDescription().getVersion()+" by "+ getDescription().getAuthors().toString().replace("[","").replace("]","")+" (MiraiMC version "+Bukkit.getPluginManager().getPlugin("MiraiMC").getDescription().getVersion()+")");
        return true;
    }
}
