package com.server.api.service;

import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.google.gson.annotations.SerializedName;


public class DistinguishService
{
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Checkup/add", method = Method.Post, encoder = WebFormEncoder.class)
	public static class AddDistinguishRequest extends Endpoint {

		@SerializedName("category_id")
		public int CategoryId;
		
		@SerializedName("quantity")
		public int Quantity;
		
		@SerializedName("province_id")
		public int ProvinceId;
		
		@SerializedName("city_id")
		public int CityId;
		
		@SerializedName("area_id")
		public int AreaId;
		
		@SerializedName("title")
		public String Title;
		
		@SerializedName("description")
		public String Description;
		
		@SerializedName("address")
		public String Address;
		
		@SerializedName("mobile")
		public String Mobile;
		
		@SerializedName("realname")
		public String Realname;
		
		@SerializedName("pic[]")
		public Integer[] Pics;
		
		@SerializedName("movie[]")
		public int Movie;
		
		@SerializedName("movie_thumb_id")
		public int MovieThumbId;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/checkup/MyCheckupList", method = Method.Post, encoder = WebFormEncoder.class)
	public static class QueryOrderRequest extends Endpoint {
		
		@SerializedName("page")
		public int Page;
		
		@SerializedName("page_size")
		public int Pagesize;
		
		@SerializedName("status")
		public String Status;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/checkup/MyCheckupList", method = Method.Post, encoder = WebFormEncoder.class)
	public static class QueryAllDistinguishRequest extends Endpoint {
		
		@SerializedName("page")
		public int Page;
		
		@SerializedName("page_size")
		public int Pagesize;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/checkup/CheckupDetail", method = Method.Post, encoder = WebFormEncoder.class)
	public static class QueryDistinguishDetailRequest extends Endpoint {
		
		@SerializedName("checkup_id")
		public String DistinguishId;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Checkup/delCheckup", method = Method.Post, encoder = WebFormEncoder.class)
	public static class DelDistinguishRequest extends Endpoint {
		
		@SerializedName("checkup_id")
		public String DistinguishId;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Checkup/Shipments", method = Method.Post, encoder = WebFormEncoder.class)
	public static class DistinguishShipmentRequest extends Endpoint {
		
		@SerializedName("checkup_id")
		public String DistinguishId;
	
		@SerializedName("shipping_code")
		public String ShippingCode;
		
		@SerializedName("code")
		public String Code;
		
		@SerializedName("cover_id[]")
		public String[] CoverIds;
	}
}
