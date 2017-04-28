package com.server.api.service;

import java.math.BigDecimal;

import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.google.gson.annotations.SerializedName;


public class PayService
{
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Pay/pay", method = Method.Post, encoder = WebFormEncoder.class)
	public static class PayOrderRequest extends Endpoint {
		
		@SerializedName("order_id")
		public String OrderId;
		
		@SerializedName("order_no")
		public String OrderNo;
		
		@SerializedName("price")
		public BigDecimal Price;
		
		@SerializedName("paycode")
		public String Paycode;
		
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Pay/pay", method = Method.Post, encoder = WebFormEncoder.class)
	public static class PayDistinguishRequest extends Endpoint {
		
		@SerializedName("checkup_id")
		public String DistinguishId;
		
		@SerializedName("checkup_no")
		public String DistinguishNo;
		
		@SerializedName("price")
		public BigDecimal Price;
		
		@SerializedName("paycode")
		public String Paycode;
		
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Pay/balancePay", method = Method.Post, encoder = WebFormEncoder.class)
	public static class BalancePayOrderRequest extends Endpoint {
		
		@SerializedName("order_id")
		public String OrderId;
		
		@SerializedName("order_no")
		public String OrderNo;
		
		@SerializedName("price")
		public BigDecimal Price;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Pay/balancePay", method = Method.Post, encoder = WebFormEncoder.class)
	public static class BalancePayDistinguishRequest extends Endpoint {
		
		@SerializedName("checkup_id")
		public String DistinguishId;
		
		@SerializedName("checkup_no")
		public String DistinguishNo;
		
		@SerializedName("price")
		public BigDecimal Price;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Pay/recharge", method = Method.Post, encoder = WebFormEncoder.class)
	public static class RechargeRequest extends Endpoint {
		
		@SerializedName("paycode")
		public String Paycode;
		
		@SerializedName("money")
		public BigDecimal Money;
	}
}
