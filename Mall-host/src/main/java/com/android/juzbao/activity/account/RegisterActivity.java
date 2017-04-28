
package com.android.juzbao.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.enumerate.VerifyType;
import com.android.juzbao.fragment.CaptchaGetFragment;
import com.android.juzbao.model.AccountBusiness;
import com.android.mall.resource.R;
import com.android.zcomponent.annotation.ZTitleMore;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.PasswordTransforEditView;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.CommonReturn;
import com.server.api.service.AccountService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description:
 * </p>
 *
 * @ClassName:RegisterActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_register)
@ZTitleMore(false)
public class RegisterActivity extends SwipeBackActivity {
    @ViewById(R.id.editvew_phone_number_show)
    EditText mEditvewPhoneNumber;

    @ViewById(R.id.editvew_code_show)
    EditText mEditvewCode;

    @ViewById(R.id.editvew_recommand_show)
    EditText mEditvewRecommand;

    @ViewById(R.id.editvew_password_show)
    PasswordTransforEditView mEditvewPassword;

    @ViewById(R.id.editvew_confirm_password_show)
    PasswordTransforEditView mEditvewConfirmPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("注册");
        addVerifyFragment();
    }

    private void addVerifyFragment() {
        final CaptchaGetFragment fragment = new CaptchaGetFragment() {

            @Override
            public String getMobile() {
                return mEditvewPhoneNumber.getText().toString();
            }

            @Override
            public EditText getCodeEditText() {
                return mEditvewCode;
            }

            @Override
            public String getCodeType() {
                return VerifyType.REGISTER.getValue();
            }

        };
        addFragment(R.id.flayout_get_captcha, fragment);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(AccountService.RegisterRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(getApplicationContext(),
                            R.string.register_success);
                    String user = mEditvewPhoneNumber.getText().toString();
                    String password = mEditvewPassword.getText().toString();

                    AccountBusiness.registerEase(user);
                    Intent intent = new Intent();
                    intent.putExtra("user", user);
                    intent.putExtra("password", password);
                    BaseApplication.getInstance().setActivityResult(
                            ResultActivity.CODE_REGISTER_SUCCESS, intent);

                    finish();
                } else {
                    ShowMsg.showToast(getApplicationContext(), response.message);
                }
            } else {
                ShowMsg.showToast(getApplicationContext(),
                        R.string.register_fail);
            }
        }
    }

    @Click(R.id.btn_register_click)
    void onClickBtnRegister() {
        String user = mEditvewPhoneNumber.getText().toString();
        String password = mEditvewPassword.getText().toString();
        String conformPassword = mEditvewConfirmPassword.getText().toString();
        String verify = mEditvewCode.getText().toString();
        String referrer = mEditvewRecommand.getText().toString();
        boolean isSend =
                AccountBusiness.register(getApplicationContext(),
                        getHttpDataLoader(), user, password, conformPassword,
                        verify, referrer);
        if (isSend) {
            showWaitDialog(1, false, R.string.register_loading);
        }
    }
}