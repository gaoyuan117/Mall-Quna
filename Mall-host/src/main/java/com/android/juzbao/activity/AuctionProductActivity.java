
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.android.mall.resource.R;
import com.server.api.model.ProductItem;
import com.server.api.model.Products;
import com.android.juzbao.adapter.AuctionBeginAdapter;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.enumerate.ProductType;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.ProductService;

@EActivity(R.layout.activity_auction_product)
public class AuctionProductActivity extends SwipeBackActivity implements
        OnHeaderRefreshListener, OnFooterRefreshListener {

    @ViewById(R.id.common_listview_show)
    ListView mListView;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshView mPullToRefreshView;

    private PageInditor<ProductItem> mPageInditor =
            new PageInditor<ProductItem>();

    private String mstrCategoryId;

    private String mstrType;

    private AuctionBeginAdapter mAdapter;

    @AfterViews
    void initUI() {
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);

        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        mstrCategoryId = intent.getStringExtra("category_id");
        mstrType = intent.getStringExtra("type");

        getTitleBar().setTitleText(title);
        getDataEmptyView().setBackgroundResource(R.drawable.transparent);
        getDataEmptyView().showViewWaiting();
        refreshData(true);
    }

    @Override
    public void onDataEmptyClickRefresh() {
        getDataEmptyView().showViewWaiting();
        refreshData(true);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ProductService.ProductsRequest.class)) {
            mPullToRefreshView.onFooterRefreshComplete();
            mPullToRefreshView.onHeaderRefreshComplete();

            mPageInditor.clear();

            Products responseProduct = (Products) msg.getRspObject();
            if (ProductBusiness.validateQueryProducts(responseProduct)) {
                mPageInditor.add(responseProduct.Data.Results);

                if (null == mAdapter) {
                    mAdapter = new AuctionBeginAdapter(this, mPageInditor.getAll());
                    mPageInditor.bindAdapter(mListView, mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }

                if (mPageInditor.getAll().size() == responseProduct.Data.Total) {
                    mPullToRefreshView.setFooterRefreshComplete();
                } else {
                    mPullToRefreshView.setFooterVisible();
                }
                getDataEmptyView().removeAllViews();
            } else {
                if (!ValidateUtil.isListEmpty(mPageInditor.getAll())) {
                    getDataEmptyView().setVisibility(View.GONE);
                } else {
                    String message =
                            ProductBusiness.validateQueryState(responseProduct,
                                    getString(R.string.common_data_empty));
                    getDataEmptyView().showViewDataEmpty(false, false, msg,
                            message);
                }
            }
        }
    }

    private void refreshData(boolean isPullRefresh) {
        mPageInditor.setPullRefresh(isPullRefresh);

        if (TextUtils.isEmpty(mstrType)) {
            mstrType = ProductType.PUTONG.getValue();
        }
        ProductBusiness.queryProducts(getHttpDataLoader(),
                mstrCategoryId, mstrType,
                mPageInditor.getPageNum());
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
