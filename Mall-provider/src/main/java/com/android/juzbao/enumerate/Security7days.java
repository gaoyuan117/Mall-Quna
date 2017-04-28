
package com.android.juzbao.enumerate;

public enum Security7days
{
	NOT_SUPPORT(0), SUPPORT(1);

	private int value;

	Security7days(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}
}
