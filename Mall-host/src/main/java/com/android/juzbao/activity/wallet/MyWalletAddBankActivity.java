
package com.android.juzbao.activity.wallet;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.widget.EditText;

import com.android.mall.resource.R;
import com.server.api.model.CommonReturn;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.model.WalletBusiness;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.WalletService;

/**
 * <p>
 * Description: 提现添加银行卡
 * </p>
 *
 * @ClassName:MyWalletAddBankActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_my_wallet_add_bank)
public class MyWalletAddBankActivity extends SwipeBackActivity {

    @ViewById(R.id.editvew_bank_account_show)
    EditText mEditvewBankAccount;

    @ViewById(R.id.editvew_bank_num_show)
    EditText mEditvewBankNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("添加银行卡");
        StringUtil.addBankCardWatcher(mEditvewBankNum);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(WalletService.AddBankcardRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (CommonValidate.validateQueryState(this, msg, response,
                    "添加银行卡失败")) {
                ShowMsg.showToast(getApplicationContext(), "添加银行卡成功");
                BaseApplication.getInstance().setActivityResult(
                        ResultActivity.CODE_ADD_BACKCARD, null);
                finish();
            }
        }
    }

    @Click(R.id.tvew_submit_click)
    void onClickTvewSubmit() {
        String cardNo = mEditvewBankNum.getText().toString();
        cardNo = cardNo.replace(" ", "");

        boolean isSend =
                WalletBusiness.queryAddBankcard(this, getHttpDataLoader(),
                        cardNo, mEditvewBankAccount.getText().toString());
        if (isSend) {
            showWaitDialog(1, false, R.string.common_submit_data);
        }
    }

}