package com.server.api.model;

import com.google.gson.annotations.SerializedName;


public class NewsPageResult extends BaseEntity
{
	@SerializedName("data")
	public Data Data;
	
	public static class Data
	{
		@SerializedName("total")
		public int Total;
		
		@SerializedName("result")
		public NewsItem[] Result;
	}
}
