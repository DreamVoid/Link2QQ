# Link2QQ
将Minecraft账号与QQ绑定

## 介绍
Link2QQ 是一个基于[MiraiMC](https://github.com/DreamVoid/MiraiMC)的插件，能够让你的玩家绑定他们的QQ，同时支持Bukkit和BungeeCord使用。

## 下载
* [Github 发布页](https://github.com/DreamVoid/Link2QQ/releases)
* [Gitee 发布页](https://gitee.com/dreamvoid/Link2QQ/releases) (中国)
* [MCBBS](https://www.mcbbs.net/thread-1234632-1-1.html) (中国)

## 开始使用
* 下载插件，并将插件文件放入plugins文件夹
* 下载[MiraiMC](https://github.com/DreamVoid/MiraiMC)插件（如果尚未下载），并将插件文件放入plugins文件夹
* 启动服务端（如果尚未启动）或使用诸如PlugMan的插件加载插件
* 使用指令“**/mirai login <账号> <密码>**”登录你的机器人账号
* 调整插件的配置文件
* 以管理员或控制台身份输入指令“**/link2qq reload**”
* 享受优雅的QQ机器人服务！

## 指令和权限
### 指令
| 命令 | 描述 | 权限 |
| ---------------------------- | ---------------------- | ---------- |
| /link2qq bind <QQ号> | 发起一个QQ号绑定 | miraimc.command.link2qq |
| /link2qq verify <QQ号> <绑定码> | 确认绑定一个QQ号 | miraimc.command.link2qq |
| /link2qq reload | 重新加载配置并取消待验证的绑定 | miraimc.command.link2qq.reload |

### 权限
| 权限节点 | 描述 | 默认 |
| ---------------------------- | ---------------------- | ---------- |
| miraimc.command.link2qq | 允许使用 /link2qq | YES |
| miraimc.command.link2qq.reload | 允许使用 /link2qq reload | OP |
