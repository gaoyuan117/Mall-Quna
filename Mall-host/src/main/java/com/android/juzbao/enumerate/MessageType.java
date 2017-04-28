
package com.android.juzbao.enumerate;

public enum MessageType
{
	/** 物流消息 */
	SHIPPING("1"), 
	
	/** 交易消息 */
	ORDER("2"),
	
	/** 通知消息 */
	NOTIFY("3"),
	
	/** 定时消息 */
	TIME("4"),
	
	/** 活动消息 */
	ACTIVITY("5");
	
	private String value;

	MessageType(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}
}
