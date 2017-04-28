package com.server.api.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("serial")
public class ProductFavoritePageResult extends BaseEntity implements Serializable
{
	public Data data;
	
	public static class Data
	{
		@SerializedName("total")
		public int Total;
		
		@SerializedName("result")
		public ProductFavorite[] Result;
	}
}
