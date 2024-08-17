package me.dreamvoid.link2qq.bungee.command;

import me.dreamvoid.link2qq.Utils;
import me.dreamvoid.link2qq.bungee.BungeePlugin;
import me.dreamvoid.miraimc.api.MiraiMC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class link2qq extends Command {
    private final BungeePlugin plugin;

    public link2qq(BungeePlugin plugin, String name) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length > 0){
            switch (args[0].toLowerCase()){
                case "bind": {
                    if(args.length >=2 && sender instanceof ProxiedPlayer){
                        ProxiedPlayer player = (ProxiedPlayer)sender;
                        long qqId = Long.parseLong(args[1]);
                        Utils.playerBind.remove(player.getName());
                        Utils.playerCode.remove(player.getName());
                        Utils.playerBind.put(player.getName(),qqId);
                        Utils.playerCode.put(player.getName(),Utils.getRandomString(plugin.config.bot.confirmCodeLength));
                        String verify = plugin.config.bot.confirmBindCommand + " "+ player.getName() + " "+ Utils.playerCode.get(player.getName());
                        TextComponent message = new TextComponent(new TextComponent(ChatColor.AQUA + verify));
                        message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, verify));
                        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("点击置入编辑框以复制")));

                        player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',"&e请使用你的QQ私聊机器人或向机器人所在群发送以下消息：")));
                        player.sendMessage(message);
                        player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',"&e以完成验证流程 &7(点击可复制)")));
                    }
                    break;
                }
                case "verify":{
                    if(args.length >=3 && sender instanceof ProxiedPlayer) {
                        ProxiedPlayer player = (ProxiedPlayer)sender;
                        long qqId = Long.parseLong(args[1]);
                        String code = args[2];
                        if(Utils.qqBind.get(qqId) != null && Utils.qqCode.get(qqId) != null && Utils.qqBind.get(qqId).equalsIgnoreCase(player.getName()) && Utils.qqCode.get(qqId).equals(code)){
                            MiraiMC.Bind.addBind(player.getUniqueId(),qqId);
                            player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&a已成功添加绑定！")));
                            player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&a如需取消绑定，请联系管理员！")));
                        } else player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',"&c无法核对您的信息，请检查您的输入或重新发起绑定！")));
                    }
                    break;
                }
                case "reload":{
                    if(sender.hasPermission("miraimc.command.link2qq.reload")){
                        plugin.loadConfig();
                        Utils.qqCode.clear();
                        Utils.qqBind.clear();
                        Utils.playerBind.clear();
                        Utils.playerCode.clear();
                        sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',"&a配置文件已重新加载，并已清空待验证队列！")));
                    } else sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',"&c你没有足够的权限执行此命令！")));
                    break;
                }
                default:{
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',"&6&lLink&b&l2&e&lQQ&r &a插件菜单")));
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',"&6/link2qq bind <QQ号>:&r 添加一个绑定")));
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',"&6/link2qq verify <QQ号> <绑定码>:&r 确认绑定")));
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',"&6/link2qq reload:&r 重新加载配置文件")));
                    break;
                }
            }
        } else sender.sendMessage(TextComponent.fromLegacyText("This server is running "+ plugin.getDescription().getName()+" version "+ plugin.getDescription().getVersion()+" by "+ plugin.getDescription().getAuthor()));
    }
}
