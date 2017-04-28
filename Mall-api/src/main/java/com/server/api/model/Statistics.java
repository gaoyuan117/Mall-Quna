package com.server.api.model;

import com.google.gson.annotations.SerializedName;


public class Statistics extends BaseEntity
{
	@SerializedName("data")
	public Data Data;
	
	public static  class Data
	{
		public String collect_product;
		
		public String collect_shop;
		
		public String collect_browse;
		
		public String order_no_pay;
		
		public String order_no_shipments;
		
		public String order_no_receipt;
		
		public String order_no_comment;
		
		public String order_goods_return;
		
	}
}
