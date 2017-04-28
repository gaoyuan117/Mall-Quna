
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

public class ArticleDetail extends BaseEntity
{

	@SerializedName("data")
	public ArticleInfo Data;

}
