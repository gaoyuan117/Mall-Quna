package com.android.juzbao.activity.jifen;

import android.view.View;
import android.widget.ListView;

import com.android.juzbao.adapter.JiFenAdapter;
import com.android.juzbao.dao.jifendao.JiFenDao;
import com.android.juzbao.enumerate.ProductType;
import com.android.juzbao.model.ProductBusiness;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.ProductItem;
import com.server.api.model.Products;
import com.server.api.service.JiFenService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_jifen)
public class JiFenProductActivity extends SwipeBackActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshView mPullToRefreshView;

    @ViewById(R.id.lsew_jifen_show)
    ListView mListView;

    public String mType = ProductType.SCORE.getValue();

    private PageInditor<ProductItem> mPageInditor = new PageInditor<ProductItem>();

    private JiFenAdapter mAdapter;

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("积分兑换");

        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);

        getDataEmptyView().setBackgroundResource(R.drawable.transparent);
        getDataEmptyView().showViewWaiting();

        resfreshData(true);
    }

    public void resfreshData(boolean isPullRefresh) {
        mPageInditor.setPullRefresh(isPullRefresh);
        JiFenDao.sendQueryScoreProducts(getHttpDataLoader(),mType,mPageInditor.getPageNum());
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(JiFenService.ProductsRequest.class)) {
            mPullToRefreshView.onFooterRefreshComplete();
            mPullToRefreshView.onHeaderRefreshComplete();
            mPageInditor.clear();
            Products responseProduct = (Products) msg.getRspObject();
            if (ProductBusiness.validateQueryProducts(responseProduct)) {
                mPageInditor.add(responseProduct.Data.Results);
                if (null == mAdapter) {
                    mAdapter = new JiFenAdapter(this, mPageInditor.getAll());
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
                    String message = ProductBusiness.validateQueryState(responseProduct, getString(com.android.mall.resource.R.string.common_data_empty));
                    getDataEmptyView().showViewDataEmpty(false, false, msg, message);
                }
            }
        }
    }

    @Override public void onHeaderRefresh(PullToRefreshView view) {
        resfreshData(true);
    }

    @Override public void onFooterRefresh(PullToRefreshView view) {
        resfreshData(false);
    }
}
