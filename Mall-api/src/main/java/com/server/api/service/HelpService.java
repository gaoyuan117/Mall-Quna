package com.server.api.service;


import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.google.gson.annotations.SerializedName;


public class HelpService
{
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Help/postFeedback", method = Method.Post, encoder = WebFormEncoder.class)
	public static class FeedbackRequest extends Endpoint {

		@SerializedName("content")
		public String Content;
	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Help/articles", method = Method.Post, encoder = WebFormEncoder.class)
	public static class ArticlesRequest extends Endpoint {

	}
	
	@HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Help/articleDetail", method = Method.Post, encoder = WebFormEncoder.class)
	public static class ArticlesDetailRequest extends Endpoint {

		@SerializedName("id")
		public String Id;
	}
}
