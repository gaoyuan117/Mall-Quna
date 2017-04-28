
package com.android.juzbao.activity;

import java.util.ArrayList;
import java.util.Arrays;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import com.android.mall.resource.R;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.server.api.model.ArticleItem;
import com.server.api.model.ArticlesPageResult;
import com.android.juzbao.adapter.ArticlesAdapter;
import com.android.juzbao.model.HelpBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.HelpService;

/**
 * <p>
 * Description: 帮助与反馈
 * </p>
 *
 * @ClassName:HelpActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_help)
public class HelpActivity extends SwipeBackActivity {

    @ViewById(R.id.common_listview_show)
    ListView mListView;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshView mPullToRefreshView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        mPullToRefreshView.setHeaderInvisible();
        mPullToRefreshView.setFooterInvisible();
        mListView.setDividerHeight(0);
        getTitleBar().setTitleText("帮助与反馈");
        getTitleBar().showRightTextView("意见反馈");
        getDataEmptyView().showViewWaiting();
        HelpBusiness.queryArticles(getHttpDataLoader());
    }

    @Override
    public void onTitleBarRightFirstViewClick(View view) {
        getIntentHandle().intentToActivity(OptionActivity_.class);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(HelpService.ArticlesRequest.class)) {
            ArticlesPageResult response =
                    (ArticlesPageResult) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                mListView
                        .setAdapter(new ArticlesAdapter(this,
                                new ArrayList<ArticleItem>(Arrays
                                        .asList(response.Data))));
                getDataEmptyView().dismiss();
            } else {
                getDataEmptyView().showViewDataEmpty(true, false, msg, "");
            }
        }
    }

}