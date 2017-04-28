
package com.android.juzbao.activity.order;

import android.app.Activity;
import android.os.Bundle;

import com.android.juzbao.activity.TabHostActivity;
import com.android.juzbao.application.BaseApplication;
import com.android.mall.resource.R;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

/**
 * Description: 提交订单 订单支付成功
 */
@EActivity(R.layout.activity_order_success)
public class OrderSuccessActivity extends SwipeBackActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @AfterViews
  void initUI() {
    getTitleBar().setTitleText("提交订单");
  }


  @Click(R.id.btn_back_to_home_click)
  void onClickBackToHome() {
    backToHome();
    TabHostActivity activity = BaseApplication.getInstance().getActivity(TabHostActivity.class);
    if (null != activity) {
      activity.setSelectedTab(0);
    }
    finish();
  }

  @Click(R.id.btn_back_to_me_click)
  void onClickBackToMe() {
    backToHome();
    TabHostActivity activity = BaseApplication.getInstance().getActivity(TabHostActivity.class);
    if (null != activity) {
      activity.setSelectedTab(4);
    }
    finish();
  }

  /**
   * 返回首页
   */
  private void backToHome() {
    List<Activity> activityList = BaseApplication.getInstance().getActivitys();
    for (int i = 0; i < activityList.size(); i++) {
      if (activityList.get(i) instanceof TabHostActivity || activityList.get(i) instanceof
          OrderSuccessActivity) {
      } else {
        activityList.get(i).finish();
      }
    }
  }
}