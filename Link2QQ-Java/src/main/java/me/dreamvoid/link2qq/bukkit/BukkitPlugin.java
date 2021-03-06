package me.dreamvoid.link2qq.bukkit;

import me.dreamvoid.link2qq.Config;
import me.dreamvoid.link2qq.Utils;
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
    private BukkitConfig PluginConfig;

    @Override
    public void onLoad() {
        PluginConfig = new BukkitConfig(this);
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
        if(Config.Bot.BotAccounts.contains(e.getBotID())){
            new BukkitRunnable() {
                @Override
                public void run() {
                    String[] args = e.getMessage().split(" ");
                    if(args[0].equals(Config.Bot.AddBindCommand)){
                        if(args.length >= 2){
                            Utils.qqBind.remove(e.getSenderID());
                            Utils.qqCode.remove(e.getSenderID());
                            Utils.qqBind.put(e.getSenderID(),args[1]);
                            Utils.qqCode.put(e.getSenderID(),Utils.getRandomString(Config.Bot.ConfirmCodeLength));
                            for (String s : Arrays.asList("???????????????????????????????????????????????????", "/link2qq verify " + e.getSenderID() + " " + Utils.qqCode.get(e.getSenderID()))) {
                                MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage(s);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                            }
                        } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("???????????????????????????????????????");
                    }
                    if(args[0].equals(Config.Bot.ConfirmBindCommand)){
                        if(args.length >= 3){
                            String name = args[1];
                            String code = args[2];
                            if(Utils.playerBind.get(name) != null && Utils.playerCode.get(name) != null && Utils.playerBind.get(name) == (e.getSenderID()) && Utils.playerCode.get(name).equals(code)){
                                MiraiMC.addBind(Bukkit.getOfflinePlayer(name).getUniqueId(), e.getSenderID());
                                MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
                            } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("????????????????????????????????????????????????????????????????????????");
                        } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("???????????????????????????????????????");
                    }
                }
            }.runTaskAsynchronously(this);
        }
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e){
        if(Config.Bot.BotAccounts.contains(e.getBotID()) && Config.Bot.GroupIds.contains(e.getGroupID())){
            new BukkitRunnable() {
                @Override
                public void run() {
                    String[] args = e.getMessage().split(" ");
                    if(args[0].equals(Config.Bot.AddBindCommand)){
                        if(args.length >= 2){
                            Utils.qqBind.remove(e.getSenderID());
                            Utils.qqCode.remove(e.getSenderID());
                            Utils.qqBind.put(e.getSenderID(),args[1]);
                            Utils.qqCode.put(e.getSenderID(),Utils.getRandomString(Config.Bot.ConfirmCodeLength));
                            for (String s : Arrays.asList("???????????????????????????????????????????????????", "/link2qq verify " + e.getSenderID() + " " + Utils.qqCode.get(e.getSenderID()))) {
                                MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage(s);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                            }
                        } else MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage("???????????????????????????????????????");
                    }
                    if(args[0].equals(Config.Bot.ConfirmBindCommand)){
                        if(args.length >= 3){
                            String name = args[1];
                            String code = args[2];
                            if(Utils.playerBind.get(name) != null && Utils.playerCode.get(name) != null && Utils.playerBind.get(name) == (e.getSenderID()) && Utils.playerCode.get(name).equals(code)){
                                MiraiMC.addBind(Bukkit.getOfflinePlayer(name).getUniqueId(), e.getSenderID());
                                MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage("?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
                                Utils.playerBind.remove(name);
                                Utils.playerCode.remove(name);
                            } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("????????????????????????????????????????????????????????????????????????");
                        } else MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage("???????????????????????????????????????");
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
                        Utils.playerCode.put(player.getName(),Utils.getRandomString(Config.Bot.ConfirmCodeLength));
                        String verify = Config.Bot.ConfirmBindCommand + " "+ player.getName() + " "+ Utils.playerCode.get(player.getName());
                        TextComponent message = new TextComponent(ChatColor.AQUA + verify);
                        message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, verify));
                        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("??????????????????????????????")).create()));

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&e???????????????QQ????????????????????????????????????????????????????????????"));
                        player.spigot().sendMessage(message);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&e????????????????????? &7(???????????????)"));
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
                            for (String s : Arrays.asList("&a????????????????????????", "&a??????????????????????????????????????????")) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',s));
                            }
                        } else player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&c????????????????????????????????????????????????????????????????????????"));
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
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&a????????????????????????????????????????????????????????????"));
                    } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&c??????????????????????????????????????????"));
                    break;
                }
                default:{
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6&lLink&b&l2&e&lQQ&r &a????????????"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/link2qq bind <QQ???>:&r ??????????????????"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/link2qq verify <QQ???> <?????????>:&r ????????????"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/link2qq reload:&r ????????????????????????"));
                    break;
                }
            }
        } else sender.sendMessage("This server is running "+getDescription().getName()+" version "+getDescription().getVersion()+" by "+ getDescription().getAuthors().toString().replace("[","").replace("]","")+" (MiraiMC version "+Bukkit.getPluginManager().getPlugin("MiraiMC").getDescription().getVersion()+")");
        return true;
    }
}
