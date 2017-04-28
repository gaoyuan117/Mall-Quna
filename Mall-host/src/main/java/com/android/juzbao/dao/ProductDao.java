
package com.android.juzbao.dao;

import com.android.juzbao.constant.GlobalConst;
import com.android.juzbao.enumerate.ProductType;
import com.android.zcomponent.http.HttpDataLoader;
import com.server.api.model.AdProduct;
import com.server.api.service.ProductService;

import java.math.BigDecimal;

public class ProductDao
{

	public static void sendCmdQueryShopInfo(HttpDataLoader httpDataLoader,
			String shopId)
	{
		ProductService.ShopInfoRequest request =
				new ProductService.ShopInfoRequest();
		request.shop_id = shopId;
		httpDataLoader.doPostProcess(request, com.server.api.model.ShopInfo.class);
	}
	
	public static void sendCmdQueryAdProducts(HttpDataLoader httpDataLoader,
			String identifier)
	{
		ProductService.AdvertRequest request =
				new ProductService.AdvertRequest();
		request.Identifier = identifier;
		httpDataLoader.doPostProcess(request, com.server.api.model.AdProduct.class);
	}

	public static void sendCmdQueryHomeProducts(HttpDataLoader httpDataLoader)
	{
		ProductService.HomeRequest request = new ProductService.HomeRequest();
		httpDataLoader.doPostProcess(request, com.server.api.model.HomeProduct.class);
	}
	
	public static void sendCmdQueryProductOptions(HttpDataLoader httpDataLoader, String productId)
	{
		ProductService.ProductOptionRequest request = new ProductService.ProductOptionRequest();
		request.ProductId = productId;
		httpDataLoader.doPostProcess(request, com.server.api.model.ProductOptions.class);
	}

	public static void sendCmdQueryProducts(HttpDataLoader httpDataLoader,
			String categoryId, String type, String sort, int page)
	{
		ProductService.ProductsRequest request =
				new ProductService.ProductsRequest();
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		request.CategoryId = categoryId;
		request.Sortview = sort;
		request.Type = type;
		httpDataLoader.doPostProcess(request, com.server.api.model.Products.class);
	}
	
	public static void sendCmdQueryShopProducts(HttpDataLoader httpDataLoader,
			int shopId, int page)
	{
		ProductService.ShopProductsRequest request =
				new ProductService.ShopProductsRequest();
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		request.ShopId = shopId;
		httpDataLoader.doPostProcess(request, com.server.api.model.Products.class);
	}
	
	public static void sendCmdQueryShopProductSearch(HttpDataLoader httpDataLoader,
			int shopId, int page, String keyword)
	{
		ProductService.ShopProductSearchRequest request =
				new ProductService.ShopProductSearchRequest();
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		request.ShopId = shopId;
		request.Keyword = keyword;
		httpDataLoader.doPostProcess(request, com.server.api.model.Products.class);
	}
	
	public static void sendCmdQuerySearchProducts(HttpDataLoader httpDataLoader,
			String type, String keyword, int page)
	{
		ProductService.SearchRequest request =
				new ProductService.SearchRequest();
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		request.Keyword = keyword;
		request.Type = type;
		httpDataLoader.doPostProcess(request,
				com.server.api.model.Products.class);
	}

	public static void sendCmdQueryGiftCategory(HttpDataLoader httpDataLoader)
	{
		ProductService.GiftCagetoryRequest request =
				new ProductService.GiftCagetoryRequest();
		httpDataLoader.doPostProcess(request,
				com.server.api.model.GiftCategory.class);
	}

	public static void sendCmdQueryPaipinSignup(HttpDataLoader httpDataLoader,
			long productId)
	{
		ProductService.ProductSignupRequest request =
				new ProductService.ProductSignupRequest();
		request.ProductId = productId;
		httpDataLoader.doPostProcess(request, com.server.api.model.CommonReturn.class);
	}

	public static void sendCmdQueryPaipinSignupMoney(
			HttpDataLoader httpDataLoader, long productId, BigDecimal money)
	{
		ProductService.ProductSignupMoneyRequest request =
				new ProductService.ProductSignupMoneyRequest();
		request.Money = money;
		request.ProductId = productId;
		httpDataLoader.doPostProcess(request, com.server.api.model.CommonReturn.class);
	}

