package com.server.api.model;

import com.google.gson.annotations.SerializedName;


public class Province
{
	public int code;

	public String message;
	
	@SerializedName("data")
	public Data[] Data;
	
	public static  class Data
	{
		public String id;
		
		public String province_id;
		
		public String province;
	}
}
