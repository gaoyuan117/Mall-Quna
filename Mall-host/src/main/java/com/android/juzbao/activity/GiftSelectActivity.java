
package com.android.juzbao.activity;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import com.android.mall.resource.R;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.server.api.model.AdProduct;
import com.server.api.model.GiftCategory;
import com.server.api.model.GiftCategory.GiftCategoryItem;
import com.server.api.model.ProductItem;
import com.server.api.model.Products;
import com.android.juzbao.adapter.GiftCategoryAdapter;
import com.android.juzbao.adapter.GiftCategoryAdapter.CallBackInteface;
import com.android.juzbao.adapter.SelectGiftAdapter;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.enumerate.ProductType;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.ListUtil;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshBase;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshBase.OnFooterRefreshListener;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshBase.OnHeaderRefreshListener;
import com.android.zcomponent.views.pulltorefresh.PullToRefreshScrollView;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.ProductService;

/**
 * <p>
 * Description: 选礼物
 * </p>
 *
 * @ClassName:GiftSelectActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_gift_select)
public class GiftSelectActivity extends SwipeBackActivity implements
        OnHeaderRefreshListener<ScrollView>,
        OnFooterRefreshListener<ScrollView>,
        CallBackInteface {

    @ViewById(R.id.editvew_search_show)
    TextView mEditvewSearch;

    @ViewById(R.id.gvew_gift_show)
    GridView mListView;

    @ViewById(R.id.gvew_gift_category_show)
    GridView mGvewCategory;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshScrollView mPullToRefreshView;

    private SelectGiftAdapter mAdapter;

    private GiftCategoryAdapter mCategoryAdapter;

    private PageInditor<ProductItem> mPageInditor =
            new PageInditor<ProductItem>();

    private List<GiftCategoryItem> mlistCategory;

    private int categoryId;

    private int giftId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        mEditvewSearch.setHint("搜索礼物宝贝");
        getTitleBar().setTitleText("选礼物 ");
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);

        ProductBusiness.queryGiftCategory(getHttpDataLoader());
        ProductBusiness.queryGiftProducts(getHttpDataLoader(), categoryId,
                giftId, mPageInditor.getPageNum());
        ProductBusiness.queryAdProducts(getHttpDataLoader(), "ad_gifttop");
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ProductService.GiftCagetoryRequest.class)) {
            GiftCategory response = (GiftCategory) msg.getRspObject();
            if (null != response) {
                if (!ValidateUtil.isArrayEmpty(response.Data)) {
                    mlistCategory = ListUtil.arrayToList(response.Data);
                    mCategoryAdapter =
                            new GiftCategoryAdapter(this, mlistCategory);
                    mCategoryAdapter.setCallBackInteface(this);
                    mGvewCategory.setAdapter(mCategoryAdapter);
                    CommonUtil.setGridViewHeightBasedOnChildren(mGvewCategory,
                            5);
                }
            }
        }else if (msg.valiateReq(ProductService.GiftProductsRequest.class)){
            mPullToRefreshView.onRefreshComplete();
            mPageInditor.clear();
            Products responseProduct = (Products) msg.getRspObject();
            if (ProductBusiness.validateQueryProducts(responseProduct)) {
                mPageInditor.add(responseProduct.Data.Results);
                if (null == mAdapter) {
                    mAdapter =
                            new SelectGiftAdapter(this, mPageInditor.getAll());
                    mPageInditor.bindAdapter(mListView, mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }

                if (mPageInditor.size() == responseProduct.Data.Total) {
                    mPullToRefreshView.setFooterRefreshComplete();
                } else {
                    mPullToRefreshView.setFooterRefresh();
                }

                CommonUtil.setGridViewHeightBasedOnChildren(mListView, 2);
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
        }else if (msg.valiateReq(ProductService.AdvertRequest.class)){
            AdProduct response = (AdProduct) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                ProductBusiness.bindSliderLayout(this,
                        R.id.flayout_slider_image, response.Data);
            } else {

            }
        }
    }

    @Click(R.id.imgvew_clear_icon_click)
    void onClickImgvewClearIcon() {
        mEditvewSearch.setText("");
    }

    @Click(R.id.tvew_scan_click)
    void onClickTvewScan() {
        getIntentHandle().intentToActivity(CategoryActivity_.class);
    }

    @Click(R.id.rlayout_search_click)
    void onClickRlayoutSearch() {
        ProductBusiness.intentToSearchActivity(this,
                ProductType.PUTONG.getValue());
    }

    private void refreshData(boolean isPullRefresh) {
        mPageInditor.setPullRefresh(isPullRefresh);
        ProductBusiness.queryGiftProducts(getHttpDataLoader(), categoryId,
                giftId, mPageInditor.getPageNum());
    }

    @Override
    public void onFooterRefresh(PullToRefreshBase<ScrollView> refreshView) {
        refreshData(false);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshBase<ScrollView> refreshView) {
        refreshData(true);
    }

    @Override
    public void onClickSelect(int position) {
        if (mCategoryAdapter.getSelectPosition() == position) {
            giftId = 0;
            mCategoryAdapter.setSelectPosition(-1);
        } else {
            if (null != mlistCategory && mlistCategory.size() > position) {
                giftId = Integer.parseInt(mlistCategory.get(position).id);
                mCategoryAdapter.setSelectPosition(position);
            }
        }

        refreshData(true);
    }

}