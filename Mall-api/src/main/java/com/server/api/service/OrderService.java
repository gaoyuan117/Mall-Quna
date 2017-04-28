package com.server.api.service;


import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;


public class OrderService
{
	/**
	 * 查询订单
	 */
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/order/getmyorders", method = Method.Post, encoder = WebFormEncoder.class)
	public static class QueryOrderRequest extends Endpoint {

		@SerializedName("page")
		public int page;

		@SerializedName("page_size")
		public int page_size;

		@SerializedName("status")
		public String status;
	}

	/**
	 * 提交订单（购物车提交）
	 */
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/submitOrder", method = Method.Post, encoder = WebFormEncoder.class)
	public static class SubmitOrderRequest extends Endpoint {

		@SerializedName("cids")
		public String Cids;

		@SerializedName("receiver_id")
		public String Address;

	}

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/confirmOrder", method = Method.Post, encoder = WebFormEncoder.class)
	public static class ConfirmOrderRequest extends Endpoint {

		@SerializedName("id")
		public String OrderId;

	}

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/cancelOrder", method = Method.Post, encoder = WebFormEncoder.class)
	public static class CancelOrderRequest extends Endpoint {

		@SerializedName("id")
		public String id;

	}

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/deleteOrder", method = Method.Post, encoder = WebFormEncoder.class)
	public static class DelOrderRequest extends Endpoint {

		@SerializedName("id")
		public String id;

	}

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/order/getmyorders", method = Method.Post, encoder = WebFormEncoder.class)
	public static class QueryAllOrderRequest extends Endpoint {
		
		@SerializedName("page")
		public int page;
		
		@SerializedName("page_size")
		public int page_size;

		//订单状态
		@SerializedName("status")
		public String status;
	}

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/myshop/getMyOrders", method = Method.Post, encoder = WebFormEncoder.class)
	public static class QueryShopOrderRequest extends Endpoint {

		@SerializedName("page")
		public int page;
		
		@SerializedName("page_size")
		public int page_size;
		
		@SerializedName("status")
		public String status;
	}

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/myshop/getMyOrders", method = Method.Post, encoder = WebFormEncoder.class)
	public static class QueryShopAllOrderRequest extends Endpoint {
		
		@SerializedName("page")
		public int page;
		
		@SerializedName("page_size")
		public int page_size;
	}

	/**
	 * 查询订单详情(会员)
	 */
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/getOrderDetail", method = Method.Post, encoder = WebFormEncoder.class)
	public static class OrderDetailRequest extends Endpoint {
		
		@SerializedName("id")
		public String id;

	}

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/getOrderDetail", method = Method.Post, encoder = WebFormEncoder.class)
	public static class ShopOrderDetailRequest extends Endpoint {
		
		@SerializedName("id")
		public String order_id;
	}

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/confirmProduct", method = Method.Post, encoder = WebFormEncoder.class)
	public static class ReceiveOrderRequest extends Endpoint {
		
		@SerializedName("order_id")
		public String OrderId;
		
	}

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/shippingOrder", method = Method.Post, encoder = WebFormEncoder.class)
	public static class ShippingOrderRequest extends Endpoint {

		@SerializedName("id")
		public String id;

	}

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Myshop/shipments", method = Method.Post, encoder = WebFormEncoder.class)
	public static class OrderShipmentRequest extends Endpoint {
		
		@SerializedName("order_id")
		public String OrderId;
		
		@SerializedName("shipping_code")
		public String ShippingCode;
		
		@SerializedName("code")
		public String Code;
		
		@SerializedName("cover_id[]")
		public String[] CoverIds;
		
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/applyforReturn", method = Method.Post, encoder = WebFormEncoder.class)
	public static class ReturnOrderRequest extends Endpoint {
		
		@SerializedName("order_id")
		public String OrderId;
		
		@SerializedName("product_id")
		public String ProductId;
		
		@SerializedName("type")
		public String Type;
		
		@SerializedName("money")
		public BigDecimal Money;
		
		@SerializedName("explain")
		public String Explain;
		
		@SerializedName("reason")
		public String Reason;
		
		@SerializedName("cover_ids[]")
		public String[] CoverIds;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Myshop/refundOperation", method = Method.Post, encoder = WebFormEncoder.class)
	public static class RefundOperationRequest extends Endpoint {
		
		@SerializedName("order_id")
		public String OrderId;
		
		@SerializedName("product_id")
		public String ProductId;
		
		@SerializedName("status")
		public String Status;
	}

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Myshop/refundOperation", method = Method.Post, encoder = WebFormEncoder.class)
	public static class RefuseRefundOperationRequest extends Endpoint {

		@SerializedName("order_id")
		public String OrderId;

		@SerializedName("product_id")
		public String ProductId;

		@SerializedName("status")
		public String Status;
	}
}
