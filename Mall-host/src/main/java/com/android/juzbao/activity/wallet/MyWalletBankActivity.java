
package com.android.juzbao.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.juzbao.adapter.MyWalletBankAdapter;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.model.WalletBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.views.PullToRefreshView;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.Bankcard;
import com.server.api.model.BankcardPageResult;
import com.server.api.service.WalletService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Description: 提现银行卡
 * </p>
 *
 * @ClassName:MyWalletBankActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_my_wallet_bank)
public class MyWalletBankActivity extends SwipeBackActivity {

    @ViewById(R.id.common_listview_show)
    ListView mListView;

    @ViewById(R.id.common_pull_refresh_view_show)
    PullToRefreshView mPullToRefreshView;

    @ViewById(R.id.btn_submit)
    Button mBtnSubmit;

    private MyWalletBankAdapter mAdapter;

    private List<Bankcard> mlistBankCard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        mPullToRefreshView.setHeaderInvisible();
        mPullToRefreshView.setFooterInvisible();
        mBtnSubmit.setVisibility(View.GONE);
        getTitleBar().setTitleText("银行卡管理");
        getTitleBar().showRightTextView("添加");
        getDataEmptyView().showViewWaiting();
        WalletBusiness.queryBankcardList(getHttpDataLoader());
    }

    @Override
    public void onDataEmptyClickRefresh() {
        getDataEmptyView().showViewWaiting();
        WalletBusiness.queryBankcardList(getHttpDataLoader());
    }

    @Override
    public void onTitleBarRightFirstViewClick(View view) {
        getIntentHandle().intentToActivity(MyWalletAddBankActivity_.class);
    }

    @Click(R.id.btn_submit)
    void onClickSubmit() {
        if (null != mAdapter) {
            int position = mAdapter.getSelectPosition();

            Intent intent = new Intent();
            intent.putExtra(
                    "bankcard",
                    JsonSerializerFactory.Create().encode(
                            mlistBankCard.get(position)));
            BaseApplication.getInstance().setActivityResult(
                    ResultActivity.CODE_SELECT_BACKCARD, intent);
            finish();
        }
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(WalletService.BankcardListRequest.class)) {
            BankcardPageResult response = msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {

                if (null != response.Data && null != response.Data.Result
                        && response.Data.Result.length > 0) {
                    mlistBankCard =
                            new ArrayList<Bankcard>(
                                    Arrays.asList(response.Data.Result));
                    mAdapter = new MyWalletBankAdapter(this, mlistBankCard);
                    mListView.setAdapter(mAdapter);
                    mBtnSubmit.setVisibility(View.VISIBLE);
                    getDataEmptyView().dismiss();
                } else {
                    mBtnSubmit.setVisibility(View.GONE);
                    getDataEmptyView().showViewDataEmpty(true, false, msg,
                            "您还没有添加银行卡");
                }
            } else {
                mBtnSubmit.setVisibility(View.GONE);
                getDataEmptyView().showViewDataEmpty(true, false, msg,
                        "您还没有添加银行卡");
            }
        }
    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        if (ResultActivity.CODE_ADD_BACKCARD == resultCode) {
            if (null == mlistBankCard || mlistBankCard.size() == 0) {
                getDataEmptyView().showViewWaiting();
            }
            WalletBusiness.queryBankcardList(getHttpDataLoader());
        }
    }

}