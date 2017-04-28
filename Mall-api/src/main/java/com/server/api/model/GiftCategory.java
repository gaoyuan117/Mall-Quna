
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

public class GiftCategory
{

	public int code;

	public String message;

	@SerializedName("data")
	public GiftCategoryItem[] Data;

	public static class GiftCategoryItem
	{

		public String id;

		public String title;

		public String sort;

		public String image;
	}
}
