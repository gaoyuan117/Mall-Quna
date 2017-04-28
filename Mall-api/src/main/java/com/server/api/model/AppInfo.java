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
public class AppInfo extends BaseEntity 
{
	public Data data;

	public static class Data
	{
		public String mobile_bbs_website;

		public String kefu_phone;

		public String about_app;
	}
}