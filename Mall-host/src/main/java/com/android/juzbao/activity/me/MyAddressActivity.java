
package com.android.juzbao.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.juzbao.activity.jifen.JifenConvertActivity_;
import com.android.juzbao.activity.order.OrderEnsureActivity_;
import com.android.juzbao.adapter.MyAddressAdapter;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.dao.AddressDao;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.PullToRefreshView.OnFooterRefreshListener;
import com.android.zcomponent.views.PullToRefreshView.OnHeaderRefreshListener;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.AddressService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Description: 收货地址管理
 * </p>
 *
 * @ClassName:MyAddressActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_my_address)
public class MyAddressActivity extends SwipeBackActivity implements
        OnHeaderRefreshListener, OnFooterRefreshListener {

    @ViewById(R.id.common_listview_show)
    ListView mListView;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshView mPullToRefreshView;

    private List<com.server.api.model.Address.Data> mlistAddress;

    private MyAddressAdapter mMyAddressAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        mListView.setDividerHeight(0);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setFooterInvisible();

        if (getIntentHandle().isIntentFrom(OrderEnsureActivity_.class) || getIntentHandle().isIntentFrom(JifenConvertActivity_.class)) {
            getTitleBar().setTitleText("选择收货地址");
        } else {
            getTitleBar().setTitleText("收货地址管理");
        }

        AddressDao.sendCmdQueryMyAddress(getHttpDataLoader());
        getDataEmptyView().showViewWaiting();
    }

    @Override
    public void onDataEmptyClickRefresh() {
        AddressDao.sendCmdQueryMyAddress(getHttpDataLoader());
        getDataEmptyView().showViewWaiting();
    }

    private void clear() {
        if (null != mlistAddress) {
            mlistAddress.clear();
        }
        if (null != mMyAddressAdapter) {
            mMyAddressAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(AddressService.GetReceiverAddressRequest.class)) {
            //我的收获地址列表
            com.server.api.model.Address response = msg.getRspObject();
            mPullToRefreshView.onHeaderRefreshComplete();
            if (null != response && response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                if (null != response.Data && response.Data.length > 0) {
                    //显示收货地址列表
                    mlistAddress = new ArrayList<com.server.api.model.Address.Data>(Arrays.asList(response.Data));
                    mMyAddressAdapter = new MyAddressAdapter(this, mlistAddress);
                    mListView.setAdapter(mMyAddressAdapter);
                    getDataEmptyView().setVisibility(View.GONE);
                } else {
                    clear();
                    getDataEmptyView().showViewDataEmpty(false, true, msg,
                            R.string.personal_set_address);
                }
            } else {
                clear();
                getDataEmptyView().showViewDataEmpty(false, true, msg,
                        R.string.personal_set_address);
            }
        }
    }

    /**
     * 新增收货地址
     */
    @Click(R.id.btn_submit_click)
    void onClickBtnSubmit() {
        getIntentHandle().intentToActivity(MyAddressEditActivity_.class);
    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        if (resultCode == ResultActivity.CODE_ADDRESS_ADD_SUCCESS
                || resultCode == ResultActivity.CODE_ADDRESS_DEL_SUCCESS) {
            mPullToRefreshView.headerRefreshing();
            AddressDao.sendCmdQueryMyAddress(getHttpDataLoader());
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        AddressDao.sendCmdQueryMyAddress(getHttpDataLoader());
    }
}