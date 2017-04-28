
package com.android.juzbao.activity.wallet;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.android.mall.resource.R;
import com.android.juzbao.activity.order.OrderPayActivity_;
import com.server.api.model.MyWallet;
import com.android.juzbao.model.WalletBusiness;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.WalletService;

/**
 * <p>
 * Description: 我的钱包
 * </p>
 *
 * @ClassName:MyWalletActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_my_wallet)
public class MyWalletActivity extends SwipeBackActivity {

    @ViewById(R.id.tvew_wallet_freeze_amount_show)
    TextView mTvewWalletFreezeMoney;

    @ViewById(R.id.tvew_wallet_amount_show)
    TextView mTvewWalletMoney;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("我的钱包");

        String strWallet = getIntent().getStringExtra("wallet");
        if (!TextUtils.isEmpty(strWallet)) {
            MyWallet.Data wallet =
                    JsonSerializerFactory.Create().decode(strWallet,
                            MyWallet.Data.class);
            showWalletMoney(wallet);
        }
        WalletBusiness.queryMyWallet(getHttpDataLoader());
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(WalletService.MyWalletRequest.class)) {
            MyWallet response = (MyWallet) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                showWalletMoney(response.Data);
            } else {

            }
        }
    }

    @Click(R.id.tvew_redraw_click)
    public void onClickTvewRedraw() {
        getIntentHandle().intentToActivity(MyWalletRedrawActivity_.class);
    }

    @Click(R.id.tvew_recharge_click)
    public void onClickTvewReRecharge() {
        getIntentHandle().intentToActivity(OrderPayActivity_.class);
    }

    @Click(R.id.rlayout_in_record_click)
    public void onClickRlayoutInRecord() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("expense", false);
        getIntentHandle().intentToActivity(bundle, MyWalletRecordActivity_.class);
    }

    @Click(R.id.rlayout_out_record_click)
    public void onClickRlayoutOutRecord() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("expense", true);
        getIntentHandle().intentToActivity(bundle, MyWalletRecordActivity_.class);
    }

    private void showWalletMoney(MyWallet.Data wallet) {
        if (null != wallet) {
            mTvewWalletMoney.setText("¥ "
                    + StringUtil.formatProgress(wallet.amount));
            mTvewWalletFreezeMoney.setText("¥ "
                    + StringUtil.formatProgress(wallet.freeze_amount));
        } else {
            mTvewWalletMoney.setText("¥ 0.00");
            mTvewWalletFreezeMoney.setText("¥ 0.00");
        }
    }

    @UiThread(delay = 3000)
    void queryMyWalletDelay() {
        WalletBusiness.queryMyWallet(getHttpDataLoader());
    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        if (resultCode == ResultActivity.CODE_WHITDRAW) {
            WalletBusiness.queryMyWallet(getHttpDataLoader());
        } else if (resultCode == ResultActivity.CODE_PAY_ECO_SUCCESS) {
            queryMyWalletDelay();
        }
    }
}