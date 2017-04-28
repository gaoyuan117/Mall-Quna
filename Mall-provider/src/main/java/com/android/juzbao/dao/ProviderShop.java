
package com.android.juzbao.dao;

import com.server.api.model.CommonReturn;
import com.server.api.model.CourseDetail;
import com.server.api.model.CoursePageResult;
import com.server.api.model.OnlineService;
import com.server.api.model.ShopInfo;
import com.server.api.service.ShopService;
import com.android.juzbao.constant.ProviderGlobalConst;
import com.android.zcomponent.http.HttpDataLoader;

public class ProviderShop
{

	public static void sendCmdQueryShopProtocol(HttpDataLoader httpDataLoader)
	{
		ShopService.ShopProtocolRequest request =
				new ShopService.ShopProtocolRequest();

		httpDataLoader.doPostProcess(request, String.class, false);
	}

	public static void sendCmdQueryShopInfo(HttpDataLoader httpDataLoader)
	{
		ShopService.ShopInfoRequest request = new ShopService.ShopInfoRequest();
		httpDataLoader.doPostProcess(request,
				ShopInfo.class);
	}

	public static void sendCmdQueryOnlineService(HttpDataLoader httpDataLoader)
	{
		ShopService.OnlineServiceRequest request = new ShopService.OnlineServiceRequest();
		httpDataLoader.doPostProcess(request,
				OnlineService.class);
	}


	public static void sendCmdQueryCourse(HttpDataLoader httpDataLoader,
			String type, int page)
	{
		ShopService.CourseListRequest request =
				new ShopService.CourseListRequest();
		request.Type = type;
		request.Page = page;
		request.Pagesize = ProviderGlobalConst.INT_PAGE_NUMBER;
		httpDataLoader.doPostProcess(request, CoursePageResult.class);
	}

	public static void sendCmdQueryCourseDetail(HttpDataLoader httpDataLoader,
			String id)
	{
		ShopService.CourseDetailRequest request =
				new ShopService.CourseDetailRequest();
		request.Id = id;
		httpDataLoader.doPostProcess(request, CourseDetail.class);
	}

	public static void sendCmdQueryShopCreate(HttpDataLoader httpDataLoader,
			String title, String desc, String headId, String signId)
	{
		ShopService.CreateShopRequest request =
				new ShopService.CreateShopRequest();
		request.Title = title;
		request.Description = desc;
		request.Headpic = headId;
		request.Signpic = signId;
		httpDataLoader.doPostProcess(request,
				CommonReturn.class);
	}

	public static void sendCmdQueryShopEdit(HttpDataLoader httpDataLoader,
			String shopId, String title, String desc, String headId,
			String signId)
	{
		ShopService.EditShopRequest request = new ShopService.EditShopRequest();
		request.ShopId = shopId;
		request.Title = title;
		request.Description = desc;
		request.Headpic = headId;
		request.Signpic = signId;
		httpDataLoader.doPostProcess(request,
				CommonReturn.class);
	}
}
