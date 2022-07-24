package me.dreamvoid.link2qq.bungee;

import java.util.HashMap;
import java.util.Random;

public class Utils {
    public static HashMap<String, Long> playerBind = new HashMap<>(); //玩家名，QQ号
    public static HashMap<Long, String> qqBind = new HashMap<>(); //QQ号，玩家名
    public static HashMap<String, String> playerCode = new HashMap<>(); //玩家名，验证码
    public static HashMap<Long, String> qqCode = new HashMap<>(); //QQ号，验证码

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
