
package com.android.juzbao.util;

import android.content.Context;
import android.text.TextUtils;

import com.server.api.model.BaseEntity;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;

public class CommonValidate
{

	public static boolean validateQueryState(Context context, MessageData msg,
			BaseEntity entity)
	{
		return validateQueryState(context, msg, entity, null);
	}

	public static boolean validateQueryState(Context context, MessageData msg,
			BaseEntity entity, String failMsg)
	{
		if (null == entity)
		{
			if (!TextUtils.isEmpty(failMsg))
			{
				ShowMsg.showToast(context, msg, failMsg);
			}
			return false;
		}

		if (entity.code == ErrorCode.INT_QUERY_DATA_SUCCESS)
		{
			return true;
		}
		else
		{
			if (!TextUtils.isEmpty(failMsg))
			{
				ShowMsg.showToast(context, msg, entity.message);
			}
			return false;
		}
	}
}
