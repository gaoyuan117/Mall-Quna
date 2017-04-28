
package com.android.juzbao.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.juzbao.adapter.MessageAdapter;
import com.android.juzbao.model.MessageBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.annotation.ZTitleMore;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.MessageItem;
import com.server.api.model.MessagePageResult;
import com.server.api.service.MessageService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 消息列表
 * </p>
 *
 * @ClassName:MyMessageListActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_my_message_list)
@ZTitleMore(false)
public class MyMessageListActivity extends SwipeBackActivity implements
        OnHeaderRefreshListener, OnFooterRefreshListener {

    private PageInditor<MessageItem> mPageInditor =
            new PageInditor<MessageItem>();

    private String mstrType;

    private MessageAdapter mAdapter;

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


        Intent intent = getIntent();
        //标题
        String title = intent.getStringExtra("title");
        //消息类型
        mstrType = intent.getStringExtra("type");
        getTitleBar().setTitleText(title);

        mListView.setDividerHeight(0);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);

        getDataEmptyView().showViewWaiting();
        refreshData(true);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(MessageService.MessageListRequest.class)) {
            mPullToRefreshView.onFooterRefreshComplete();
            mPullToRefreshView.onHeaderRefreshComplete();

            mPageInditor.clear();

            MessagePageResult response = (MessagePageResult) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                mPageInditor.add(response.Data.Result);

                if (null == mAdapter) {
                    mAdapter = new MessageAdapter(this, mPageInditor.getAll());
                    mPageInditor.bindAdapter(mListView, mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }

                if (mPageInditor.getAll().size() == response.Data.Total) {
                    mPullToRefreshView.setFooterRefreshComplete();
                } else {
                    mPullToRefreshView.setFooterVisible();
                }
                getDataEmptyView().removeAllViews();
            } else {
                if (!ValidateUtil.isListEmpty(mPageInditor.getAll())) {
                    getDataEmptyView().setVisibility(View.GONE);
                } else {
                    getDataEmptyView().showViewDataEmpty(false, false, msg,
                            R.string.common_data_empty);
                }
            }
        }
    }

    private void refreshData(boolean isPullRefresh) {
        mPageInditor.setPullRefresh(isPullRefresh);
        MessageBusiness.queryMessageList(getHttpDataLoader(), mstrType, mPageInditor.getPageNum());
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        refreshData(false);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refreshData(true);
    }
}