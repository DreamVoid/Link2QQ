package me.dreamvoid.link2qq;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.dreamvoid.link2qq.config.SerializableConfig;
import me.dreamvoid.miraimc.api.MiraiMC;
import me.dreamvoid.miraimc.velocity.event.message.passive.MiraiFriendMessageEvent;
import me.dreamvoid.miraimc.velocity.event.message.passive.MiraiGroupMessageEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
        id = "link2qq",
        name = "Link2QQ",
        version = "1.2"
)
public class VelocityPlugin {

    @Inject
    private Logger logger;

    @Inject
    @DataDirectory
    private Path dataDirectory;

    @Inject
    private ProxyServer server;

    private File configFile;
    private SerializableConfig config;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Loading configuration.");
        loadConfig();

        logger.info("Registering commands.");
        CommandManager manager = server.getCommandManager();
        CommandMeta link2qq = manager.metaBuilder("link2qq").build();
        manager.register(link2qq, (SimpleCommand) invocation -> {
            CommandSource sender = invocation.source();
            String[] args = invocation.arguments();

            if(args.length > 0){
                switch (args[0].toLowerCase()){
                    case "bind": {
                        if(args.length >=2 && sender instanceof Player){
                            Player player = (Player)sender;
                            if(config.general.allowOverride || MiraiMC.Bind.getBind(player.getUniqueId()) == 0) {
                                long qqId = Long.parseLong(args[1]);
                                Utils.playerBind.put(player.getUsername(), qqId);
                                Utils.playerCode.put(player.getUsername(), Utils.getRandomString(config.bot.confirmCodeLength));
                                String verify = String.format("%s %s %s", config.bot.confirmBindCommand, player.getUsername(), Utils.playerCode.get(player.getUsername()));

                                player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>请使用你的QQ私聊机器人或向机器人所在群发送以下消息：\n" +
                                        "<aqua><click:suggest_command:" + verify + ">" + verify + "</click>\n" +
                                        "<yellow>以完成验证流程 <gray>(点击可复制)"));
                            } else {
                                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>您已绑定了一个QQ账号！"));
                            }
                        }
                        break;
                    }
                    case "verify":{
                        if(args.length >=3 && sender instanceof Player) {
                            Player player = (Player) sender;
                            long qqId = Long.parseLong(args[1]);
                            String code = args[2];
                            if(Utils.qqBind.containsKey(qqId) && Utils.qqCode.containsKey(qqId) && Utils.qqBind.get(qqId).equalsIgnoreCase(player.getUsername()) && Utils.qqCode.get(qqId).equals(code)){
                                MiraiMC.Bind.addBind(player.getUniqueId(),qqId);
                                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>已成功添加绑定！"));
                                Utils.qqBind.remove(qqId);
                                Utils.qqCode.remove(qqId);
                            } else {
                                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>无法核对您的信息，请检查您的输入或重新发起绑定！"));
                            }
                        }
                        break;
                    }
                    case "reload":{
                        if(sender.hasPermission("miraimc.command.link2qq.reload")){
                            loadConfig();
                            Utils.qqCode.clear();
                            Utils.qqBind.clear();
                            Utils.playerBind.clear();
                            Utils.playerCode.clear();
                            sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>配置文件已重新加载，并已清空待验证队列！"));
                        } else {
                            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>你没有足够的权限执行此命令！"));
                        }
                        break;
                    }
                    default:{
                        sender.sendMessage(MiniMessage.miniMessage().deserialize("<gold><bold>Link<aqua><bold>2<yellow><bold>QQ<reset> <green>插件菜单"));
                        sender.sendMessage(MiniMessage.miniMessage().deserialize("<gold>/link2qq bind <QQ号>:<reset> 添加一个绑定"));
                        sender.sendMessage(MiniMessage.miniMessage().deserialize("<gold>/link2qq verify <QQ号> <绑定码>:<reset> 确认绑定"));
                        sender.sendMessage(MiniMessage.miniMessage().deserialize("<gold>/link2qq reload:<reset> 重新加载配置文件"));
                        break;
                    }
                }
            }
        });

    }

    @Subscribe
    public void onFriendMessage(MiraiFriendMessageEvent e){
        if(config.bot.botAccounts.contains(e.getBotID())){
            server.getScheduler().buildTask(this, () -> {
                String[] args = e.getMessage().split(" ");
                if(args[0].equals(config.bot.addBindCommand)){
                    if(args.length >= 2){
                        if(config.general.allowOverride || MiraiMC.Bind.getBind(e.getSenderID()) == null){
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
                            try{
                                MiraiMC.Bind.addBind(server.getPlayer(name).orElseThrow(() -> new RuntimeException("玩家不在线")).getUniqueId(), e.getSenderID());
                                e.reply("已成功链接到您的Minecraft账号！");
                                Utils.playerBind.remove(name);
                                Utils.playerCode.remove(name);
                            } catch (RuntimeException ex){
                                e.reply("无法链接您的Minecraft账号，原因：" + ex.getMessage());
                            }
                        } else {
                            e.reply("无法核对您的信息，请检查您的输入或重新发起绑定！");
                        }
                    } else {
                        e.reply("参数不足，请检查消息内容！");
                    }
                }
            }).schedule();
        }
    }

    @Subscribe
    public void onGroupMessageReceive(MiraiGroupMessageEvent e){
        if(config.bot.botAccounts.contains(e.getBotID()) && config.bot.groupIds.contains(e.getGroupID())){
            server.getScheduler().buildTask(this, () -> {
                String[] args = e.getMessage().split(" ");
                if(args[0].equals(config.bot.addBindCommand)){
                    if(args.length >= 2){
                        if(config.general.allowOverride || MiraiMC.Bind.getBind(e.getSenderID()) == null){
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
                            try{
                                MiraiMC.Bind.addBind(server.getPlayer(name).orElseThrow(() -> new RuntimeException("玩家不在线")).getUniqueId(), e.getSenderID());
                                e.reply("已成功链接到您的Minecraft账号！");
                                Utils.playerBind.remove(name);
                                Utils.playerCode.remove(name);
                            } catch (RuntimeException ex){
                                e.reply("无法链接您的Minecraft账号，原因：" + ex.getMessage());
                            }
                        } else {
                            e.reply("无法核对您的信息，请检查您的输入或重新发起绑定！");
                        }
                    } else {
                        e.reply("参数不足，请检查消息内容！");
                    }
                }
            }).schedule();
        }
    }

    private void loadConfig(){
        if(!dataDirectory.toFile().exists() && !dataDirectory.toFile().mkdirs()){
            logger.warn("Failed to create data directory!");
        }

        configFile = new File(dataDirectory.toFile(), "config.yml");

        try {
            if(!configFile.exists()){
                try (InputStream is = getClass().getResourceAsStream("/config.yml")) {
                    assert is != null;
                    Files.copy(is, configFile.toPath());
                }
            }
            config = new Gson().fromJson(new Yaml().load(Files.newInputStream(configFile.toPath())).toString(), SerializableConfig.class); // 先转为json，再用gson解析，权宜之计
        } catch (IOException e) {
            logger.error("Failed to load configuration file!", e);
        }
    }
}
