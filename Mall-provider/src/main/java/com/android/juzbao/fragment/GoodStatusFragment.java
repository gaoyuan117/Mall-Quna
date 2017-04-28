
package com.android.juzbao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;

import com.android.juzbao.adapter.GoodsManageItemsAdapter;
import com.android.juzbao.adapter.GoodsManageItemsAdapter.CallBackInteface;
import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.enumerate.GoodsStatus;
import com.android.juzbao.model.ProviderProductBusiness;
import com.android.juzbao.provider.R;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.common.uiframe.FramewrokApplication;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.ShowMsg.IConfirmDialog;
import com.android.zcomponent.util.ValidateUtil;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.server.api.model.CommonReturn;
import com.server.api.model.ProductItem;
import com.server.api.model.Products;
import com.server.api.service.ProductService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 我的购买订单列表
 * </p>
 *
 * @ClassName:ShopOrderFragment
 * @author: wei
 * @date: 2015-11-10
 */
@EFragment(resName = "fragment_goods_status")
public class GoodStatusFragment extends BaseFragment implements
        OnHeaderRefreshListener, OnFooterRefreshListener, CallBackInteface {
    private GoodsStatus mGoodsStatus;

    private GoodsManageItemsAdapter mAdapter;

    RadioButton mRadionBtnSelling;

    RadioButton mRadionBtnWarehouse;

    @ViewById(resName = "common_listview_show")
    ListView mListView;

    @ViewById(resName = "common_pull_refresh_view_show")
    PullToRefreshView mPullToRefreshView;

    private boolean isFragmentInstantiate = false;

    private PageInditor<ProductItem> mPageInditor =
            new PageInditor<ProductItem>();

    private int status = 1;

    public GoodStatusFragment() {

    }

    public void setOrderStatus(GoodsStatus orderStatus, int status) {
        this.mGoodsStatus = orderStatus;
        this.status = status;
    }

    public void setTotalCountView(RadioButton btnSelling,
                                  RadioButton btnWarehouse) {
        mRadionBtnSelling = btnSelling;
        mRadionBtnWarehouse = btnWarehouse;
    }

    @AfterViews
    void initUI() {
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDataEmptyView().setBackgroundResource(R.drawable.transparent);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(ProductService.MyProductsRequest.class)) {
            if (null != mPageInditor) {
                mPageInditor.clear();
            }
            mPullToRefreshView.onFooterRefreshComplete();
            mPullToRefreshView.onHeaderRefreshComplete();
            Products responseProduct = (Products) msg.getRspObject();
            if (ProviderProductBusiness.validateQueryProducts(responseProduct)) {
                mPageInditor.add(responseProduct.Data.Results);
                setProductAdapter(status);
                if (status == 1) {
                    mRadionBtnSelling.setText("已发布( "
                            + responseProduct.Data.Total + " )");
                } else {
                    mRadionBtnWarehouse.setText("待发布( "
                            + responseProduct.Data.Total + " )");
                }

                if (mPageInditor.getAll().size() == responseProduct.Data.Total) {
                    mPullToRefreshView.setFooterRefreshComplete();
                } else {
                    mPullToRefreshView.setFooterVisible();
                }
                getDataEmptyView().setVisibility(View.GONE);
            } else {
                if (status == 1) {
                    mRadionBtnSelling.setText("已发布( 0 )");
                } else {
                    mRadionBtnWarehouse.setText("待发布( 0 )");
                }

                if (!ValidateUtil.isListEmpty(mPageInditor.getAll())) {
                    getDataEmptyView().setVisibility(View.GONE);
                } else {
                    String message =
                            ProviderProductBusiness.validateQueryState(
                                    responseProduct,
                                    "您还没有相关数据");
                    getDataEmptyView().showViewDataEmpty(true, true, msg,
                            message);
                }
            }
        } else if (msg.valiateReq(ProductService.PutawayProductRequest.class)) {
            CommonReturn response = msg.getRspObject();
            if (response != null && response.code == -3) {
                if (getActivity() != null) {
                    ShowMsg.showToast(getActivity(),"您的积分不足商品赠送积分，不能上架哦");
                }
                return;
            }
            if (response != null  && response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                ShowMsg.showToast(getActivity(), "上架成功");
                mPullToRefreshView.headerRefreshing();
                FramewrokApplication.getInstance().setActivityResult(
                        ProviderResultActivity.CODE_EDIT_PRODUCT_PUTAWAY, null);
            }else {
                ShowMsg.showToast(getActivity(),"上架失败");
            }
        } else if (msg.valiateReq(ProductService.HaltProductRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response,
                    "下架失败")) {
                ShowMsg.showToast(getActivity(), "下架成功");
                mPullToRefreshView.headerRefreshing();
                FramewrokApplication.getInstance().setActivityResult(
                        ProviderResultActivity.CODE_EDIT_PRODUCT_PUTAWAY, null);
            }
        } else if (msg.valiateReq(ProductService.DelProductRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response,
                    "删除商品失败")) {
                ShowMsg.showToast(getActivity(), "删除商品成功");

                mPageInditor.remove(mPageInditor.getSelectPosition());
            }
        }
    }

    private void setProductAdapter(int status) {
        if (null == mAdapter) {
            mAdapter =
                    new GoodsManageItemsAdapter(getActivity(),
                            mPageInditor.getAll());
            mPageInditor.bindAdapter(mListView, mAdapter);
            mAdapter.setCallBackInteface(GoodStatusFragment.this);
            mAdapter.setGoodsState(mGoodsStatus);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDataEmptyClickRefresh() {
        getDataEmptyView().showViewWaiting();
        refreshData(true);
    }

    public void queryListViewData() {
        if (!isFragmentInstantiate) {
            isFragmentInstantiate = true;
            getDataEmptyView().showViewWaiting();
            ProviderProductBusiness.queryMyProducts(getHttpDataLoader(),
                    mGoodsStatus.getValue(), mPageInditor.getPageNum(), status);
        }
    }

    public void refundSuccess() {

    }

    public void payEcoSuccess(Intent intent) {

    }

    public void addShopReviewSuccess(Intent intent) {

    }

    public void refreshData(boolean isRefreshByPull) {
        mPageInditor.setPullRefresh(isRefreshByPull);

        if (null != mGoodsStatus) {
            ProviderProductBusiness.queryMyProducts(getHttpDataLoader(),
                    mGoodsStatus.getValue(), mPageInditor.getPageNum(), status);
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        refreshData(false);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refreshData(true);
    }

    @Override
    public void onClickUpdate(int position, boolean isPutaway) {
        if (isPutaway) {
            ProviderProductBusiness.queryPutawayProduct(getHttpDataLoader(),
                    mPageInditor.get(position).id);
        } else {
            ProviderProductBusiness.queryHaltProduct(getHttpDataLoader(),
                    mPageInditor.get(position).id);
        }
        showWaitDialog(1, false, R.string.common_submit_data);
    }

    @Override
    public void onClickDelete(final int position) {
        ShowMsg.showConfirmDialog(getActivity(), new IConfirmDialog() {

            @Override
            public void onConfirm(boolean confirmValue) {
                if (confirmValue) {
                    ProviderProductBusiness.queryDelProduct(getHttpDataLoader(),
                            mPageInditor.get(position).id);
                    showWaitDialog(1, false, R.string.common_submit_data);
                }
            }
        }, "确定", "取消", "是否确认删除该商品？");
    }
}