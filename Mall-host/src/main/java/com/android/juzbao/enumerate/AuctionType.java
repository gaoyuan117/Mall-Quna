
package com.android.juzbao.enumerate;

public enum AuctionType
{
	START("start"), END("end"), ZERO("zero");

	private String value;

	AuctionType(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}
}
