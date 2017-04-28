
package com.android.juzbao.dao;

import java.math.BigDecimal;

import com.server.api.model.CommonReturn;
import com.server.api.model.Payment;
import com.server.api.service.PayService;
import com.android.zcomponent.http.HttpDataLoader;

public class PayDao
{

	public static void sendCmdOrderPay(HttpDataLoader httpDataLoader,
			String orderId, String orderNo, String payCode, BigDecimal price)
	{
		PayService.PayOrderRequest request = new PayService.PayOrderRequest();
		request.OrderId = orderId;
		request.OrderNo = orderNo;
		request.Price = price;
		request.Paycode = payCode;
		httpDataLoader.doPostProcess(request, Payment.class);
	}

	public static void sendCmdDistinguishPay(HttpDataLoader httpDataLoader,
			String disId, String disNo, String payCode, BigDecimal price)
	{
		PayService.PayDistinguishRequest request =
				new PayService.PayDistinguishRequest();
		request.DistinguishId = disId;
		request.DistinguishNo = disNo;
		request.Price = price;
		request.Paycode = payCode;
		httpDataLoader.doPostProcess(request, Payment.class);
	}

	public static void sendCmdOrderBalancePay(HttpDataLoader httpDataLoader,
			String orderId, String orderNo, BigDecimal price)
	{
		PayService.BalancePayOrderRequest request =
				new PayService.BalancePayOrderRequest();
		request.OrderId = orderId;
		request.OrderNo = orderNo;
		request.Price = price;
		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}

	public static void sendCmdDistinguishBalancePay(
			HttpDataLoader httpDataLoader, String disId, String disNo, BigDecimal price)
	{
		PayService.BalancePayDistinguishRequest request =
				new PayService.BalancePayDistinguishRequest();
		request.DistinguishId = disId;
		request.DistinguishNo = disNo;
		request.Price = price;
		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}

	public static void sendCmdRecharge(HttpDataLoader httpDataLoader,
			String payCode, BigDecimal price)
	{
		PayService.RechargeRequest request = new PayService.RechargeRequest();
		request.Money = price;
		request.Paycode = payCode;
		httpDataLoader.doPostProcess(request, Payment.class);
	}
}
