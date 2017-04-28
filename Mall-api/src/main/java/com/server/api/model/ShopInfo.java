
package com.server.api.model;

import java.math.BigDecimal;

public class ShopInfo extends BaseEntity
{

	public Data data;

	public static class Data
	{
		public String id;

		public String uid;

		public String level;

		public String title;

		public String gift_zhangsheng;

		public String gift_xiaomuzhi;

		public String headpic;

		public String signpic;

		public String description;

		public String create_time;
		
		public String create_time_format;

		public String update_time;

		public String apply_uid;

		public String status;

		public String headpic_path;

		public String signpic_path;

		public BigDecimal amount;

		public String order_count;

		public String visitor_count;
		
		public String im_account;
		
		public String applause_Rate;
	}
}
