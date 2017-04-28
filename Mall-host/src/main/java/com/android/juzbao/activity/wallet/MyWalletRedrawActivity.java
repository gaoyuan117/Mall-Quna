
package com.android.juzbao.activity.wallet;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.mall.resource.R;
import com.server.api.model.Bankcard;
import com.server.api.model.BankcardPageResult;
import com.server.api.model.CommonReturn;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.model.WalletBusiness;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.WalletService;

/**
 * <p>
 * Description: 提现
 * </p>
 *
 * @ClassName:MyWalletRedrawActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_my_wallet_redraw)
public class MyWalletRedrawActivity extends SwipeBackActivity {

    @ViewById(R.id.editvew_redraw_money_show)
    EditText mEditvewRedrawMoney;

    @ViewById(R.id.editvew_redraw_bank_name_show)
    TextView mEditvewRedrawBankName;

    @ViewById(R.id.editvew_redraw_bank_num_show)
    TextView mEditvewRedrawBankNum;

    @ViewById(R.id.tvew_wallet_coupon_num_show)
    TextView mTvewWalletCouponNum;

    private String mstrBankcardId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("提现 ");
        mEditvewRedrawMoney
                .addTextChangedListener(new StringUtil.DecimalTextWatcher(
                        mEditvewRedrawMoney, 2));

        WalletBusiness.queryBankcardList(getHttpDataLoader());
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(WalletService.BankcardListRequest.class)) {
            BankcardPageResult response =
                    (BankcardPageResult) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response)) {
                if (null != response.Data && null != response.Data.Result
                        && response.Data.Result.length > 0) {
                    showBankcardInfo(response.Data.Result[0]);
                } else {

                }
            } else {
                ShowMsg.showToast(getApplicationContext(), msg, "");
            }
        }else if (msg.valiateReq(WalletService.WithdrawRequest.class)){
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response, "提现失败")) {
                ShowMsg.showToast(getApplicationContext(), response.message);
                BaseApplication.getInstance().setActivityResult(
                        ResultActivity.CODE_WHITDRAW, null);
                finish();
            }
        }
    }

    private void showBankcardInfo(Bankcard bankcard) {
        if (null == bankcard) {
            return;
        }
        mstrBankcardId = bankcard.id;
        mEditvewRedrawBankName.setText(bankcard.bank_title);
        String cardNo = bankcard.card_no;
        mEditvewRedrawBankNum.setText("尾号"
                + cardNo.substring(cardNo.length() - 4, cardNo.length()));
        mEditvewRedrawBankNum.setVisibility(View.VISIBLE);
    }

    @Click(R.id.llayout_select_bankcard_click)
    void onClickSelectBankcard() {
        getIntentHandle().intentToActivity(MyWalletBankActivity_.class);
    }

    @Click(R.id.tvew_submit_click)
    void onClickTvewSubmit() {
        boolean isSend =
                WalletBusiness.queryWithdraw(this, getHttpDataLoader(),
                        mstrBankcardId, mEditvewRedrawMoney.getText()
                                .toString());
        if (isSend) {
            showWaitDialog(1, false, R.string.common_submit_data);
        }
    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        if (ResultActivity.CODE_SELECT_BACKCARD == resultCode) {
            Bankcard bankcard =
                    JsonSerializerFactory.Create().decode(
                            intent.getStringExtra("bankcard"), Bankcard.class);
            showBankcardInfo(bankcard);
        }
    }
}