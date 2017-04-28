
package com.android.juzbao.enumerate;

public enum VerifyType
{
	MODIFY_MOBILE("modifymobile"), REGISTER("register"), FORGETTEN("forgotten");
	
	private String value;

	VerifyType(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}
}
