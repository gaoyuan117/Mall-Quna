package com.server.api.model;

import com.google.gson.annotations.SerializedName;


public class PaincTimes extends BaseEntity
{
	@SerializedName("data")
	public Data[] Data;
	
	public static  class Data
	{
		public String id;
		
		public String start_time;
		
		public String end_time;
	}
}
