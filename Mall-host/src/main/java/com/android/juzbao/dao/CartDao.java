
package com.android.juzbao.dao;

import com.android.zcomponent.http.HttpDataLoader;
import com.server.api.model.CommonReturn;
import com.server.api.service.CartService;

import java.math.BigDecimal;

public class CartDao
{

	public static void sendCmdQueryAddToCart(HttpDataLoader httpDataLoader,
			BigDecimal price, int productId, int quantity, String options)
	{
		CartService.AddToCartRequest request = new CartService.AddToCartRequest();
		request.Price = price;
		request.ProductId = productId;
		request.Quantity = quantity;
		request.option_ids = options;
		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}

	public static void sendCmdDelCartById(HttpDataLoader httpDataLoader,
			int cartId)
	{
		CartService.DelCartByIdRequest request =
				new CartService.DelCartByIdRequest();
		request.CartId = cartId;
		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}

	public static void sendCmdQueryEditCart(HttpDataLoader httpDataLoader,
			BigDecimal price, int CartId, int quantity, String options)
	{
		CartService.EditCartRequest request = new CartService.EditCartRequest();
		request.Price = price;
		request.CartId = CartId;
		request.Quantity = quantity;
		request.option_ids = options;
		httpDataLoader.doPostProcess(request, CommonReturn.class);
	}
	
	public static void sendCmdQueryCarts(HttpDataLoader httpDataLoader, int page)
	{
		CartService.QueryCartsRequest request =
				new CartService.QueryCartsRequest();
		request.Page = page;
		request.PageSize = Integer.MAX_VALUE;
		httpDataLoader.doPostProcess(request, com.server.api.model.CartPageResult.class);
	}
	
	public static void sendCmdToTrade(HttpDataLoader httpDataLoader, Integer[] cartIds)
	{
		CartService.ToTradeRequest request = new CartService.ToTradeRequest();
		request.CartIds = cartIds;
		httpDataLoader.doPostProcess(request, com.server.api.model.CartTradeResult.class);
	}
}
