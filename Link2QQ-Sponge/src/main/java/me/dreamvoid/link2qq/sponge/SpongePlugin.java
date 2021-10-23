package me.dreamvoid.link2qq.sponge;

import com.google.inject.Inject;
import me.dreamvoid.miraimc.api.*;
import me.dreamvoid.miraimc.sponge.event.*;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.*;
import org.spongepowered.api.command.args.*;
import org.spongepowered.api.command.spec.*;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.plugin.*;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Plugin(id = "link2qq", name = "Link2QQ", version = "1.1", description = "Link your minecraft account to QQ.", dependencies = @Dependency(id = "miraimc"))
public class SpongePlugin implements CommandExecutor {

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File dataFolder;

    @Inject
    private PluginContainer pluginContainer;

    /**
     * 触发 GamePreInitializationEvent 时，插件准备进行初始化，这时默认的 Logger 已经准备好被调用，同时你也可以开始引用配置文件中的内容。
     */
    @Listener
    public void onLoad(GamePreInitializationEvent e) {
        try {
            new Config(this).loadConfig();
        } catch (IOException ioException) {
            getLogger().warn("An error occurred while loading plugin.");
            ioException.printStackTrace();
        }
    }

    /**
     * 触发 GameStartingServerEvent 时，服务器初始化和世界载入都已经完成，你应该在这时注册插件命令。
     */
    @Listener
    public void onServerLoaded(GameStartingServerEvent e) {
        CommandSpec link2qq = CommandSpec.builder().description(Text.of("MiraiMC Bot Command.")).permission("miraimc.command.mirai").executor(this)
                .arguments(GenericArguments.remainingJoinedStrings((Text.of("args"))))
                .build();
        Sponge.getCommandManager().register(this, link2qq, "link2qq");
    }

    @Listener
    public void onFriendMessageReceive(MiraiFriendMessageEvent e){
        if(Config.Bot_Id.contains(e.getBotID())){
            Task.builder().async().name("Link2QQ Friend Message").execute(() -> {
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
                        if(Utils.playerBind.get(name) != null && Utils.playerCode.get(name) != null && Utils.playerBind.get(name) == (e.getSenderID()) && Utils.playerCode.get(name).equals(code)){
                            MiraiMC.addBinding(Sponge.getServer().getPlayer(name).get().getUniqueId().toString(), e.getSenderID());
                            MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("已成功添加绑定！如需更换绑定，请直接发起新的绑定；如需取消绑定，请联系管理员！");
                        } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("无法核对您的信息，请检查您的输入或重新发起绑定！");
                    } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("参数不足，请检查消息内容！");
                }
            }).submit(this);
        }
    }

    @Listener
    public void onGroupMessageReceive(MiraiGroupMessageEvent e){
        if(Config.Bot_Id.contains(e.getBotID()) && Config.Bot_Group.contains(e.getGroupID())){
            Task.builder().async().name("Link2QQ Group Message").execute(() -> {
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
                        if(Utils.playerBind.get(name) != null && Utils.playerCode.get(name) != null && Utils.playerBind.get(name) == (e.getSenderID()) && Utils.playerCode.get(name).equals(code)){
                            MiraiMC.addBinding(Sponge.getServer().getPlayer(name).get().getUniqueId().toString(), e.getSenderID());
                            MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage("已成功添加绑定！如需更换绑定，请直接发起新的绑定；如需取消绑定，请联系管理员！");
                            Utils.playerBind.remove(name);
                            Utils.playerCode.remove(name);
                        } else MiraiBot.getBot(e.getBotID()).getFriend(e.getSenderID()).sendMessage("无法核对您的信息，请检查您的输入或重新发起绑定！");
                    } else MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessage("参数不足，请检查消息内容！");
                }
            }).submit(this);
        }
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }

    public Logger getLogger() {
        return logger;
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext arg) throws CommandException {
        if(arg.<String>getOne("args").isPresent()){
            String argo = arg.<String>getOne("args").get();
            String[] args = argo.split("\\s+");
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
                        player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&e请使用你的QQ私聊机器人或向机器人所在群发送以下消息："));
                        player.sendMessage(Text.of(TextColors.AQUA + verify));
                        player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&e以完成验证流程 &7(点击可复制)"));
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
                                player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(s));
                            }
                        } else player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&c无法核对您的信息，请检查您的输入或重新发起绑定！"));
                    }
                    break;
                }
                case "reload":{
                    if(sender.hasPermission("miraimc.command.link2qq.reload")){
                        try {
                            Config.reloadConfig();
                            Utils.qqCode.clear();
                            Utils.qqBind.clear();
                            Utils.playerBind.clear();
                            Utils.playerCode.clear();
                            sender.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&a配置文件已重新加载，并已清空待验证队列！"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else sender.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&c你没有足够的权限执行此命令！"));
                    break;
                }
                default:{
                    sender.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&6&lLink&b&l2&e&lQQ&r &a插件菜单"));
                    sender.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&6/link2qq bind <QQ号>:&r 添加一个绑定"));
                    sender.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&6/link2qq verify <QQ号> <绑定码>:&r 确认绑定"));
                    sender.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&6/link2qq reload:&r 重新加载配置文件"));
                    break;
                }
            }
            return CommandResult.builder().successCount(1).build();
        } else throw new ArgumentParseException(Text.of("isPresent() returned false!"),"Link2QQ",0);
    }
}
