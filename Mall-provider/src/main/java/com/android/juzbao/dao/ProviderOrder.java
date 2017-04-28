
package com.android.juzbao.dao;

import com.server.api.model.CommonReturn;
import com.server.api.model.OrderDetail;
import com.server.api.model.OrderPageResult;
import com.server.api.service.OrderService;
import com.android.zcomponent.constant.GlobalConst;
import com.android.zcomponent.http.HttpDataLoader;

public class ProviderOrder
{

	public static void sendCmdQueryQueryProviderOrder(
			HttpDataLoader httpDataLoader, int page, String status)
	{
		OrderService.QueryShopOrderRequest request = new OrderService.QueryShopOrderRequest();
		request.status = status;
		request.page = page;
		request.page_size = GlobalConst.INT_NUM_PAGE;

		httpDataLoader.doPostProcess(request, OrderPageResult.class);
	}

	public static void sendCmdQueryQueryAllProviderOrder(
			HttpDataLoader httpDataLoader, int page)
	{
		OrderService.QueryShopAllOrderRequest request =
				new OrderService.QueryShopAllOrderRequest();
		request.page = page;
		request.page_size = GlobalConst.INT_NUM_PAGE;
		httpDataLoader.doPostProcess(request, OrderPageResult.class);
	}

	public static void sendCmdQueryProviderOrderDetail(HttpDataLoader httpDataLoader, String orderId)
	{
		OrderService.ShopOrderDetailRequest request = new OrderService.ShopOrderDetailRequest();
		request.order_id = orderId;
		httpDataLoader.doPostProcess(request, OrderDetail.class);
	}

	public static void sendCmdProviderOrderCancel(
			HttpDataLoader httpDataLoader, String orderId)
	{
		OrderService.CancelOrderRequest request =
				new OrderService.CancelOrderRequest();
		request.id = orderId;
		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}

	public static void sendCmdProviderOrderShippment(
			HttpDataLoader httpDataLoader, String orderId, String shippingCode,
			String code, String[] coverIds)
	{
		OrderService.OrderShipmentRequest request =
				new OrderService.OrderShipmentRequest();
		request.OrderId = orderId;
		request.ShippingCode = shippingCode;
		request.Code = code;
		request.CoverIds = coverIds;
		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}

	public static void sendCmdProviderOrderRefund(
			HttpDataLoader httpDataLoader, String orderId, String productId)
	{
		OrderService.RefundOperationRequest request =
				new OrderService.RefundOperationRequest();
		request.OrderId = orderId;
		request.ProductId = productId;
		request.Status = "2";
		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}

	public static void sendCmdProviderOrderRefuseRefund(
			HttpDataLoader httpDataLoader, String orderId, String productId)
	{
		OrderService.RefuseRefundOperationRequest request =
				new OrderService.RefuseRefundOperationRequest();
		request.OrderId = orderId;
		request.ProductId = productId;
		request.Status = "4";
		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}
}
