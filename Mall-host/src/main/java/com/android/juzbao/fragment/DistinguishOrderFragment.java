
package com.android.juzbao.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.widget.ListView;

import com.server.api.model.CommonReturn;
import com.server.api.model.DistinguishDetail;
import com.server.api.model.DistinguishItem;
import com.server.api.model.DistinguishPageResult;
import com.android.mall.resource.R;
import com.android.juzbao.adapter.DistinguishOrderAdapter;
import com.android.juzbao.adapter.DistinguishOrderAdapter.CallBackInteface;
import com.android.juzbao.model.DistinguishBusiness;
import com.android.juzbao.enumerate.DistinguishStatus;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.server.api.service.DistinguishService;

/**
 * <p>
 * Description: 我的鉴定订单列表
 * </p>
 *
 * @ClassName:DistinguishOrderFragment
 * @author: wei
 * @date: 2015-11-10
 */
@EFragment(R.layout.fragment_shop_order)
public class DistinguishOrderFragment extends BaseFragment implements
        OnHeaderRefreshListener, OnFooterRefreshListener, CallBackInteface {

    private DistinguishStatus mOrderStatus;

    private DistinguishOrderAdapter mAdapter;

    @ViewById(R.id.common_listview_show)
    ListView mListView;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshView mPullToRefreshView;

    private boolean isInit = false;

    private PageInditor<DistinguishItem> mPageInditor;

    public DistinguishOrderFragment() {

    }

    public void setOrderStatus(DistinguishStatus orderStatus) {
        mOrderStatus = orderStatus;
    }

    @AfterViews
    void initUI() {
        getDataEmptyView().setBackgroundResource(R.drawable.transparent);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mListView.setDividerHeight(0);
        mPageInditor = new PageInditor<DistinguishItem>();
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(DistinguishService.QueryOrderRequest.class)
                || msg.valiateReq(DistinguishService.QueryAllDistinguishRequest.class)) {
            if (null != mPageInditor) {
                mPageInditor.clear();
            }
            DistinguishPageResult response = msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                if (null != response.Data && null != response.Data.Result) {
                    mPageInditor.add(response.Data.Result);
                }
                if (null == mAdapter) {
                    mAdapter =
                            new DistinguishOrderAdapter(getActivity(),
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
                    getDataEmptyView().showViewDataEmpty(true, true, msg,
                            "您还没有相关的订单");
                }
            }
            mPullToRefreshView.onFooterRefreshComplete();
            mPullToRefreshView.onHeaderRefreshComplete();
        }else if (msg.valiateReq(DistinguishService.DelDistinguishRequest.class)){
            CommonReturn response = msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(getActivity(), msg, "订单已刪除");
                    mPageInditor.remove(mPageInditor.getSelectPosition());
                } else {
                    ShowMsg.showToast(getActivity(), response.message);
                }
            } else {
                ShowMsg.showToast(getActivity(), msg, "删除订单失败");
            }
        }else if (msg.valiateReq(DistinguishService.QueryDistinguishDetailRequest.class)){
            DistinguishDetail response = (DistinguishDetail) msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                if (null != response && null != response.Data) {
                    DistinguishItem item = mPageInditor.getSelectItem();
                    item.status = response.Data.status;
                    item.status_text = response.Data.status_text;
                    mPageInditor.updateSelectItem(item);
                }
            }
        }
    }

    @Override
    public void onDataEmptyClickRefresh() {
        getDataEmptyView().showViewWaiting();
        refreshData(true);
    }

    public void queryListViewData() {
        if (isInit) {
            return;
        }

        isInit = true;
        getDataEmptyView().showViewWaiting();
        refreshData(true);
    }

    public void refreshData(boolean isPullRefresh) {
        if (null == mPageInditor) {
            mPageInditor = new PageInditor<DistinguishItem>();
        }
        mPageInditor.setPullRefresh(isPullRefresh);

        if (mOrderStatus == DistinguishStatus.ALL) {
            DistinguishBusiness.queryAllDistinguish(getHttpDataLoader(),
                    mPageInditor.getPageNum());
        } else {
            DistinguishBusiness.queryDistinguish(getHttpDataLoader(),
                    mPageInditor.getPageNum(), mOrderStatus.getValue());
        }
    }

    public void refundSuccess() {

    }

    public void delSuccess() {
        mPageInditor.remove(mPageInditor.getSelectPosition());
    }

    public void payEcoSuccess(Intent intent) {
        DistinguishBusiness.queryDistinguishDetail(getHttpDataLoader(),
                mPageInditor.getSelectItem().id);
    }

    public void refreshDistinguishStatus() {
        DistinguishBusiness.queryDistinguishDetail(getHttpDataLoader(),
                mPageInditor.getSelectItem().id);
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
    public void onClickCancel(int position) {
        DistinguishBusiness.queryDelDistinguish(getHttpDataLoader(),
                mPageInditor.get(position).id);
        showWaitDialog(1, false, R.string.common_submit_data);
    }

    @Override
    public void onClickUpdate(int position) {
        mPageInditor.updateSelectPosition(position);
    }
}