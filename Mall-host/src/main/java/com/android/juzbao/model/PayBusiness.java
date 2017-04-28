
package com.android.juzbao.model;

import java.math.BigDecimal;

import android.content.Context;
import android.text.TextUtils;

import com.android.juzbao.dao.PayDao;
import com.android.zcomponent.http.HttpDataLoader;
import com.android.zcomponent.util.ShowMsg;

public class PayBusiness
{

	public static void queryOrderPay(HttpDataLoader httpDataLoader,
			String payCode, String orderId, String orderNo, BigDecimal price)
	{
		PayDao.sendCmdOrderPay(httpDataLoader, orderId, orderNo, payCode, price);
	}

	public static void queryDistinguishPay(HttpDataLoader httpDataLoader,
			String payCode, String disId, String disNo, BigDecimal price)
	{
		PayDao.sendCmdDistinguishPay(httpDataLoader, disId, disNo, payCode, price);
	}

	public static void queryOrderBalancePay(HttpDataLoader httpDataLoader,
			String orderId, String orderNo, BigDecimal price)
	{
		PayDao.sendCmdOrderBalancePay(httpDataLoader, orderId, orderNo, price);
	}

	public static void queryDistinguishBalancePay(
			HttpDataLoader httpDataLoader, String disId, String disNo,
			BigDecimal price)
	{
		PayDao.sendCmdDistinguishBalancePay(httpDataLoader, disId, disNo, price);
	}

	public static boolean queryPayRecharge(Context context,
			HttpDataLoader httpDataLoader, String payCode, String price)
	{
		if (TextUtils.isEmpty(price))
		{
			ShowMsg.showToast(context, "请输入充值金额");
			return false;
		}

		if (new BigDecimal(price).doubleValue() == 0)
		{
			ShowMsg.showToast(context, "请充值需大于零");
			return false;
		}

		PayDao.sendCmdRecharge(httpDataLoader, payCode, new BigDecimal(price));
		return true;
	}
}
