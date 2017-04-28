
package com.android.juzbao.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.widget.ListView;

import com.server.api.model.WalletRecordItem;
import com.server.api.model.WalletRecordPageResult;
import com.android.mall.resource.R;
import com.android.juzbao.adapter.WalletRecordAdapter;
import com.android.juzbao.model.WalletBusiness;
import com.android.juzbao.enumerate.WalletRecordType;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.PageInditor;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.server.api.service.WalletService;

/**
 * <p>
 * Description: 钱包收支记录
 * </p>
 *
 * @ClassName:WalletRecordFragment
 * @author: wei
 * @date: 2016-03-17
 */
@EFragment(R.layout.fragment_shop_order)
public class WalletRecordFragment extends BaseFragment implements
        OnHeaderRefreshListener, OnFooterRefreshListener {

    private WalletRecordType mWalletRecordType;

    private WalletRecordAdapter mAdapter;

    @ViewById(R.id.common_listview_show)
    ListView mListView;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshView mPullToRefreshView;

    private boolean isInit = false;

    private PageInditor<WalletRecordItem> mPageInditor;

    public WalletRecordFragment() {

    }

    public void setWalletRecordType(WalletRecordType orderStatus) {
        mWalletRecordType = orderStatus;
    }

    @AfterViews
    void initUI() {
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPageInditor = new PageInditor<WalletRecordItem>();
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(WalletService.FreezeRecordRequest.class)
                || msg.valiateReq(WalletService.RechargeRecordRequest.class)
                || msg.valiateReq(WalletService.UnFreezeRecordRequest.class)
                || msg.valiateReq(WalletService.IncomeRecordRequest.class)
                || msg.valiateReq(WalletService.PaymentRecordRequest.class)
                || msg.valiateReq(WalletService.WithdrawalRecordRequest.class)) {
            if (null != mPageInditor) {
                mPageInditor.clear();
            }
            WalletRecordPageResult response = (WalletRecordPageResult) msg.getRspObject();
            if (CommonValidate.validateQueryState(getActivity(), msg, response)) {
                if (null != response.Data && null != response.Data.Result) {
                    mPageInditor.add(response.Data.Result);
                }
                if (null == mAdapter) {
                    mAdapter =
                            new WalletRecordAdapter(getActivity(),
                                    mPageInditor.getAll());
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
                            "您还没有相关的记录");
                }
            }
            mPullToRefreshView.onFooterRefreshComplete();
            mPullToRefreshView.onHeaderRefreshComplete();
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
        getDataEmptyView().setBackgroundResource(R.drawable.transparent);
        getDataEmptyView().showViewWaiting();
        refreshData(true);
    }

    public void refreshData(boolean isPullRefresh) {
        if (null == mPageInditor) {
            mPageInditor = new PageInditor<WalletRecordItem>();
        }
        mPageInditor.setPullRefresh(isPullRefresh);

        WalletBusiness.queryMyWalletRecord(getHttpDataLoader(),
                mPageInditor.getPageNum(), mWalletRecordType);
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