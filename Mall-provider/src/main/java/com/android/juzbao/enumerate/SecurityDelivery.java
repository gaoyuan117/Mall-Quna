
package com.android.juzbao.enumerate;

public enum SecurityDelivery
{
	NULL(0), HOUR24(1), HOUR48(2), HOUR72(3);

	private int value;

	SecurityDelivery(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}
}
