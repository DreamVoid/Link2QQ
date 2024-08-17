package me.dreamvoid.link2qq.config;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class SerializableConfig {
	@SerializedName("general")
	public General general;

	public static class General{
		@SerializedName("allow-override")
		public boolean allowOverride = false;
	}

	@SerializedName("bot")
	public Bot bot;

	public static class Bot {
		@SerializedName("bot-accounts")
		public List<Long> botAccounts = Arrays.asList(123456L, 789012L);
		@SerializedName("group-ids")
		public List<Long> groupIds = Arrays.asList(123456L, 789012L);
		@SerializedName("add-bind-command")
		public String addBindCommand = "添加绑定";
		@SerializedName("confirm-bind-command")
		public String confirmBindCommand = "确认绑定";
		@SerializedName("confirm-code-length")
		public int confirmCodeLength = 6;
	}
}
