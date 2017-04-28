
package com.android.juzbao.dao;

import com.server.api.model.MessageDetail;
import com.server.api.model.MessagePageResult;
import com.server.api.service.MessageService;
import com.android.juzbao.constant.GlobalConst;
import com.android.zcomponent.http.HttpDataLoader;

public class MessageDao
{

	public static void sendCmdQueryMessage(HttpDataLoader httpDataLoader,
			String type, int page)
	{
		MessageService.MessageListRequest request =
				new MessageService.MessageListRequest();
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		request.Type = type;
		httpDataLoader.doPostProcess(request,
				MessagePageResult.class);
	}
	
	public static void sendCmdQueryMessageDetail(HttpDataLoader httpDataLoader,
			String id)
	{
		MessageService.MessageDetailRequest request =
				new MessageService.MessageDetailRequest();
		request.Id = id;
		httpDataLoader.doPostProcess(request, MessageDetail.class);
	}

}
