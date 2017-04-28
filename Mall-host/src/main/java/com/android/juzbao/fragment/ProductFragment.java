
package com.android.juzbao.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.android.juzbao.adapter.ProductListAdapter;
import com.android.juzbao.enumerate.ProductType;
import com.android.juzbao.model.ProductBusiness;
import com.android.mall.resource.R;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.server.api.model.ProductItem;
import com.server.api.model.Products;
import com.server.api.service.ProductService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


/**
 * 商品列表Fragment
 */
@EFragment(R.layout.fragment_common_listview)
public class ProductFragment extends BaseFragment implements
        OnHeaderRefreshListener, OnFooterRefreshListener {

    @ViewById(R.id.common_listview_show)
    ListView mGridView;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshView mPullToRefreshView;

    private PageInditor<ProductItem> mPageInditor =
            new PageInditor<ProductItem>();

    private String mstrCurCategoryId;

    private String mstrCategoryId;

    private String mstrType;

    private String mstrSort = "";

    private ProductListAdapter mAdapter;

    @AfterViews
    void initUI() {
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);

        getDataEmptyView().setBackgroundResource(R.drawable.transparent);
        getDataEmptyView().showViewWaiting();

        refreshData(true, getArguments());
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
            Products responseProduct = msg.getRspObject();
            if (ProductBusiness.validateQueryProducts(responseProduct)) {
                mPageInditor.add(responseProduct.Data.Results);

                if (null == mAdapter) {
                    mAdapter = new ProductListAdapter(getActivity(), mPageInditor.getAll());
                    mPageInditor.bindAdapter(mGridView, mAdapter);
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
                    getDataEmptyView().dismiss();
                } else {
                    String message = ProductBusiness.validateQueryState(responseProduct,
                                    getString(R.string.common_data_empty));
                    //错误信息。位置错误
                    getDataEmptyView().showViewDataEmpty(false, false, msg, message);
                }
            }
        }else if (msg.valiateReq(ProductService.SearchRequest.class)){

            mPullToRefreshView.onFooterRefreshComplete();
            mPullToRefreshView.onHeaderRefreshComplete();
            if (null != mPageInditor) {
                mPageInditor.clear();
            }
            Products rsp = (Products) msg.getRspObject();
            if (ProductBusiness.validateQueryProducts(rsp)) {
                mPageInditor.add(rsp.Data.Results);

                if (null != mAdapter) {
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter =
                            new ProductListAdapter(getActivity(),
                                    mPageInditor.getAll());
                    mPageInditor
                            .bindAdapter(mGridView, mAdapter);
                }

                if (mPageInditor.size() == rsp.Data.Total) {
                    mPullToRefreshView.setFooterRefreshComplete();
                } else {
                    mPullToRefreshView.setFooterVisible();
                }
                getDataEmptyView().dismiss();
            } else {
                getDataEmptyView().showViewDataEmpty(false, false, msg,
                        "查询不到相关信息");
            }
        }
    }

    public void headRefreshing(){
        mPullToRefreshView.headerFirstRefreshing();
    }

    public void refreshData(){
        mPullToRefreshView.headerFirstRefreshing();
        mstrCurCategoryId = mstrCategoryId;
        refreshData(true);
    }

    public void refreshData(boolean isPullRefresh){
        mPageInditor.setPullRefresh(isPullRefresh);

        if (TextUtils.isEmpty(mstrType)) {
            mstrType = ProductType.PUTONG.getValue();
        }
        ProductBusiness.queryProducts(getHttpDataLoader(),
                mstrCurCategoryId, mstrType,
                mstrSort, mPageInditor.getPageNum());
    }

    public void setCurCategoryId(String category){
        mstrCurCategoryId = category;
    }

    public void setCurSort(String sort){
        mstrSort = sort;
    }

    public void refreshData(boolean isPullRefresh, Bundle bundle) {
        if (null != bundle){
            mstrCategoryId = bundle.getString("category_id");
            mstrCurCategoryId = new String(mstrCategoryId);
            mstrType = bundle.getString("type");
            refreshData(isPullRefresh);
        }
    }

    public void searchData(boolean isPullRefresh, String strShopName) {
        mPageInditor.setPullRefresh(isPullRefresh);
        if (TextUtils.isEmpty(mstrType)) {
            mstrType = ProductType.PUTONG.getValue();
        }
        ProductBusiness.querySearchProducts(getHttpDataLoader(),
                mstrType, strShopName, mPageInditor.getPageNum());
    }

    public void clearCache() {
        mPageInditor.setPullRefresh(true);
        if (null != mPageInditor) {
            mPageInditor.clear();
        }

        refreshData();
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
