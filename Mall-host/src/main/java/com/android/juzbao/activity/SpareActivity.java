
package com.android.juzbao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.juzbao.adapter.SelectGiftAdapter;
import com.android.juzbao.enumerate.ProductType;
import com.android.juzbao.fragment.CategroyFreeFragment_;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshBase;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshBase.OnFooterRefreshListener;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshBase.OnHeaderRefreshListener;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshScrollView;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.AdProduct;
import com.server.api.model.ProductItem;
import com.server.api.model.Products;
import com.server.api.service.ProductService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 闲置 需求
 * </p>
 *
 * @ClassName: SpareActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_free)
public class SpareActivity extends SwipeBackActivity implements
        OnHeaderRefreshListener<ScrollView>, OnFooterRefreshListener<ScrollView> {

    @ViewById(R.id.editvew_search_show)
    TextView mEditvewSearch;

    @ViewById(R.id.gvew_gift_show)
    GridView mGridView;

    /**
     * 需要分类
     */
    @ViewById(R.id.flayout_free_category)
    FrameLayout mFlayoutCategory;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshScrollView mPullToRefreshView;

    private SelectGiftAdapter mAdapter;

    private PageInditor<ProductItem> mPageInditor =
            new PageInditor<ProductItem>();

    /**
     * 需求分类Fragment
     */
    private CategroyFreeFragment_ mCategroyFreeFragment;

    private String mCategoryId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        mEditvewSearch.setHint("搜索需求宝贝");
        getTitleBar().setTitleText("需求");
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);

        mCategroyFreeFragment = new CategroyFreeFragment_();
        addFragment(R.id.flayout_free_category, mCategroyFreeFragment);

        getDataEmptyView().showViewWaiting();
        refreshData(true, mCategoryId);
        ProductBusiness.queryAdProducts(getHttpDataLoader(), "free_banner");
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ProductService.ProductsRequest.class)) {
            mPullToRefreshView.onRefreshComplete();
            mPageInditor.clear();
            Products responseProduct = (Products) msg.getRspObject();

            if (ProductBusiness.validateQueryProducts(responseProduct)) {
                mPageInditor.add(responseProduct.Data.Results);

                if (null == mAdapter) {
                    mAdapter =
                            new SelectGiftAdapter(this, mPageInditor.getAll());
                    mPageInditor.bindAdapter(mGridView, mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }

                if (mPageInditor.size() == responseProduct.Data.Total) {
                    mPullToRefreshView.setFooterRefreshComplete();
                } else {
                    mPullToRefreshView.setFooterRefresh();
                }
                CommonUtil.setGridViewHeightBasedOnChildren(mGridView, 2);
                getDataEmptyView().setVisibility(View.GONE);
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
        } else if (msg.valiateReq(ProductService.AdvertRequest.class)) {
            AdProduct response = (AdProduct) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                ProductBusiness.bindSliderLayout(this, R.id.flayout_slider_image, response.Data);
            } else {

            }
        }
    }

    @Click(R.id.rlayout_search_click)
    void onClickRlayoutSearch() {
        ProductBusiness.intentToSearchActivity(this,
                ProductType.XIANZHI.getValue());
    }

    /**
     * 控制需求分类的显示和隐藏
     */
    @Click(R.id.tvew_scan_click)
    void onClickTvewScan() {
        if (mFlayoutCategory.getVisibility() == View.VISIBLE) {
            mFlayoutCategory.setVisibility(View.GONE);
        } else {
            mFlayoutCategory.setVisibility(View.VISIBLE);
        }
    }

    //查询xianzhi
    public void refreshData(String categoryId) {
        mFlayoutCategory.setVisibility(View.GONE);
        mPageInditor.setPullRefresh(true);
        mPullToRefreshView.setRefreshing();
        ProductBusiness.queryProducts(getHttpDataLoader(), categoryId,
                ProductType.XIANZHI.getValue(), mPageInditor.getPageNum());
    }

    public void refreshData(boolean isPullRefresh, String categoryId) {
        mPageInditor.setPullRefresh(isPullRefresh);
        ProductBusiness.queryProducts(getHttpDataLoader(), categoryId,
                ProductType.XIANZHI.getValue(), mPageInditor.getPageNum());
    }

    @Override
    public void onFooterRefresh(PullToRefreshBase<ScrollView> refreshView) {
        refreshData(false, mCategoryId);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshBase<ScrollView> refreshView) {
        refreshData(true, mCategoryId);
    }
}