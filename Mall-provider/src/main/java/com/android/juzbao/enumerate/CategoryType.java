
package com.android.juzbao.enumerate;

public enum CategoryType
{
	PRODCUT("product"), FREE("free");

	private String value;

	CategoryType(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}
}
