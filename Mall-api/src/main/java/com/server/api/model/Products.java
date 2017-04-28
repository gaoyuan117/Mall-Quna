
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

public class Products extends BaseEntity
{
	@SerializedName("data")
	public ProductPageResult Data;
}
