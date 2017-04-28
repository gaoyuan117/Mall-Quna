package com.android.juzbao.enumerate;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:GoodsStatus
 * @Description: 商品类型状态
 * @author:
 */
public enum GoodsStatus
{
    PUTONG("商品管理", "putong"),
    
	/** 待付款 */
	PAIPIN("拍品管理", "paipin"),
	
	/** 待发货 */
	DINGZHI("定制管理", "dingzhi"),
    
    XIANZHI("需求管理", "xianzhi");
    
	GoodsStatus(String name, String value)
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

	public static List<GoodsStatus> toList()
	{
		GoodsStatus[] distances = GoodsStatus.values();
		List<GoodsStatus> listDistances = new ArrayList<GoodsStatus>();
		for (int i = 0; i < distances.length; i++)
		{
			listDistances.add(distances[i]);
		}

		return listDistances;
	}
	
	private String mstrName;

	private String mstrValue;
}
