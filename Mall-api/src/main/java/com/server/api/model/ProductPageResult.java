package com.server.api.model;

import com.google.gson.annotations.SerializedName;


public class ProductPageResult extends BaseEntity
{
	@SerializedName("total")
	public int Total;

	@SerializedName("result")
	public ProductItem[] Results;
}
