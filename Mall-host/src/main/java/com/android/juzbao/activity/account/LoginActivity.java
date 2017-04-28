
package com.android.juzbao.activity.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.juzbao.activity.BindVisiterActivity_;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.Config;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.model.AccountBusiness;
import com.android.juzbao.presenter.LoginPresenter;
import com.android.juzbao.view.ILoginView;
import com.android.mall.resource.R;
import com.android.zcomponent.annotation.ZTitleMore;
import com.android.zcomponent.common.uiframe.IBaseView;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JSONParser;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.SettingsBase;
import com.android.zcomponent.util.SharedPreferencesUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.share.AccountInfo;
import com.android.zcomponent.util.share.QQconnectHandle;
import com.android.zcomponent.util.share.QQconnectHandle.AuthSuccessListener;
import com.android.zcomponent.util.share.WechatHandle;
import com.android.zcomponent.util.share.WechatHandle.WechatAuthListener;
import com.android.zcomponent.views.PasswordTransforEditView;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.Login;
import com.server.api.service.AccountService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Description:登陆
 * </p>
 */
@EActivity(R.layout.activity_login)
@ZTitleMore(false)
public class LoginActivity extends SwipeBackActivity implements
        ILoginView, IBaseView, AuthSuccessListener, WechatAuthListener {

    @ViewById(R.id.editvew_username_show)
    EditText mEditvewUsername;

    @ViewById(R.id.editvew_password_show)
    PasswordTransforEditView mEditvewPassword;

    private QQconnectHandle mQconnectHandle;

    private WechatHandle mWechatHandle;

    /**
     * 微博唯一id
     */
    private String mstrUid = "";

    private String mstrType = "";

    private String mstrNickName = "";

    private AccountInfo mAccountInfo;

    private LoginPresenter mLoginPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initUI() {
        getTitleBar().setTitleText("登录");
        mLoginPresenter = new LoginPresenter(this);
//        mWechatHandle = new WechatHandle(this);
//        mWechatHandle.setOnWechatAuthListener(this);
        mQconnectHandle = new QQconnectHandle(this);
        mQconnectHandle.setAuthSuccessListener(this);
    }

    @Override
    public void onRecvMsg(MessageData msg) throws Exception {
        if (msg.valiateReq(AccountService.LoginVisiterRequest.class)) {
            Login response = msg.getRspObject();
            if (null != response) {
                if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                    if (null != response.data) {
                        Endpoint.Token = response.data.token;
                        SharedPreferencesUtil.put(Config.TOKEN, response.data.token);
                    }
                    SettingsBase.getInstance().writeStringByKey(Config.USER_ID,
                            mstrUid);
                    SettingsBase.getInstance().writeStringByKey(Config.USER_MOBILE,
                            response.data.username);
                    SettingsBase.getInstance().writeStringByKey(
                            Config.USER_TYPE, mstrType);
                    SettingsBase.getInstance().writeStringByKey(
                            Config.WEIBO_LOGIN, "true");
                    AccountBusiness.registerEase(response.data.username);
                    AccountBusiness.loginEase(response.data.username);
                    BaseApplication.getInstance().setLogin(true);
                    ShowMsg.showToast(getApplicationContext(),R.string.login_success);
                    finish();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("openid", mstrUid);
                    bundle.putString("type", mstrType);
                    bundle.putString("nickname", mstrNickName);
                    getIntentHandle().intentToActivity(bundle,
                            BindVisiterActivity_.class);
                    ShowMsg.showToast(getApplicationContext(),
                            "请绑定个人信息");
                }
            } else {
                ShowMsg.showToast(getApplicationContext(), msg,
                        R.string.login_fail);
            }
        }
    }

    @Click(R.id.btn_login_click)
    void onClickBtnLogin() {
        String user = mEditvewUsername.getText().toString();
        String password = mEditvewPassword.getText().toString();
        mLoginPresenter.login(user, password);
    }

    @Click(R.id.rlayout_forget_password_click)
    void onClickRlayoutForgetPassword() {
        getIntentHandle().intentToActivity(FindPwdActivity_.class);
    }

    @Click(R.id.llayout_register_click)
    void onClickLlayoutRegister() {
        getIntentHandle().intentToActivity(RegisterActivity_.class);
    }

    @Click(R.id.tvew_weixin_click)
    void onClickTvewWeixin() {
        mWechatHandle.authorize();
    }

    @Click(R.id.tvew_qq_click)
    void onClickTvewQq() {
        mQconnectHandle.authorize();
    }

    @Override
    public void onQQAuthSuccess(String resopnse) {
        List<String> resultFiled = new ArrayList<String>();
        resultFiled.add("ret");
        resultFiled.add("openid"); // 用于唯一标识用户身份
        resultFiled.add("expires_in");
        resultFiled.add("access_token");
        Map<String, Object> data =
                JSONParser.parserMap(resultFiled, resopnse.toString());
        String returnCode = (String) data.get("ret");
        if ("0".equals(returnCode)) {
            mstrUid = (String) data.get("openid");
            showWaitDialog(1, false, R.string.login_loading);
            mQconnectHandle.getUserInfo();
        } else {
            dismissWaitDialog();
            ShowMsg.showToast(this, "登录鉴权失败");
        }
    }

    @Override
    public void onQQUserInfo(AccountInfo userInfo) {
        if (null != userInfo) {
            mAccountInfo = userInfo;
            mstrType = "qq";
            mstrNickName = mAccountInfo.Nickname;
            AccountBusiness.loginVister(getHttpDataLoader(), mstrUid, mstrType);
        } else {
            dismissWaitDialog();
            ShowMsg.showToast(this, "登录鉴权失败");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (null != mQconnectHandle
                    && null != mQconnectHandle.getTencentClass()) {
                mQconnectHandle.getTencentClass().onActivityResult(requestCode,
                        resultCode, data);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onActivityResultCallBack(int resultCode, Intent intent) {
        if (resultCode == ResultActivity.CODE_BIND_VISITOR) {
            AccountBusiness.loginVister(getHttpDataLoader(), mstrUid, mstrType);
            showWaitDialog(1, false, R.string.login_loading);
        } else if (resultCode == ResultActivity.CODE_REGISTER_SUCCESS) {
            String user = intent.getStringExtra("user");
            String password = intent.getStringExtra("password");

            mEditvewUsername.setText(user);
            mEditvewPassword.setText(password);
            CommonUtil.setEditViewSelectionEnd(mEditvewUsername);
            onClickBtnLogin();
        }
    }

    @Override
    public void onWechatAuthSuccess(String openId) {
        mstrUid = openId;
        mstrType = "weinxin";
        AccountBusiness.loginVister(getHttpDataLoader(), mstrUid, mstrType);
        showWaitDialog(1, false, R.string.login_loading);
    }

    @Override
    public String getAccount() {
        return mEditvewUsername.getText().toString();
    }

    @Override
    public String getPassword() {
        return mEditvewPassword.getText().toString();
    }
}