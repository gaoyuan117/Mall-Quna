package com.server.api.service;

import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.google.gson.annotations.SerializedName;

public class AddressService {

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Public/jzbAddress", method = Method.Post, encoder = WebFormEncoder.class)
	public static class JZBAddressRequest extends Endpoint {

	}

	/**
	 * 获取省份列表
	 */
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Public/getProvinces", method = Method.Post, encoder = WebFormEncoder.class)
	public static class GetProvincesRequest extends Endpoint {

	}

	/**
	 * 获取城市列表
	 */
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Public/getCities", method = Method.Post, encoder = WebFormEncoder.class)
	public static class GetCitiesRequest extends Endpoint {
		
		@SerializedName("province_id")
		public String ProvinceId;
	}

	/**
	 * 获取区县列表
	 */
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Public/getAreas", method = Method.Post, encoder = WebFormEncoder.class)
	public static class GetAreasRequest extends Endpoint {
		
		@SerializedName("city_id")
		public String CityId;
	}

	/**
	 * 获取收货地址列表
	 */
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/getReceiveAddrs", method = Method.Post, encoder = WebFormEncoder.class)
	public static class GetReceiverAddressRequest extends Endpoint {

	}

	/**
	 * 删除收货地址
	 */
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/delReceiveAddr", method = Method.Post, encoder = WebFormEncoder.class)
	public static class DelReceiverAddressRequest extends Endpoint {

		@SerializedName("id")
		public String Id;
	}

	/**
	 * 添加收货地址di
 	 */
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/addReceiveAddr", method = Method.Post, encoder = WebFormEncoder.class)
	public static class AddReceiverAddressRequest extends Endpoint {
		
		@SerializedName("mobile")
		public String Mobile;
		
		@SerializedName("realname")
		public String Realname;
		
		@SerializedName("province_id")
		public String ProvinceId;
		
		@SerializedName("city_id")
		public String CityId;
		
		@SerializedName("area_id")
		public String AreaId;
		
		@SerializedName("address")
		public String Address;
		
		@SerializedName("is_default")
		public String IsDefault;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/editReceiveAddr", method = Method.Post, encoder = WebFormEncoder.class)
	public static class EditReceiverAddressRequest extends Endpoint {
		
		@SerializedName("id")
		public int Id;
		
		@SerializedName("mobile")
		public String Mobile;
		
		@SerializedName("realname")
		public String Realname;
		
		@SerializedName("province_id")
		public String ProvinceId;
		
		@SerializedName("city_id")
		public String CityId;
		
		@SerializedName("area_id")
		public String AreaId;
		
		@SerializedName("address")
		public String Address;
		
		@SerializedName("is_default")
		public String IsDefault;
	}
}