
package com.android.juzbao.model;

import android.content.Context;
import android.text.TextUtils;

import com.server.api.service.ReviewService.AddReviewRequest;
import com.android.juzbao.dao.ReviewDao;
import com.android.zcomponent.http.HttpDataLoader;
import com.android.zcomponent.util.ShowMsg;

public class ReviewBusiness
{

	public static void queryReviews(HttpDataLoader httpDataLoader,
			String productId, int type, int page)
	{
		ReviewDao.sendCmdQueryReviews(httpDataLoader, productId, type, page);
	}
	
	public static boolean queryAddReview(Context context, HttpDataLoader httpDataLoader,
			AddReviewRequest request)
	{
		if (request.Rate == 0)
		{
			ShowMsg.showToast(context, "请选择评价");
			return false;
		}
		
		if (TextUtils.isEmpty(request.Content))
		{
			ShowMsg.showToast(context, "请输入评价内容");
			return false;
		}
		ReviewDao.sendCmdQueryAddReview(httpDataLoader, request);
		return true;
	}

}
