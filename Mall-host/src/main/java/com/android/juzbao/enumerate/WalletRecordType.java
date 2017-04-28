
package com.android.juzbao.enumerate;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:OrderByEnum
 * @Description: 订单状态
 * @author:
 * @date: 2013-6-26
 */
public enum WalletRecordType
{
	/** 充值 */
	IN_RECHARGE("充值", "0"),
	
	/** 收货款 */
	IN_INCOME("收货款", "1"),
	
	/** 解冻 */
	IN_UNFREEZE("解冻", "2"),

	/** 提现 */
	OUT_WITHDRAW("提现", "3"),
	
	/** 消费 */
	OUT_PAYMENT("消费", "4"),
	
	/** 冻结 */
    OUT_FREEZE("冻结 ", "8");
	
	WalletRecordType(String name, String value)
	{
		mstrName = name;
		mstrValue = value;
	}

	public String getName()
	{
		return mstrName;
	}

	public void setName(String strName)
	{
		this.mstrName = strName;
	}

	public String getValue()
	{
		return mstrValue;
	}

	public void setValue(String strValue)
	{
		this.mstrValue = strValue;
	}

	public static List<WalletRecordType> toList()
	{
		WalletRecordType[] distances = WalletRecordType.values();
		List<WalletRecordType> listDistances = new ArrayList<WalletRecordType>();
		for (int i = 0; i < distances.length; i++)
		{
			listDistances.add(distances[i]);
		}

		return listDistances;
	}
	
	private String mstrName;

	private String mstrValue;
}
