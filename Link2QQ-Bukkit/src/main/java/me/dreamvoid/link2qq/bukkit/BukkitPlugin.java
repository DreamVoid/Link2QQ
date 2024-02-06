package me.dreamvoid.link2qq.bukkit;

import com.google.gson.Gson;
import me.dreamvoid.link2qq.SerializableConfig;
import me.dreamvoid.link2qq.Utils;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

public class BukkitPlugin extends JavaPlugin implements Listener {
    private SerializableConfig config;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        try {
            config = new Gson().fromJson(new FileReader(new File(getDataFolder(), "config.yml")), SerializableConfig.class);
        } catch (FileNotFoundException e) {
            getLogger().severe("Failed to load configuration file, reason: " + e);
        }
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onFriendMessageReceive(MiraiFriendMessageEvent e){
        if(config.bot.botAccounts.contains(e.getBotID())){
            getServer().getScheduler().runTaskAsynchronously(this, () -> {
                String[] args = e.getMessage().split(" ");
                if(args[0].equals(config.bot.addBindCommand)){
                    if(args.length >= 2){
                        if(config.general.allowOverride || MiraiMC.getBind(e.getSenderID()) == null){
                            Utils.qqBind.put(e.getSenderID(),args[1]);
                            Utils.qqCode.put(e.getSenderID(),Utils.getRandomString(config.bot.confirmCodeLength));
                        } else {
                            e.reply("您已绑定了一个Minecraft账号！");
                        }
                        e.reply("请在游戏内输入指令以完成绑定流程：" + System.lineSeparator() + String.format("/link2qq verify %d %s", e.getSenderID(), Utils.qqCode.get(e.getSenderID())));
                    } else {
                        e.reply("参数不足，请检查消息内容！");
                    }
                } else if(args[0].equals(config.bot.confirmBindCommand)){
                    if(args.length >= 3){
                        String name = args[1];
                        String code = args[2];
                        if(Utils.playerBind.containsKey(name) && Utils.playerCode.containsKey(name) && Utils.playerBind.get(name).equals(e.getSenderID()) && Utils.playerCode.get(name).equals(code)){
                            MiraiMC.addBind(Bukkit.getOfflinePlayer(name).getUniqueId(), e.getSenderID());
                            e.reply("已成功链接到您的Minecraft账号！");
                            Utils.playerBind.remove(name);
                            Utils.playerCode.remove(name);
                        } else {
                            e.reply("无法核对您的信息，请检查您的输入或重新发起绑定！");
                        }
                    } else {
                        e.reply("参数不足，请检查消息内容！");
                    }
                }
            });
        }
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e){
        if(config.bot.botAccounts.contains(e.getBotID()) && config.bot.groupIds.contains(e.getGroupID())){
            getServer().getScheduler().runTaskAsynchronously(this, () -> {
                String[] args = e.getMessage().split(" ");
                if(args[0].equals(config.bot.addBindCommand)){
                    if(args.length >= 2){
                        if(config.general.allowOverride || MiraiMC.getBind(e.getSenderID()) == null){
                            Utils.qqBind.put(e.getSenderID(), args[1]);
                            Utils.qqCode.put(e.getSenderID(), Utils.getRandomString(config.bot.confirmCodeLength));
                            e.reply("请在游戏内输入指令以完成绑定流程：" + System.lineSeparator() + String.format("/link2qq verify %d %s", e.getSenderID(), Utils.qqCode.get(e.getSenderID())));
                        } else {
                            e.reply("您已绑定了一个Minecraft账号！");
                        }
                    } else {
                        e.reply("参数不足，请检查消息内容！");
                    }
                } else if(args[0].equals(config.bot.confirmBindCommand)){
                    if(args.length >= 3){
                        String name = args[1];
                        String code = args[2];
                        if(Utils.playerBind.containsKey(name) && Utils.playerCode.containsKey(name) && Utils.playerBind.get(name).equals(e.getSenderID()) && Utils.playerCode.get(name).equals(code)){
                            MiraiMC.addBind(Bukkit.getOfflinePlayer(name).getUniqueId(), e.getSenderID());
                            e.reply("已成功链接到您的Minecraft账号！");
                            Utils.playerBind.remove(name);
                            Utils.playerCode.remove(name);
                        } else {
                            e.reply("无法核对您的信息，请检查您的输入或重新发起绑定！");
                        }
                    } else {
                        e.reply("参数不足，请检查消息内容！");
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
                        if(config.general.allowOverride || MiraiMC.getBind(player.getUniqueId()) == 0) {
                            long qqId = Long.parseLong(args[1]);
                            Utils.playerBind.put(player.getName(), qqId);
                            Utils.playerCode.put(player.getName(), Utils.getRandomString(config.bot.confirmCodeLength));
                            String verify = String.format("%s %s %s", config.bot.confirmBindCommand, player.getName(), Utils.playerCode.get(player.getName()));
                            TextComponent message = new TextComponent(ChatColor.AQUA + verify);
                            message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, verify));
                            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("点击置入编辑框以复制")).create()));

                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e请使用你的QQ私聊机器人或向机器人所在群发送以下消息："));
                            player.spigot().sendMessage(message);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e以完成验证流程 &7(点击可复制)"));
                        } else {
                            player.sendMessage(ChatColor.RED + "您已绑定了一个QQ账号！");
                        }
                    }
                    break;
                }
                case "verify":{
                    if(args.length >=3 && sender instanceof Player) {
                        Player player = (Player) sender;
                        long qqId = Long.parseLong(args[1]);
                        String code = args[2];
                        if(Utils.qqBind.containsKey(qqId) && Utils.qqCode.containsKey(qqId) && Utils.qqBind.get(qqId).equalsIgnoreCase(player.getName()) && Utils.qqCode.get(qqId).equals(code)){
                            MiraiMC.addBind(player.getUniqueId(),qqId);
                            for (String s : Arrays.asList("&a已成功添加绑定！", "&a如需取消绑定，请联系管理员！")) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',s));
                            }
                            Utils.qqBind.remove(qqId);
                            Utils.qqCode.remove(qqId);
                        } else {
                            player.sendMessage(ChatColor.RED + "无法核对您的信息，请检查您的输入或重新发起绑定！");
                        }
                    }
                    break;
                }
                case "reload":{
                    if(sender.hasPermission("miraimc.command.link2qq.reload")){
                        onLoad();
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
        } else {
            sender.sendMessage("This server is running "+getDescription().getName()+" version "+getDescription().getVersion()+" by "+ getDescription().getAuthors().toString().replace("[","").replace("]","")+" (MiraiMC version "+Bukkit.getPluginManager().getPlugin("MiraiMC").getDescription().getVersion()+")");
        }
        return true;
    }
}
