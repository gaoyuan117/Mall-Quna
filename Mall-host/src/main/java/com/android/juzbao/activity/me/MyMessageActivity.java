
package com.android.juzbao.activity.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.android.juzbao.constant.ProviderGlobalConst;
import com.android.juzbao.enumerate.MessageType;
import com.android.juzbao.model.ProviderShopBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.annotation.ZNetNotify;
import com.android.zcomponent.annotation.ZTitleMore;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.easemob.easeui.simple.EaseHelper;
import com.hyphenate.chatui.ChatActivity;
import com.hyphenate.chatui.ConversationListFragment;
import com.server.api.model.OnlineService;
import com.server.api.service.ShopService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 设置
 * </p>
 *
 * @ClassName:MyMeaageActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_my_meaage)
@ZNetNotify(false)
@ZTitleMore(false)
public class MyMessageActivity extends SwipeBackActivity {

  @ViewById(R.id.rlayout_my_help1_click)
  LinearLayout rlayout_my_help1_click;
  @ViewById(R.id.rlayout_my_help3_click)
  LinearLayout rlayout_my_help3_click;
  private ConversationListFragment mContactFragment;
  private OnlineService mOnlineService;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @AfterViews
  void initUI() {
    getTitleBar().setTitleText("消息");

    rlayout_my_help1_click.setVisibility(View.GONE);
//        rlayout_my_help3_click.setVisibility(View.GONE);

    mContactFragment = new ConversationListFragment();
    addFragment(R.id.flayout_message_show, mContactFragment);

    ProviderShopBusiness.queryShopOnlineSerivce(getHttpDataLoader());
  }

  @Override
  public void onRecvMsg(MessageData msg) throws Exception {
    if (msg.valiateReq(ShopService.OnlineServiceRequest.class)) {
      mOnlineService = msg.getRspObject();
      if (CommonValidate.validateQueryState(this, msg, mOnlineService)) {
      }
    }
  }

  @Click(R.id.rlayout_my_help1_click)
  void onClickRlayoutMyHelp1() {
    intentToMessageListActivity("物流助手", MessageType.SHIPPING.getValue());
  }

  @Click(R.id.rlayout_my_help2_click)
  void onClickRlayoutMyHelp2() {
    intentToMessageListActivity("交易信息", MessageType.ORDER.getValue());
  }

  @Click(R.id.rlayout_my_help3_click)
  void onClickRlayoutMyHelp3() {
    intentToMessageListActivity("通知消息", MessageType.NOTIFY.getValue());
  }

  @Click(R.id.rlayout_my_help4_click)
  void onClickRlayoutMyHelp4() {
    intentToMessageListActivity("定时提醒", MessageType.TIME.getValue());
  }

  @Click(R.id.rlayout_my_help5_click)
  void onClickRlayoutMyHelp5() {
    intentToMessageListActivity("小元活动", MessageType.ACTIVITY.getValue());
  }

  @Click(R.id.rlayout_my_help6_click)
  void onClickRlayoutMyHelp6() {
    if (EaseHelper.getInstance().isLoggedIn()) {
      if (null != mOnlineService && null != mOnlineService.data
          && mOnlineService.data.hx_accoun.length > 0
          && !TextUtils.isEmpty(mOnlineService.data.hx_accoun[0])) {
        ChatActivity.startChart(this, mOnlineService.data.hx_accoun[0]);
      } else {
        ChatActivity.startChart(this, ProviderGlobalConst.IM_ACCOUNT);
//        ChatActivity.startChart(this, "大众积分", ProviderGlobalConst.IM_ACCOUNT);
      }
    }
  }

  private void intentToMessageListActivity(String title, String type) {
    Bundle bundle = new Bundle();
    bundle.putString("title", title);
    bundle.putString("type", type);
    getIntentHandle()
        .intentToActivity(bundle, MyMessageListActivity_.class);
  }

}