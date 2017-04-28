
package com.android.juzbao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.android.juzbao.adapter.OrderAdapter;
import com.android.juzbao.adapter.OrderAdapter.CallBackInteface;
import com.android.juzbao.dao.JifenDao;
import com.android.juzbao.enumerate.ProviderOrderStatus;
import com.android.juzbao.model.ProviderOrderBusiness;
import com.android.juzbao.model.ProviderOrderBusiness.OrderHelper;
import com.android.juzbao.provider.R;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.server.api.model.CommonReturn;
import com.server.api.model.Order;
import com.server.api.model.OrderDetail;
import com.server.api.model.OrderPageResult;
import com.server.api.service.JiFenService;
import com.server.api.service.OrderService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 我的购买订单列表(商家)
 * </p>
 *
 * @ClassName:ShopOrderFragment
 * @author: wei
 * @date: 2015-11-10
 */
@EFragment(resName = "fragment_order")
public class OrderFragment extends BaseFragment implements
        OnHeaderRefreshListener, OnFooterRefreshListener, CallBackInteface {

    /**
     * 订单状态
     */
    private ProviderOrderStatus mOrderStatus;

    private OrderAdapter mAdapter;

    /**
     * 订单ListView
     */
    @ViewById(resName = "common_listview_show")
    ListView mListView;

    @ViewById(resName = "common_pull_refresh_view_show")
    PullToRefreshView mPullToRefreshView;

    private boolean isInit = false;

    private PageInditor<Order> mPageInditor;

    private OrderHelper mOrderHelper;

    public OrderFragment() {

    }

    public void setOrderStatus(ProviderOrderStatus orderStatus) {
        mOrderStatus = orderStatus;
    }

    @AfterViews void initUI() {
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPageInditor = new PageInditor<Order>();
        mOrderHelper = new OrderHelper(getActivity(), getHttpDataLoader());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDataEmptyView().setBackgroundResource(R.drawable.transparent);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (null != mOrderHelper) {
            mOrderHelper.onRecvMsg(msg);
        }
        if (msg.valiateReq(OrderService.QueryShopOrderRequest.class) || msg.valiateReq
                (OrderService.QueryShopAllOrderRequest.class)) {
            //查询订单，all，othrt都有这里
            if (null != mPageInditor) {
                mPageInditor.clear();
            }
            OrderPageResult response = (OrderPageResult) msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                if (null != response.Data && null != response.Data.Result) {
                    mPageInditor.add(response.Data.Result);
                }
                if (null == mAdapter) {
                    mAdapter = new OrderAdapter(getActivity(), mPageInditor.getAll());
                    mAdapter.setCallBackInteface(this);
                    mPageInditor.bindAdapter(mListView, mAdapter);
                }

                if (mPageInditor.getAll().size() == response.Data.Total) {
                    mPullToRefreshView.setFooterRefreshComplete();
                } else {
                    mPullToRefreshView.setFooterVisible();
                }

                getDataEmptyView().dismiss();
            } else {
                if (mPageInditor.getAll().size() == 0) {
                    getDataEmptyView().showViewDataEmpty(true, true, msg, "您还没有相关的订单");
                } else {
                    getDataEmptyView().dismiss();
                }
            }
            mPullToRefreshView.onFooterRefreshComplete();
            mPullToRefreshView.onHeaderRefreshComplete();
        } else if (msg.valiateReq(OrderService.CancelOrderRequest.class)) {
            //取消订单
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(getActivity(), msg, "订单已取消");
                    ProviderOrderBusiness.queryProviderOrderDetail(getHttpDataLoader(),
                            mPageInditor.getSelectItem().order_id);
                } else {
                    ShowMsg.showToast(getActivity(), response.message);
                }
            } else {
                ShowMsg.showToast(getActivity(), msg, "取消订单失败");
            }
        } else if (msg.valiateReq(OrderService.ShopOrderDetailRequest.class)) {
            //查询订单详情
            OrderDetail response = (OrderDetail) msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                if (null != response && null != response.Data) {
                    mPageInditor.updateSelectItem(response.Data);
                }
            }
        } else if (msg.valiateReq(OrderService.DelOrderRequest.class)) {
            //删除订单
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(getActivity(), msg, "成功删除订单");
                    mPageInditor.remove(mPageInditor.getSelectPosition());
//                    ProviderOrderBusiness.queryProviderOrderDetail(getHttpDataLoader(),
//                    mPageInditor.getSelectItem().order_id);
                } else {
                    ShowMsg.showToast(getActivity(), response.message);
                }
            } else {
                ShowMsg.showToast(getActivity(), msg, "订单删除失败");
            }
        } else if (msg.valiateReq(JiFenService.JifenShopRefundRequest.class)) {
            CommonReturn response = msg.getRspObject();
            if (response != null && response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                ShowMsg.showToast(getActivity(), "确认退款成功");
                refreshItem();
            } else {
                ShowMsg.showToast(getActivity(), "确认退款失败");
            }
        }
    }

    @Override
    public void onDataEmptyClickRefresh() {
        getDataEmptyView().showViewWaiting();
        refreshData(true);
    }

    public void queryListData() {
        if (isInit) {
            return;
        }
        isInit = true;
        getDataEmptyView().showViewWaiting();
        refreshData(true);
    }

    /**
     * 刷新数据
     */
    public void refreshData(boolean isPullRefresh) {
        if (null == mPageInditor) {
            mPageInditor = new PageInditor<Order>();
        }
        mPageInditor.setPullRefresh(isPullRefresh);
        ProviderOrderBusiness.queryProviderOrders(getHttpDataLoader(), mPageInditor.getPageNum(),
                mOrderStatus);
    }

    public void refundSuccess() {
        ProviderOrderBusiness.queryProviderOrderDetail(getHttpDataLoader(),
                mPageInditor.getSelectItem().order_id);
    }

    public void payEcoSuccess(Intent intent) {
    }

    public void addShopReviewSuccess(Intent intent) {
        ProviderOrderBusiness.queryProviderOrderDetail(getHttpDataLoader(), mPageInditor
                .getSelectItem().order_id);
    }


    /**
     * 刷新当前Item
     */
    public void refreshItem(Order order) {
        mPageInditor.updateSelectItem(order);
    }

    /**
     * 刷新当前Item
     */
    public void refreshItem() {
        ProviderOrderBusiness.queryProviderOrderDetail(getHttpDataLoader(), mPageInditor
                .getSelectItem().order_id);
    }

    /**
     * 删除当前的Item
     */
    public void delItem() {
        mPageInditor.remove(mPageInditor.getSelectPosition());
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        refreshData(false);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refreshData(true);
    }


    /**
     * 点击删除订单
     */
    @Override
    public void onClickDelete(int position) {
        OrderService.DelOrderRequest request = new OrderService.DelOrderRequest();
        request.id = mPageInditor.get(position).order_id;
        getHttpDataLoader().doPostProcess(request, CommonReturn.class);
    }

    @Override
    public void onClickRefund(int position) {
//        ProviderOrderBusiness.queryProviderOrderRefund(
//                getHttpDataLoader(), mPageInditor.getSelectItem().order_id,
//                mPageInditor.get(position).products[0].product_id);

        JifenDao.sendOrderRefund(getHttpDataLoader(), mPageInditor.getSelectItem().order_id);
        showWaitDialog(2, false, R.string.common_submit_data);
    }

    @Override
    public void onClickRefuseRefund(int position) {
        ProviderOrderBusiness.queryProviderOrderRefuseRefund(
                getHttpDataLoader(), mPageInditor.getSelectItem().order_id,
                mPageInditor.get(position).products[0].product_id);
        showWaitDialog(2, false, R.string.common_submit_data);
    }

    @Override
    public void onClickUpdate(int position) {
        mPageInditor.updateSelectPosition(position);
    }
}