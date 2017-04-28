package com.server.api.model;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;



public class MyWallet extends BaseEntity
{
	@SerializedName("data")
	public Data Data;
	
	public static class Data
	{
		public String uid;
		
		public BigDecimal amount;

		public BigDecimal freeze_amount;
		
	}
}
