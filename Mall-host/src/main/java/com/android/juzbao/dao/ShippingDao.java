package com.android.juzbao.dao;

import com.server.api.service.ShippingService;
import com.android.zcomponent.http.HttpDataLoader;


public class ShippingDao
{
	public static void sendCmdQueryShippingList(HttpDataLoader httpDataLoader)
	{
		ShippingService.ShippingListRequest request =
				new ShippingService.ShippingListRequest();
		httpDataLoader.doPostProcess(request, com.server.api.model.Shipping.class);
	}
	
	public static void sendCmdQueryOrderShippingDetail(HttpDataLoader httpDataLoader, String orderId)
	{
		ShippingService.OrderShippingDetailRequest request =
				new ShippingService.OrderShippingDetailRequest();
		request.OrderId = orderId;
		httpDataLoader.doPostProcess(request, com.server.api.model.ShippingAdminDetail.class);
	}
	
	public static void sendCmdQueryDistinguishShippingDetail(HttpDataLoader httpDataLoader, String distinguishId)
	{
		ShippingService.DistinguishShippingDetailRequest request =
				new ShippingService.DistinguishShippingDetailRequest();
		request.DistinguishId = distinguishId;
		httpDataLoader.doPostProcess(request, com.server.api.model.ShippingSellerDetail.class);
	}
}
