
package com.android.juzbao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.android.juzbao.adapter.ShopOrderAdapter;
import com.android.juzbao.adapter.ShopOrderAdapter.CallBackInteface;
import com.android.juzbao.enumerate.OrderStatus;
import com.android.juzbao.model.OrderBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.server.api.model.CommonReturn;
import com.server.api.model.Order;
import com.server.api.model.OrderDetail;
import com.server.api.model.OrderPageResult;
import com.server.api.model.Payment;
import com.server.api.service.OrderService;
import com.server.api.service.PayService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Description: 我的购买订单列表（会员）
 */
@EFragment(R.layout.fragment_shop_order)
public class ShopOrderFragment extends BaseFragment implements
        OnHeaderRefreshListener, OnFooterRefreshListener, CallBackInteface {

    private OrderStatus mOrderStatus;

    private ShopOrderAdapter mAdapter;

    @ViewById(R.id.common_listview_show)
    ListView mListView;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshView mPullToRefreshView;

    private boolean isInit = false;

    private PageInditor<Order> mPageInditor;

    public ShopOrderFragment() {

    }

    public void setOrderStatus(OrderStatus orderStatus) {
        mOrderStatus = orderStatus;
    }

    @AfterViews
    void initUI() {
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPageInditor = new PageInditor<Order>();
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(OrderService.QueryOrderRequest.class)
                || msg.valiateReq(OrderService.QueryAllOrderRequest.class)) {

            //查询订单
            if (null != mPageInditor) {
                mPageInditor.clear();
            }
            OrderPageResult response = (OrderPageResult) msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                if (null != response.Data && null != response.Data.Result) {
                    mPageInditor.add(response.Data.Result);
                }

                if (null == mAdapter) {
                    mAdapter =
                            new ShopOrderAdapter(getActivity(),
                                    mPageInditor.getAll());
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
                }
            }
            mPullToRefreshView.onFooterRefreshComplete();
            mPullToRefreshView.onHeaderRefreshComplete();
        } else if (msg.valiateReq(OrderService.CancelOrderRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(getActivity(), msg, "订单已取消");
                    OrderBusiness.queryOrderDetail(getHttpDataLoader(), mPageInditor.getSelectItem().order_id);
                } else {
                    dismissWaitDialog();
                    ShowMsg.showToast(getActivity(), response.message);
                }
            } else {
                dismissWaitDialog();
                ShowMsg.showToast(getActivity(), msg, "取消订单失败");
            }
        } else if (msg.valiateReq(OrderService.ReceiveOrderRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(getActivity(), msg, "已确认收货");
                    OrderBusiness.queryOrderDetail(getHttpDataLoader(), mPageInditor.getSelectItem().order_id);
                } else {
                    dismissWaitDialog();
                    ShowMsg.showToast(getActivity(), response.message);
                }
            } else {
                dismissWaitDialog();
                ShowMsg.showToast(getActivity(), msg, "确认收货失败");
            }
        } else if (msg.valiateReq(OrderService.OrderDetailRequest.class)) {
            OrderDetail response = (OrderDetail) msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                if (null != response && null != response.Data) {
                    if (mOrderStatus == OrderStatus.PAY) {
                        //当前订单为未提交
                        mPageInditor.remove(mPageInditor.getSelectPosition());
                        return;
                    }
                    mPageInditor.updateSelectItem(response.Data);
                }
            }
        } else if (msg.valiateReq(PayService.PayOrderRequest.class)) {
            Payment response = msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                if (null != response && null != response.Data) {
                    OrderBusiness.queryOrderDetail(getHttpDataLoader(), mPageInditor.getSelectItem().order_id);
                    ShowMsg.showToast(getActivity(), msg, "提交成功");
                } else {
                    ShowMsg.showToast(getActivity(), msg, "提交失败");
                }
            }
        } else if (msg.valiateReq(OrderService.ConfirmOrderRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(getActivity(), msg, "已提交订单");
                    OrderBusiness.queryOrderDetail(getHttpDataLoader(), mPageInditor.getSelectItem().order_id);
                } else {
                    dismissWaitDialog();
                    ShowMsg.showToast(getActivity(), response.message);
                }
            } else {
                dismissWaitDialog();
                ShowMsg.showToast(getActivity(), msg, "提交订单失败");
            }
        } else if (msg.valiateReq(OrderService.DelOrderRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    mPageInditor.remove(mPageInditor.getSelectPosition());
                    dismissWaitDialog();
                    ShowMsg.showToast(getActivity(), msg, "已成功删除");
                } else {
                    dismissWaitDialog();
                    ShowMsg.showToast(getActivity(), response.message);
                }
            } else {
                dismissWaitDialog();
                ShowMsg.showToast(getActivity(), msg, "删除失败");
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDataEmptyView().setBackgroundResource(R.drawable.transparent);
        getDataEmptyView().showViewWaiting();
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
        refreshData(true);
    }

    public void refreshData(boolean isPullRefresh) {
        if (null == mPageInditor) {
            mPageInditor = new PageInditor<Order>();
        }
        mPageInditor.setPullRefresh(isPullRefresh);
        if (mOrderStatus == OrderStatus.ALL) {
            OrderBusiness.queryAllOrders(getHttpDataLoader(), mPageInditor.getPageNum());
        } else {
            OrderBusiness.queryOrders(getHttpDataLoader(), mPageInditor.getPageNum(), mOrderStatus);
        }
    }

    public void refundSuccess() {
        OrderBusiness.queryOrderDetail(getHttpDataLoader(), mPageInditor.getSelectItem().order_id);
    }

    public void payEcoSuccess() {
        OrderBusiness.queryOrderDetail(getHttpDataLoader(), mPageInditor.getSelectItem().order_id);
    }

    /**
     * 在详情页面中，评价成功的回调
     */
    public void addShopReviewSuccess(Intent intent) {
        String strOrder = intent.getStringExtra("order");
        Order order = JsonSerializerFactory.Create().decode(strOrder, Order.class);
        mPageInditor.updateSelectItem(order);
        OrderBusiness.queryOrderDetail(getHttpDataLoader(), mPageInditor.getSelectItem().order_id);
    }

    /**
     * 更新条目
     */
    public void refreshItem(Intent intent, boolean isDelete) {
        if (isDelete) {
            mPageInditor.remove(mPageInditor.getSelectPosition());
            return;
        }
        String strOrder = intent.getStringExtra("order");
        if (!TextUtils.isEmpty(strOrder)) {
            Order order = JsonSerializerFactory.Create().decode(strOrder, Order.class);
            mPageInditor.updateSelectItem(order);
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

    /**
     * 取消订单
     */
    @Override
    public void onClickCancel(int position) {
        OrderBusiness.queryCancelOrder(getHttpDataLoader(), mPageInditor.get(position).order_id);
        showWaitDialog(2, false, R.string.common_submit_data);
    }

    /**
     * 提交订单
     */
    @Override
    public void onClickSubmit(int position) {
//        PayBusiness.queryOrderPay(getHttpDataLoader(), "", mPageInditor.get(position).order_id
//                , mPageInditor.get(position).order_no, mPageInditor.get(position).total_price);
//        showWaitDialog(2, false, R.string.common_submit_data);

        OrderBusiness.queryConfirmOrder(getHttpDataLoader(), mPageInditor.get(position).order_id);
        showWaitDialog(2, false, "正在提交...");
    }

    /**
     * 删除订单
     */
    @Override public void onClickDel(int position) {
        OrderBusiness.queryDelOrder(getHttpDataLoader(), mPageInditor.get(position).order_id);
        showWaitDialog(2, false, "正在删除...");
    }

    /**
     * 确认收货
     */
    @Override public void onClickReceiver(int position) {
        OrderService.ReceiveOrderRequest request = new OrderService.ReceiveOrderRequest();
        request.OrderId = mPageInditor.get(position).order_id;
        getHttpDataLoader().doPostProcess(request, CommonReturn.class);
    }

    @Override
    public void onClickUpdate(int position) {
        mPageInditor.updateSelectPosition(position);
    }
}