package com.server.api.model;

import java.io.Serializable;
import java.math.BigDecimal;


@SuppressWarnings("serial")
public class ProductFavorite implements Serializable
{
	public String id;
	
	public String product_id;
	
	public String uid;
	
	public String image;
	
	public String product_title;
	
	public BigDecimal product_price;
}
