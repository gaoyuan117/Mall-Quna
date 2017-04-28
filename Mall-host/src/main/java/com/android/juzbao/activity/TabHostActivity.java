
package com.android.juzbao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.Config;
import com.android.juzbao.fragment.CartFragment_;
import com.android.juzbao.fragment.CategroyFragment_;
import com.android.juzbao.fragment.circle.CircleFragment;
import com.android.juzbao.fragment.HomeFragment_;
import com.android.juzbao.fragment.MyFragment_;
import com.android.juzbao.model.AccountBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.activity.BaseNavgationActivity;
import com.android.zcomponent.annotation.ZMainActivity;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.MyLayoutAdapter;
import com.android.zcomponent.util.SettingsBase;
import com.android.zcomponent.util.SharedPreferencesUtil;
import com.server.api.model.Login;
import com.server.api.model.Version;
import com.server.api.service.AccountService;

@ZMainActivity
public class TabHostActivity extends BaseNavgationActivity {

  /**
   * 添加便面信息
   */
  private String[] titles = {"首页", "分类", "圈子", "购物车", "我的"};

  private int[] drawables = {
      R.drawable.tabhost_index_bg_selector,
      R.drawable.tabhost_fenlei_bg_selector,
      //新增便民信息
      R.drawable.tabhost_circlr_bg_selector,
      R.drawable.tabhost_gwc_bg_selector,
      R.drawable.tabhost_my_bg_selector

  };

  private Fragment[] fragment = {
      new HomeFragment_(),
      new CategroyFragment_(),
      new CircleFragment(),
      new CartFragment_(),
      new MyFragment_()};

  @Override
  public void onCreate(Bundle arg0) {
    new MyLayoutAdapter(this);
    super.onCreate(arg0);
//    AccountBusiness.queryAppInfo(getHttpDataLoader());
//    AccountBusiness.doDefaultLogin(getHttpDataLoader());
//    AccountBusiness.queryVersion(getHttpDataLoader());
    if (!TextUtils.isEmpty(SharedPreferencesUtil.get(Config.USER_MOBILE)))
      AccountBusiness.loginEase(SharedPreferencesUtil.get(Config.USER_MOBILE));

  }

  public void queryAppInfo() {
    AccountBusiness.queryAppInfo(getHttpDataLoader());
  }

  @Override
  public void onRecvMsg(MessageData msg) throws Exception {
    if (msg.valiateReq(AccountService.LoginVisiterRequest.class) || msg.valiateReq(AccountService
        .LoginRequest.class)) {
      Login response = (Login) msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          Endpoint.Token = response.data.token;

          if (!TextUtils.isEmpty(response.data.username)) {
            SettingsBase.getInstance().writeStringByKey(
                Config.USER_MOBILE, response.data.username);
            AccountBusiness.loginEase(response.data.username);
          } else {
            String user = SettingsBase.getInstance().readStringByKey(
                Config.USER_ID);
            AccountBusiness.loginEase(user);
          }
          BaseApplication.getInstance().setLogin(true);
        }
      }
    } else if (msg.valiateReq(AccountService.VersionRequest.class)) {
      Version response = (Version) msg.getRspObject();
      if (CommonValidate.validateQueryState(this, msg, response)) {
        AccountBusiness.checkAppUpdate(response.data[0]);
      }
    }
  }

  @Override
  public void showCurrentTab(int position) {
    super.showCurrentTab(position);
    if (position == 3 || position == 4) {
      getTitleBar().getBackTextView().setVisibility(View.GONE);
      getTitleBar().setTitleText(titles[position]);
      setTitleBarVisibility(View.VISIBLE);
    } else {
      setTitleBarVisibility(View.GONE);
    }
  }


  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  public int[] getDrawables() {
    return drawables;
  }

  @Override
  public String[] getTitles() {
    return titles;
  }

  @Override
  public int getTabSelectedBackground() {
    return R.drawable.transparent;
  }

  @Override
  public int getTabSelectedTextColor() {
    return R.color.tabhost_tab_tv_color_selector1;
  }

  @Override
  public Fragment[] getFragments() {
    return fragment;
  }

  @Override
  public int getNavBackgroundResource() {
    return R.drawable.ltgray;
  }

  @Override
  public int getNavHeight() {
    return (int) (50 * MyLayoutAdapter.getInstance().getDensityRatio());
  }

  @Override
  public int getBackgroundResource() {
    return R.drawable.common_bg;
  }

  @Override
  public boolean isSetContentPadding() {
    return true;
  }

  @Override
  public int getTextDipSize() {
    return getResources().getDimensionPixelSize(R.dimen.dimen_text_sm);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }



}
