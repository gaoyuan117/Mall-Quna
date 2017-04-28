package com.android.juzbao.model;

import com.android.juzbao.dao.MessageDao;
import com.android.zcomponent.http.HttpDataLoader;
import com.server.api.model.NewsDetail;
import com.server.api.model.NewsPageResult;
import com.server.api.service.MessageService;


public class MessageBusiness
{
	public static void queryMessageList(HttpDataLoader httpDataLoader,
			String type, int page)
	{
		MessageDao.sendCmdQueryMessage(httpDataLoader, type, page);
	}
	
	public static void queryMessageDetail(HttpDataLoader httpDataLoader,
			String id)
	{
		MessageDao.sendCmdQueryMessageDetail(httpDataLoader, id);
	}

	public static void queryNewsList(HttpDataLoader httpDataLoader, String type)
	{
		MessageService.NewsListRequest request = new MessageService.NewsListRequest();
		request.isHot = "1";
		request.Page = 1;
		request.Pagesize = Integer.MAX_VALUE;
		request.Type = type;
		httpDataLoader.doPostProcess(request, NewsPageResult.class);
	}

	public static void queryNewsDetail(HttpDataLoader httpDataLoader,
										  String id)
	{
		MessageService.NewsDetailRequest request = new MessageService.NewsDetailRequest();
		request.Id = id;
		httpDataLoader.doPostProcess(request, NewsDetail.class);
	}
}
