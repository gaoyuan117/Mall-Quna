
package com.android.juzbao.enumerate;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:OrderByEnum
 * @Description: 订单状态
 * @author:
 * @date: 2013-6-26
 */
public enum DistinguishStatus
{
    ALL("全部", ""),
    
	/** 待付款 */
	PAY("待付款", "0"),
	
	/** 待发货 */
	DELIVERY("待发出", "1"),
	
	/** 已发出 */
	SENDED("已发出", "2"),
	
	/** 正在鉴定 */
	DISTINGUISH("正在鉴定", "3"),
	
	/** 待收货 */
	RECEIPT("待收货", "4"),

	/** 已完成 */
	COMPLETE("已完成 ", "5"),
	
	/** 已取消 */
	CANCEL("已取消", "6");

	DistinguishStatus(String name, String value)
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

	public static List<DistinguishStatus> toList()
	{
		DistinguishStatus[] distances = DistinguishStatus.values();
		List<DistinguishStatus> listDistances = new ArrayList<DistinguishStatus>();
		for (int i = 0; i < distances.length; i++)
		{
			listDistances.add(distances[i]);
		}

		return listDistances;
	}
	
	private String mstrName;

	private String mstrValue;
}
