
package com.android.juzbao.activity.me;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.android.mall.resource.R;
import com.server.api.model.CommonReturn;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.model.AccountBusiness;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.enumerate.VerifyType;
import com.android.juzbao.fragment.CaptchaGetFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;
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
@EActivity(R.layout.activity_my_modify_mobile)
public class MyModifyMobileActivity extends SwipeBackActivity {

    @ViewById(R.id.editvew_phone_number_show)
    EditText mEditvewPhoneNumber;

    @ViewById(R.id.editvew_code_show)
    EditText mEditvewCode;

    private CaptchaGetFragment mCaptchaGetFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("修改手机号码");
        addVerifyFragment();
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(AccountService.EditMobileRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(getApplicationContext(), "手机号修改成功");

                    Intent intent = new Intent();
                    intent.putExtra("mobile", mEditvewPhoneNumber.getText()
                            .toString());
                    BaseApplication.getInstance().setActivityResult(
                            ResultActivity.CODE_EDIT_MOBILE_SUCCESS, intent);
                    finish();
                } else {
                    reGetCaptcha();
                    ShowMsg.showToast(getApplicationContext(), msg,
                            response.message);
                }
            } else {
                reGetCaptcha();
                ShowMsg.showToast(getApplicationContext(), msg, "手机号修改失败");
            }
        }
    }

    private void reGetCaptcha() {
        if (null != mCaptchaGetFragment) {
            mCaptchaGetFragment.setGetCaptchaBtnClickable(true);
        }
        mEditvewCode.setText("");
    }

    private void addVerifyFragment() {
        mCaptchaGetFragment = new CaptchaGetFragment() {

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
                return VerifyType.MODIFY_MOBILE.getValue();
            }
        };
        addFragment(R.id.flayout_get_captcha, mCaptchaGetFragment);
    }

    @Click(R.id.btn_register_click)
    void onClickBtnRegister() {
        String user = mEditvewPhoneNumber.getText().toString();
        String verify = mEditvewCode.getText().toString();
        boolean isSend =
                AccountBusiness.editMobile(getApplicationContext(),
                        getHttpDataLoader(), user, verify);
        if (isSend) {
            showWaitDialog(1, false, R.string.common_submit_data);
        }
    }

}