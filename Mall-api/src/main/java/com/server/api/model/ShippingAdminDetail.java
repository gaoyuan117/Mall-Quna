
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

public class ShippingAdminDetail extends BaseEntity
{

	@SerializedName("data")
	public Data Data;

	public static class Data
	{
		public ShippingItem admin_shipping ;
	}
}
