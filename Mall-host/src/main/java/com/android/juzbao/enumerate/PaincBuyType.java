
package com.android.juzbao.enumerate;

public enum PaincBuyType
{
	BUYING("buying"), COUPON("coupon"), TIME("time");

	private String value;

	PaincBuyType(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}
}
