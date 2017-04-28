
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

public class ProductRecomend extends BaseEntity
{
	@SerializedName("data")
	public ProductItem[] Data;
}
