package com.server.api.service;

import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.google.gson.annotations.SerializedName;


public class ShopService
{
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Myshop/shopProtocol", method = Method.Post, encoder = WebFormEncoder.class)
	public static class ShopProtocolRequest extends Endpoint {

	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Myshop/myshopInfo", method = Method.Post, encoder = WebFormEncoder.class)
	public static class ShopInfoRequest extends Endpoint {

	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/public/services", method = Method.Post, encoder = WebFormEncoder.class)
	public static class OnlineServiceRequest extends Endpoint {

	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/LearningCenter/getList", method = Method.Post, encoder = WebFormEncoder.class)
	public static class CourseListRequest extends Endpoint {
		
		@SerializedName("page")
		public int Page;
		
		@SerializedName("pagesize")
		public int Pagesize;
		
		@SerializedName("type")
		public String Type;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/LearningCenter/getDetail", method = Method.Post, encoder = WebFormEncoder.class)
	public static class CourseDetailRequest extends Endpoint {

		@SerializedName("id")
		public String Id;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Myshop/createShop", method = Method.Post, encoder = WebFormEncoder.class)
	public static class CreateShopRequest extends Endpoint {

		@SerializedName("title")
		public String Title;
		
		@SerializedName("description")
		public String Description;
		
		@SerializedName("headpic")
		public String Headpic;
		
		@SerializedName("signpic")
		public String Signpic;
	}
	
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Myshop/settingMyshop", method = Method.Post, encoder = WebFormEncoder.class)
	public static class EditShopRequest extends Endpoint {

		@SerializedName("shop_id")
		public String ShopId;
		
		@SerializedName("title")
		public String Title;
		
		@SerializedName("description")
		public String Description;
		
		@SerializedName("headpic")
		public String Headpic;
		
		@SerializedName("signpic")
		public String Signpic;
	}
}
