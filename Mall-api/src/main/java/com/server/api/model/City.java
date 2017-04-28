package com.server.api.model;

import com.google.gson.annotations.SerializedName;


public class City
{
	public int code;

	public String message;
	
	@SerializedName("data")
	public Data[] Data;
	
	public static  class Data
	{
		public String id;
		
		public String city_id;
		
		public String city;
	}
}
