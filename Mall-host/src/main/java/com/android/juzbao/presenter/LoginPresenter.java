package com.android.juzbao.presenter;

import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.Config;
import com.android.juzbao.model.AccountBusiness;
import com.android.juzbao.view.ILoginView;
import com.android.mall.resource.R;
import com.android.zcomponent.common.uiframe.BasePresenter;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.jpush.JPushUtil;
import com.android.zcomponent.util.SettingsBase;
import com.android.zcomponent.util.SharedPreferencesUtil;
import com.android.zcomponent.util.encrypt.AES;
import com.server.api.model.Login;
import com.server.api.service.AccountService;

/**
 * Created by WEI on 2016/6/28.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {

  public LoginPresenter(ILoginView view) {
    super(view);
  }

  public void login(String user, String password) {
    boolean isSend = AccountBusiness.login(getBaseView(),
        getHttpDataLoader(), user, password);
    if (isSend) {
      getBaseView().showWaitDialog(1, false, R.string.login_loading);
    }
  }

  @Override
  public void onRecvMsg(MessageData msg) throws Exception {
    if (msg.valiateReq(AccountService.LoginRequest.class)) {
      Login response = msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          if (null != response.data) {
            Endpoint.Token = response.data.token;
            SharedPreferencesUtil.put(Config.TOKEN, response.data.token);
          }

          String username = mView.get().getAccount();
          String passwrod = mView.get().getPassword();

          AccountBusiness.loginEase(username);
          SharedPreferencesUtil.put(Config.USER_MOBILE, username);

           JPushUtil.startPushServices(BaseApplication.app,username,username);


          SettingsBase.getInstance().writeStringByKey(
              Config.WEIBO_LOGIN, "");
          SettingsBase.getInstance().writeStringByKey(Config.USER_ID,
              username);
          SettingsBase.getInstance().writeStringByKey(Config.USER_MOBILE,
              username);
          SettingsBase.getInstance().writeStringByKey(
              Config.USER_PASSWORD, AES.encrypt(passwrod));

          BaseApplication.getInstance().setLogin(true);
          getBaseView().showToast(R.string.login_success);
          getBaseView().finishActivity();
        } else {
          getBaseView().showToast(response.message);
        }
      } else {
        getBaseView().showToast(R.string.login_fail);
      }
    }
  }
}
