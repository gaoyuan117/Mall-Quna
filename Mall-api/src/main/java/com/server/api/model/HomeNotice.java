
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HomeNotice extends BaseEntity implements Serializable
{
	@SerializedName("data")
	public Data Data;
	
    public static class Data
    {
		@SerializedName("total")
		public int Total;

    	@SerializedName("result")
    	public NoticeItem[] Results;
    	
    }
    
	public static class NoticeItem
	{

		public String id;

		public String title;

		public String content;

		public String create_time;

		public String update_time;
	}
}
