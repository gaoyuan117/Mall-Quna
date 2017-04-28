
package com.android.juzbao.model;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.server.api.model.DistinguishItem;
import com.server.api.service.DistinguishService.AddDistinguishRequest;
import com.android.juzbao.activity.DistinguishEnsureActivity_;
import com.android.juzbao.dao.DistinguishDao;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.http.HttpDataLoader;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ShowMsg;

public class DistinguishBusiness
{

	public static boolean addDistinguish(Context context,
			HttpDataLoader httpDataLoader, AddDistinguishRequest request)
	{
		if (TextUtils.isEmpty(request.Title))
		{
			ShowMsg.showToast(context, "请输入宝贝名称");
			return false;
		}

		if (request.CategoryId <= 0)
		{
			ShowMsg.showToast(context, "请选择商品分类");
			return false;
		}

		if (request.Quantity <= 0)
		{
			ShowMsg.showToast(context, "请输入数量");
			return false;
		}

		if (request.ProvinceId <= 0)
		{
			ShowMsg.showToast(context, "请发货地区");
			return false;
		}

		if (TextUtils.isEmpty(request.Address))
		{
			ShowMsg.showToast(context, "请输入详细地址");
			return false;
		}

		if (TextUtils.isEmpty(request.Mobile))
		{
			ShowMsg.showToast(context, "请输入联系电话");
			return false;
		}

		if (TextUtils.isEmpty(request.Realname))
		{
			ShowMsg.showToast(context, "请输入姓名");
			return false;
		}

		DistinguishDao.sendCmdQueryAddDistinguish(httpDataLoader, request);
		return true;
	}

	public static void queryDistinguish(HttpDataLoader httpDataLoader,
			int page, String status)
	{
		DistinguishDao.sendCmdQueryQueryDistinguish(httpDataLoader, page, status);
	}

	public static void queryDelDistinguish(HttpDataLoader httpDataLoader,
			String distinguishId)
	{
		DistinguishDao.sendCmdDelDistinguish(httpDataLoader, distinguishId);
	}

	public static void queryAllDistinguish(HttpDataLoader httpDataLoader,
			int page)
	{
		DistinguishDao.sendCmdQueryQueryAllDistinguish(httpDataLoader, page);
	}

	public static void queryDistinguishDetail(HttpDataLoader httpDataLoader,
			String distinguishId)
	{
		DistinguishDao
				.sendCmdQueryDistinguishDetail(httpDataLoader, distinguishId);
	}

	public static boolean queryDistinguishShippment(Context context, HttpDataLoader httpDataLoader,
			String distinguishId, String shippingCode, String code,
			String[] coverIds)
	{
		if (TextUtils.isEmpty(shippingCode))
		{
			ShowMsg.showToast(context, "请选择物流公司");
			return false;
		}
		
		if (TextUtils.isEmpty(code))
		{
			ShowMsg.showToast(context, "请输入物流单号");
			return false;
		}
		
		DistinguishDao.sendCmdQueryDistinguishShippment(httpDataLoader,
				distinguishId, shippingCode, code, coverIds);
		return true;
	}

	public static void intentToDistinguishEnsureActivity(Context context,
			DistinguishItem distinguishItem, String id)
	{
		Bundle bundle = new Bundle();
		if (null != distinguishItem)
		{
			bundle.putString("item",
					JsonSerializerFactory.Create().encode(distinguishItem));
		}
		bundle.putString("id", id);
		((BaseActivity) context).getIntentHandle().intentToActivity(bundle,
				DistinguishEnsureActivity_.class);
	}
}
