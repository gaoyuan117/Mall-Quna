
package com.server.api.model;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class WalletRecordItem implements Serializable
{

	public String id;

	public String create_time;

	public String sign;
	
	public String title;
	
	public String desc;

	public BigDecimal money;
	
	public String status;
	
	public String status_text;
	
}
