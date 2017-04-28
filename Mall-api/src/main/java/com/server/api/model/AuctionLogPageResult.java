package com.server.api.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("serial")
public class AuctionLogPageResult extends BaseEntity implements Serializable
{
	@SerializedName("data")
	public Data Data;
	
	public static class Data
	{
		@SerializedName("total")
		public int Total;
		
		@SerializedName("result")
		public AuctionLogItem[] Result;
	}
}
