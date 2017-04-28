package com.server.api.service;

import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CartService {

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Cart/addTocart", method = Method.Post, encoder = WebFormEncoder.class)
	public static class AddToCartRequest extends Endpoint {

		@SerializedName("product_id")
		public int ProductId;
		
		@SerializedName("quantity")
		public int Quantity;
		
		@SerializedName("option_ids")
		public String option_ids;
		
		/** 价格(单价*数量) */
		@SerializedName("price")
		public BigDecimal Price;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Cart/delCartbyId", method = Method.Post, encoder = WebFormEncoder.class)
	public static class DelCartByIdRequest extends Endpoint {

		@SerializedName("cart_id")
		public int CartId;
		
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Cart/editCart", method = Method.Post, encoder = WebFormEncoder.class)
	public static class EditCartRequest extends Endpoint {

		@SerializedName("cart_id")
		public int CartId;
		
		@SerializedName("quantity")
		public int Quantity;
		
		/** 价格(单价*数量) */
		@SerializedName("price")
		public BigDecimal Price;
		
		@SerializedName("option_ids")
		public String option_ids;
		
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Cart/getCarts", method = Method.Post, encoder = WebFormEncoder.class)
	public static class QueryCartsRequest extends Endpoint {

		@SerializedName("page")
		public int Page;
		
		@SerializedName("page_size")
		public int PageSize;
		
	}

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/cart/toTrade", method = Method.Post, encoder = WebFormEncoder.class)
	public static class ToTradeRequest extends Endpoint {

		@SerializedName("cart_id[]")
		public Integer[] CartIds;
		
	}
}