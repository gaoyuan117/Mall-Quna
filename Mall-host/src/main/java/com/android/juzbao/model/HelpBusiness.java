
package com.android.juzbao.model;

import com.android.juzbao.dao.HelpDao;
import com.android.zcomponent.http.HttpDataLoader;

public class HelpBusiness
{

	public static void queryFeedback(HttpDataLoader httpDataLoader,
			String content)
	{
		HelpDao.sendCmdAddFeedback(httpDataLoader, content);
	}

	public static void queryArticles(HttpDataLoader httpDataLoader)
	{
		HelpDao.sendCmdQueryArticles(httpDataLoader);
	}

	public static void queryArticlesDetail(HttpDataLoader httpDataLoader,
			String id)
	{
		HelpDao.sendCmdQueryArticlesDetail(httpDataLoader, id);
	}

}
