package com.server.api.service;

import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.google.gson.annotations.SerializedName;


public class MessageService
{

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Message/messageList", method = Method.Post, encoder = WebFormEncoder.class)
	public static class MessageListRequest extends Endpoint {

		@SerializedName("page")
		public int Page;
		
		@SerializedName("pagesize")
		public int Pagesize;
		
		@SerializedName("type")
		public String Type;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Message/messageDetail", method = Method.Post, encoder = WebFormEncoder.class)
	public static class MessageDetailRequest extends Endpoint {

		@SerializedName("id")
		public String Id;
	}

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/News/getList", method = Method.Post, encoder = WebFormEncoder.class)
	public static class NewsListRequest extends Endpoint {

		@SerializedName("page")
		public int Page;

		@SerializedName("pagesize")
		public int Pagesize;

		@SerializedName("type")
		public String Type;

		@SerializedName("isHot")
		public String isHot;
	}

	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/News/getDetail", method = Method.Post, encoder = WebFormEncoder.class)
	public static class NewsDetailRequest extends Endpoint {

		@SerializedName("id")
		public String Id;
	}
}
