
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

public class AddOrderResult extends BaseEntity
{

	@SerializedName("data")
	public Data[] Data;

	public static class Data
	{

		@SerializedName("id")
		public String Id;

		@SerializedName("order_no")
		public String OrderNo;
	}
}
