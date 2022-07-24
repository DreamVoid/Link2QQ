package me.dreamvoid.link2qq;

import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {
    public static HashMap<String, Long> playerBind = new HashMap<>(); //玩家名，QQ号
    public static HashMap<Long, String> qqBind = new HashMap<>(); //QQ号，玩家名
    public static HashMap<String, String> playerCode = new HashMap<>(); //玩家名，验证码
    public static HashMap<Long, String> qqCode = new HashMap<>(); //QQ号，验证码

    public static String getRandomString(int length){
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return IntStream.range(0, length).map(i -> new Random().nextInt(62)).mapToObj(number -> String.valueOf(str.charAt(number))).collect(Collectors.joining());
    }
}
