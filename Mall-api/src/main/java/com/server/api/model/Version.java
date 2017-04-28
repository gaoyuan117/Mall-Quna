/*
 * Copyright 2015-2023 sundy.net. All rights reserved.
* Support: http://www.3dfield.net
 * License: http://www.3dfield.net/license
 */
package com.server.api.model;


/**
 * Entity - 版本
 * 
 * @author Administrator
 * @version 3.0
 */
public class Version extends BaseEntity 
{
	public Data[] data;

	public static class Data
	{
		/** 应用类型 */
		public String os;

		/** 名称 */
		public String title;

		/** 版本编号 */
		public Integer number;

		/** 版本hash */
		public String hash;

		/** 版本uri */
		public String url;

		/** 是否强制更新 */
		public String mask;
		
		/** 描述*/
		public String description;
		
		public String create_time;
		
		public String update_time;
	}
}