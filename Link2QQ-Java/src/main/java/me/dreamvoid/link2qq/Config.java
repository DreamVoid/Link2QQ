package me.dreamvoid.link2qq;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class Config {
	public static class Bot {
		@SerializedName("bot-accounts")
		public List<Long> BotAccounts = Arrays.asList(123456L, 789012L);
		@SerializedName("group-ids")
		public List<Long> GroupIds = Arrays.asList(123456L, 789012L);
		@SerializedName("add-bind-command")
		public String AddBindCommand = "添加绑定";
		@SerializedName("confirm-bind-command")
		public String ConfirmBindCommand = "确认绑定";
		@SerializedName("confirm-code-length")
		public int ConfirmCodeLength = 6;
	}
	@SerializedName("bot")
	public static Bot Bot = new Bot();
}
