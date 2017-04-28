package com.android.juzbao.activity.account;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.widget.EditText;

import com.android.mall.resource.R;
import com.server.api.model.CommonReturn;
import com.android.juzbao.model.AccountBusiness;
import com.android.juzbao.enumerate.VerifyType;
import com.android.juzbao.fragment.CaptchaGetFragment;
import com.android.zcomponent.annotation.ZTitleMore;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.PasswordTransforEditView;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.AccountService;


/**
 * <p>
 * Description:
 * </p>
 *
 * @ClassName:FindPwdActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_find_pwd)
@ZTitleMore(false)
public class FindPwdActivity extends SwipeBackActivity {
    @ViewById(R.id.editvew_phone_number_show)
    EditText mEditvewPhoneNumber;

    @ViewById(R.id.editvew_code_show)
    EditText mEditvewCode;

    @ViewById(R.id.editvew_password_show)
    PasswordTransforEditView mEditvewPassword;

    @ViewById(R.id.editvew_password_confirm_show)
    PasswordTransforEditView mEditvewPasswordConfirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("找回密码");
        addVerifyFragment();
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(AccountService.ResetPasswordRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(getApplicationContext(),
                            "找回密码成功");
                    finish();
                } else {
                    ShowMsg.showToast(getApplicationContext(), response.message);
                }
            } else {
                ShowMsg.showToast(getApplicationContext(),
                        "找回密码失败");
            }
        }
    }

    private void addVerifyFragment() {
        final CaptchaGetFragment fragment = new CaptchaGetFragment() {

            @Override
            public String getMobile() {
                String user = mEditvewPhoneNumber.getText().toString();
                return user;
            }

            @Override
            public EditText getCodeEditText() {
                return mEditvewCode;
            }

            @Override
            public String getCodeType() {
                return VerifyType.FORGETTEN.getValue();
            }

        };
        addFragment(R.id.flayout_get_captcha, fragment);
    }

    @Click(R.id.btn_register_click)
    void onClickBtnRegister() {
        String user = mEditvewPhoneNumber.getText().toString();
        String password = mEditvewPassword.getText().toString();
        String conformPassword = mEditvewPasswordConfirm.getText().toString();
        String verify = mEditvewCode.getText().toString();
        boolean isSend = AccountBusiness.resetPassword(getApplicationContext(), getHttpDataLoader(),
                user, password, conformPassword, verify);
        if (isSend) {
            showWaitDialog(1, false, R.string.common_submit_data);
        }
    }

}