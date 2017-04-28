package com.server.api.service;

import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.google.gson.annotations.SerializedName;


public class FavoriteService
{
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Collect/addProduct", method = Method.Post, encoder = WebFormEncoder.class)
	public static class AddProductFavoriteRequest extends Endpoint {
		
		@SerializedName("product_id")
		public int ProductId;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Collect/getCollectIdByProduct", method = Method.Post, encoder = WebFormEncoder.class)
	public static class IsProductFavoritedRequest extends Endpoint {
		
		@SerializedName("product_id")
		public int ProductId;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Collect/productCollectDel", method = Method.Post, encoder = WebFormEncoder.class)
	public static class DelProductFavoriteRequest extends Endpoint {
		
		@SerializedName("id")
		public int Id;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Collect/collectProducts", method = Method.Post, encoder = WebFormEncoder.class)
	public static class FavoriteProductsRequest extends Endpoint {
		
		@SerializedName("page")
		public int Page;
		
		@SerializedName("pagesize")
		public int Pagesize;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Collect/myBrowse", method = Method.Post, encoder = WebFormEncoder.class)
	public static class BrowseProductsRequest extends Endpoint {
		
		@SerializedName("page")
		public int Page;
		
		@SerializedName("pagesize")
		public int Pagesize;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Collect/borwseCollectDel", method = Method.Post, encoder = WebFormEncoder.class)
	public static class DelProductBrowseRequest extends Endpoint {
		
		@SerializedName("id")
		public int Id;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Collect/addShop", method = Method.Post, encoder = WebFormEncoder.class)
	public static class AddShopFavoriteRequest extends Endpoint {
		
		@SerializedName("shop_id")
		public int ShopId;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Collect/getCollectIdByShop", method = Method.Post, encoder = WebFormEncoder.class)
	public static class IsShopFavoriteRequest extends Endpoint {
		
		@SerializedName("shop_id")
		public int ShopId;
	}
	
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Collect/shopCollectDel", method = Method.Post, encoder = WebFormEncoder.class)
	public static class DelShopFavoriteRequest extends Endpoint {
		
		@SerializedName("id")
		public int Id;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Collect/collectShops", method = Method.Post, encoder = WebFormEncoder.class)
	public static class FavoriteShopsRequest extends Endpoint {
		
		@SerializedName("page")
		public int Page;
		
		@SerializedName("pagesize")
		public int Pagesize;
	}
}
