package com.android.juzbao.enumerate;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:SpecialType
 * @Description: 专题类型
 * @author:
 */
public enum SpecialType
{
    GIFT("选礼物", "gift"),
    
    PANIC("抢购会", "panic");
    
	SpecialType(String name, String value)
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

	public static List<SpecialType> toList()
	{
		SpecialType[] distances = SpecialType.values();
		List<SpecialType> listDistances = new ArrayList<SpecialType>();
		for (int i = 0; i < distances.length; i++)
		{
			listDistances.add(distances[i]);
		}

		return listDistances;
	}
	
	private String mstrName;

	private String mstrValue;
}
