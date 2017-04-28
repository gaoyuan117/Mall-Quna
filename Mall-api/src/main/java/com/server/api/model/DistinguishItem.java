
package com.server.api.model;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class DistinguishItem implements Serializable
{

	public String id;

	public String checkup_no;

	public String uid;

	public String title;
	
	public String category_id;

	public int quantity;

	public String description;

	public String province_id;

	public String city_id;

	public String area_id;

	public String address;
	
	public String address_full;

	public String mobile;

	public String realname;

	public String create_time;

	public String status;

	public String is_show;

	public String[] image;
	
	public Image[] images;

	public Movie[] movie;
	
	public String status_text;

	public BigDecimal shipping_price;

	public BigDecimal total;

	public BigDecimal identify_price;
}
