package com.server.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * 加入订单返回对象
 */
public class CartTradeResult extends BaseEntity {

    @SerializedName("data")
    public CartItem[] Data;

//	public Data data;
//
//	public static class Data {
//
//		public int total_price;
//
//		public Cart[] cart;
//
//		public static class Cart {
//
//			public String shop_title;
//
//			public Product[] product;
//
//			public static class Product {
//
//				public String cart_id;
//
//				public String title;
//
//				public String price;
//
//				public String quantity;
//
//				public int total_price;
//
//				public String option_ids;
//
//				public String product_attr;
//
//				public String image;
//			}
//		}
//	}

}
