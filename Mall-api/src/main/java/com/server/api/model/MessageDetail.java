package com.server.api.model;

import com.google.gson.annotations.SerializedName;


public class MessageDetail extends BaseEntity
{
	@SerializedName("data")
	public MessageItem Data;
	
}
