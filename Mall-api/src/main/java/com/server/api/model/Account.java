
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

public class Account extends BaseEntity
{

	@SerializedName("data")
	public Data Data;

	public static class Data
	{

		public String nickname;

		public String sex;

		public String level;

		public String birthday;

		public String qq;

		public String score;

		public String login;

		public String reg_ip;

		public String reg_time;

		public String last_login_ip;

		public String last_login_time;

		public String status;

		public String avatar;

		public String freeze_amount;

		public String amount;

		public String avatar_path;

		public String username;

		public String email;
	}
}
