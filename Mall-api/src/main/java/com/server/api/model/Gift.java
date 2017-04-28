
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

public class Gift extends BaseEntity
{

	@SerializedName("data")
	public Data Data;

	public static class Data
	{
		public GiftItem zhangsheng;

		public GiftItem xiaomuzhi;
	}

	public static class GiftItem
	{
        public int total;

		public int unused;
	}
}
