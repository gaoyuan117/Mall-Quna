
package com.android.juzbao.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import com.server.api.model.AdProduct;
import com.server.api.model.ProductItem;
import com.server.api.model.Products;
import com.android.juzbao.activity.CategoryActivity_;
import com.android.mall.resource.R;
import com.android.juzbao.adapter.AuctionSelectedAdapter;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.enumerate.ProductType;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.imageslider.SliderLayout;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshBase;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshBase.OnFooterRefreshListener;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshBase.OnHeaderRefreshListener;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshScrollView;
import com.server.api.service.ProductService;

/**
 * <p>
 * Description: 拍真宝 - 拍卖精选
 * </p>
 *
 * @ClassName:AuctionSelectedFragment
 * @author: wei
 * @date: 2015-11-10
 */
@EFragment(R.layout.fragment_auction_selected)
public class AuctionSelectedFragment extends BaseFragment implements
        OnHeaderRefreshListener<ScrollView>, OnFooterRefreshListener<ScrollView> {

    @ViewById(R.id.common_listview_show)
    ListView mListView;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshScrollView mPullToRefreshView;

    private SliderLayout mSliderLayout;

    private AuctionSelectedAdapter mAdapter;

    private PageInditor<ProductItem> mPageInditor =
            new PageInditor<ProductItem>();

    @AfterViews
    void initUI() {
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDataEmptyView().showViewWaiting();
        ProductBusiness.queryAuctionProducts(getHttpDataLoader(), 0, 1, 0, 0,
                0, mPageInditor.getPageNum());
        ProductBusiness.queryAdProducts(getHttpDataLoader(), "ad_paipintop");
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ProductService.PaipinProductsRequest.class)) {
            mPullToRefreshView.onRefreshComplete();
            mPageInditor.clear();

            Products responseProduct = (Products) msg.getRspObject();
            if (ProductBusiness.validateQueryProducts(responseProduct)) {
                mPageInditor.add(responseProduct.Data.Results);
                if (null == mAdapter) {
                    mAdapter =
                            new AuctionSelectedAdapter(getActivity(),
                                    mPageInditor.getAll());
                    mPageInditor.bindAdapter(mListView, mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }

                if (mPageInditor.getAll().size() == responseProduct.Data.Total) {
                    mPullToRefreshView.setFooterRefreshComplete();
                } else {
                    mPullToRefreshView.setFooterRefresh();
                }
                CommonUtil.setListViewHeightBasedOnChildren(mListView);
                getDataEmptyView().dismiss();
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
        }else if (msg.valiateReq(ProductService.AdvertRequest.class)){
            AdProduct response = msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                mSliderLayout =
                        ProductBusiness.bindSliderLayout(getActivity(),
                                R.id.flayout_slider_image,
                                response.Data);
            } else {

            }
        }
    }

    @Click(R.id.rlayout_search_click)
    void onClickRlayoutSearch() {
        ProductBusiness.intentToSearchActivity(getActivity(),
                ProductType.PAIPIN.getValue());
    }

    @Click(R.id.tvew_scan_click)
    void onClickTvewScan() {
        Bundle bundle = new Bundle();
        bundle.putString("type", ProductType.PAIPIN.getValue());
        getIntentHandle().intentToActivity(CategoryActivity_.class);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null != mSliderLayout) {
            mSliderLayout.startAutoCycle();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (null != mSliderLayout) {
            mSliderLayout.stopAutoCycle();
        }
    }

    private void refreshData(boolean isPullRefresh) {
        mPageInditor.setPullRefresh(isPullRefresh);

        ProductBusiness.queryAuctionProducts(getHttpDataLoader(), 0, 1, 0, 0,
                0, mPageInditor.getPageNum());
    }

    @Override
    public void onFooterRefresh(PullToRefreshBase<ScrollView> refreshView) {
        refreshData(false);

    }

    @Override
    public void onHeaderRefresh(PullToRefreshBase<ScrollView> refreshView) {
        refreshData(true);

    }
}