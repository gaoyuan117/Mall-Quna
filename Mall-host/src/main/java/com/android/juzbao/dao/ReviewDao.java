package com.android.juzbao.dao;

import com.server.api.service.ReviewService;
import com.server.api.service.ReviewService.AddReviewRequest;
import com.android.juzbao.constant.GlobalConst;
import com.android.zcomponent.http.HttpDataLoader;


public class ReviewDao
{
	public static void sendCmdQueryReviews(HttpDataLoader httpDataLoader,
			String productId, int type, int page)
	{
		ReviewService.GetProductReviewRequest request =
				new ReviewService.GetProductReviewRequest();
		request.Page = page;
		request.Pagesize = GlobalConst.INT_NUM_PAGE;
		request.ProductId = productId;
		request.Rate = type;
		httpDataLoader.doPostProcess(request, com.server.api.model.ReviewPageResult.class);
	}
	
	public static void sendCmdQueryAddReview(HttpDataLoader httpDataLoader, AddReviewRequest request)
	{
		httpDataLoader.doPostProcess(request, com.server.api.model.CommonReturn.class);
	}

}
