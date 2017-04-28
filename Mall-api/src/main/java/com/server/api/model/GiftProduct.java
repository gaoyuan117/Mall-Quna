
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

public class GiftProduct
{

	public int code;

	public String message;

	@SerializedName("data")
	public GiftProdcutItem[] Data;

	public static class GiftProdcutItem
	{

		public String id;

		public String special_gift_id;

	}
}
