
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

public class Address
{

	public int code;

	public String message;

	@SerializedName("data")
	public Data[] Data;

	public static class Data
	{
		public String id;
		
		public String mobile;

		//姓名
		public String realname;
		
		public String is_default;

		public String address;
		
		public String province_id;

		public String province;

		public String city_id;

		public String city;

		public String area_id;

		public String area;
	}
}
