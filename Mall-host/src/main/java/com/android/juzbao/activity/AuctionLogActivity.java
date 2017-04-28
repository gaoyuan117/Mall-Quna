
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import com.android.mall.resource.R;
import com.server.api.model.AuctionLogItem;
import com.server.api.model.AuctionLogPageResult;
import com.android.juzbao.adapter.AuctionLogAdapter;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.ProductService;

/**
 * <p>
 * Description: 拍真宝 - 拍卖精选
 * </p>
 *
 * @ClassName:AuctionLogActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_auction_log)
public class AuctionLogActivity extends SwipeBackActivity implements
        OnHeaderRefreshListener, OnFooterRefreshListener {

    @ViewById(R.id.editvew_search_show)
    EditText mEditvewSearch;

    @ViewById(R.id.common_listview_show)
    ListView mListView;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshView mPullToRefreshView;

    private String mProductId;

    private AuctionLogAdapter mAdapter;

    private PageInditor<AuctionLogItem> mPageInditor =
            new PageInditor<AuctionLogItem>();

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("出价记录");
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);

        mProductId = getIntent().getStringExtra("id");

        getDataEmptyView().showViewWaiting();
        ProductBusiness.queryPaipinSignupLog(getHttpDataLoader(),
                Long.parseLong(mProductId), mPageInditor.getPageNum());
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ProductService.ProductSignupLogRequest.class)) {
            mPullToRefreshView.onFooterRefreshComplete();
            mPullToRefreshView.onHeaderRefreshComplete();
            mPageInditor.clear();

            AuctionLogPageResult responseProduct = (AuctionLogPageResult) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, responseProduct)) {
                mPageInditor.add(responseProduct.Data.Result);
                if (null == mAdapter) {
                    mAdapter =
                            new AuctionLogAdapter(this,
                                    mPageInditor.getAll());
                    mPageInditor.bindAdapter(mListView, mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }

                if (mPageInditor.getAll().size() == responseProduct.Data.Total) {
                    mPullToRefreshView.setFooterRefreshComplete();
                } else {
                    mPullToRefreshView.setFooterVisible();
                }
                getDataEmptyView().dismiss();
            } else {
                if (!ValidateUtil.isListEmpty(mPageInditor.getAll())) {
                    getDataEmptyView().setVisibility(View.GONE);
                } else {
                    getDataEmptyView().showViewDataEmpty(false, false, msg,
                            "暂无出价记录");
                }
            }
        }
    }


    private void refreshData(boolean isPullRefresh) {
        mPageInditor.setPullRefresh(isPullRefresh);

        ProductBusiness.queryPaipinSignupLog(getHttpDataLoader(),
                Long.parseLong(mProductId), mPageInditor.getPageNum());
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