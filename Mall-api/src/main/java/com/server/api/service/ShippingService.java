package com.server.api.service;

import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.google.gson.annotations.SerializedName;


public class ShippingService
{
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Public/shippingList", method = Method.Post, encoder = WebFormEncoder.class)
	public static class ShippingListRequest extends Endpoint {

	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Trace/traceDetail", method = Method.Post, encoder = WebFormEncoder.class)
	public static class OrderShippingDetailRequest extends Endpoint {

		@SerializedName("order_id")
		public String OrderId;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Trace/traceDetail", method = Method.Post, encoder = WebFormEncoder.class)
	public static class DistinguishShippingDetailRequest extends Endpoint {

		@SerializedName("checkup_id")
		public String DistinguishId;
	}
}
