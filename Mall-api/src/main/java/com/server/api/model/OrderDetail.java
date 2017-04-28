package com.server.api.model;

import com.google.gson.annotations.SerializedName;


public class OrderDetail extends BaseEntity
{
	@SerializedName("data")
	public Order Data;

}
