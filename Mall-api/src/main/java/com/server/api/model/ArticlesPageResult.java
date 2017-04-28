
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

public class ArticlesPageResult extends BaseEntity
{

	@SerializedName("data")
	public ArticleItem[] Data;
	
}
