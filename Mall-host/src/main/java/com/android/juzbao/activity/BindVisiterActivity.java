
package com.android.juzbao.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.android.mall.resource.R;
import com.server.api.model.CommonReturn;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.model.AccountBusiness;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.enumerate.VerifyType;
import com.android.juzbao.fragment.CaptchaGetFragment;
import com.android.zcomponent.annotation.ZTitleMore;
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
 * @ClassName:RegisterActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_bind_visiter)
@ZTitleMore(false)
public class BindVisiterActivity extends SwipeBackActivity {

    @ViewById(R.id.editvew_phone_number_show)
    EditText mEditvewPhoneNumber;

    @ViewById(R.id.editvew_code_show)
    EditText mEditvewCode;

    @ViewById(R.id.editvew_nick_name_show)
    EditText mEditvewNick;

    private String mstrOpenId;

    private String mstrType;

    private String mstrNickName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("绑定用户信息");
        Intent intent = getIntent();
        mstrNickName = intent.getStringExtra("nickname");
        mstrOpenId = intent.getStringExtra("openid");
        mstrType = intent.getStringExtra("type");

        if (!TextUtils.isEmpty(mstrNickName)) {
            mEditvewNick.setText(mstrNickName);
        }
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
        if (msg.valiateReq(AccountService.BindVisiterRequest.class)) {
            CommonReturn response = (CommonReturn) msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    ShowMsg.showToast(getApplicationContext(), "信息绑定成功");
                    BaseApplication.getInstance().setActivityResult(
                            ResultActivity.CODE_BIND_VISITOR, null);
                    String strMobile = mEditvewPhoneNumber.getText().toString();
                    AccountBusiness.loginEase(strMobile);
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
        String mobile = mEditvewPhoneNumber.getText().toString();
        String nickname = mEditvewNick.getText().toString();
        String verify = mEditvewCode.getText().toString();
        boolean isSend =
                AccountBusiness.queryBindVister(this, getHttpDataLoader(),
                        mstrOpenId, mstrType, mobile, verify, nickname);
        if (isSend) {
            showWaitDialog(1, false, "正在绑定信息...");
        }
    }
}