	public static void sendCmdQueryPaipinBondNotice(
			HttpDataLoader httpDataLoader)
	{
		ProductService.BondNoticeRequest request =
				new ProductService.BondNoticeRequest();
		httpDataLoader.doPostProcess(request, com.server.api.model.BondNotice.class);
	}
	
	public static void sendCmdQueryPaipinSignupLog(
			HttpDataLoader httpDataLoader, long productId, int page)
	{
		ProductService.ProductSignupLogRequest request =
				new ProductService.ProductSignupLogRequest();
		request.ProductId = productId;
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		httpDataLoader.doPostProcess(request, com.server.api.model.AuctionLogPageResult.class);
	}

	public static void sendCmdQueryGiftProducts(HttpDataLoader httpDataLoader,
			int categoryId, int giftId, int page)
	{
		ProductService.GiftProductsRequest request =
				new ProductService.GiftProductsRequest();
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		request.CategoryId = categoryId;
		request.Type = ProductType.PUTONG.getValue();
		request.GiftId = giftId;
		request.InSpecialGift = 1;
		httpDataLoader.doPostProcess(request, com.server.api.model.Products.class);
	}

	public static void sendCmdQueryPanicProducts(HttpDataLoader httpDataLoader,
			int categoryId, int sortDiscount, long panicId, long endTime,
			int page)
	{
		ProductService.PanicProductsRequest request =
				new ProductService.PanicProductsRequest();
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		request.CategoryId = categoryId;
		request.Type = ProductType.PUTONG.getValue();
		request.PanicId = panicId;
		request.EndTime = endTime;
		request.InSpecialPanic = 1;
		request.SortDiscount = sortDiscount;
		httpDataLoader.doPostProcess(request,
				com.server.api.model.Products.class);
	}

	public static void sendCmdQueryAuctionProducts(
			HttpDataLoader httpDataLoader, int categoryId, int isDelicacy,
			int startTime, int endTime, int priceStart, int page)
	{
		ProductService.PaipinProductsRequest request =
				new ProductService.PaipinProductsRequest();
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		request.CategoryId = categoryId;
		request.IsDelicacy = isDelicacy;
		request.Starttime = startTime;
		request.Endtime = endTime;
		request.PriceStart = priceStart;
		httpDataLoader.doPostProcess(request,
				com.server.api.model.Products.class);
	}

	public static void sendCmdQueryProduct(HttpDataLoader httpDataLoader, int id)
	{
		ProductService.ProductRequest request =
				new ProductService.ProductRequest();
		request.Id = id;
		httpDataLoader.doPostProcess(request, com.server.api.model.Product.class);
	}

	public static void sendCmdQueryPaincTime(HttpDataLoader httpDataLoader,
			String type)
	{
		ProductService.PanicTimesRequest request =
				new ProductService.PanicTimesRequest();
		request.Type = type;
		httpDataLoader.doPostProcess(request,
				com.server.api.model.PaincTimes.class);
	}

	public static void sendCmdQueryAddPhotoByProduct(
			HttpDataLoader httpDataLoader,
			ProductService.ProductAddPhotoBuyRequest request)
	{
		httpDataLoader.doPostProcess(request, com.server.api.model.AddProduct.class);
	}

	public static void sendCmdQueryAddFreeProduct(
			HttpDataLoader httpDataLoader,
			ProductService.ProductAddFreeRequest request)
	{
		httpDataLoader.doPostProcess(request, com.server.api.model.AddProduct.class);
	}

	public static void sendCmdQueryEditFreeProduct(
			HttpDataLoader httpDataLoader,
			ProductService.ProductEditFreeRequest request)
	{
		httpDataLoader.doPostProcess(request, com.server.api.model.CommonReturn.class);
	}

	public static void sendCmdQueryIndexRecomAdProducts(HttpDataLoader httpDataLoader, String index_recom) {
		ProductService.AdIndexRecomRequest request = new ProductService.AdIndexRecomRequest();
		request.Identifier = "index_recom";
		httpDataLoader.doPostProcess(request, AdProduct.class);
	}
}
