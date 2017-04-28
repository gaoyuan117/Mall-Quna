
package com.hyphenate.chatui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.juzbao.chart.R;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;

public class ChatActivity extends EaseBaseActivity {

  public static ChatActivity activityInstance;

  private EaseChatFragment chatFragment;

  String toChatUsername;

//  static String toNickName = "";

  @Override
  protected void onCreate(Bundle arg0) {
    super.onCreate(arg0);
    setContentView(R.layout.activity_chat);
    activityInstance = this;
    // 聊天人或群id
    toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
    chatFragment = new EaseChatFragment();
    // 传入参数
    chatFragment.setArguments(getIntent().getExtras());

    getSupportFragmentManager().beginTransaction()
        .add(R.id.container, chatFragment).commit();
//    new Handler().postDelayed(new Runnable() {
//      @Override public void run() {
//        chatFragment.setTitleText(toNickName);
//      }
//    }, 100);
  }

  @Override protected void onResume() {
    super.onResume();

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    activityInstance = null;
  }

  @Override
  protected void onNewIntent(Intent intent) {
    // 点击notification bar进入聊天页面，保证只有一个聊天页面
    String username = intent.getStringExtra("userId");
//    if (!username.equals("18034206511")){
//      toNickName = username;
//    }
    if (toChatUsername.equals(username))
      super.onNewIntent(intent);
    else {
      finish();
      startActivity(intent);
    }
  }

  @Override
  public void onBackPressed() {
    chatFragment.onBackPressed();
  }

  public String getToChatUsername() {
    return toChatUsername;
  }

  public static void startChart(Context context, String username) {
    context.startActivity(new Intent(context, ChatActivity.class).putExtra("userId", username));
  }

  public static void startChart(Context context, String name, String userId) {
    Intent intent = new Intent(context, ChatActivity.class);
    intent.putExtra("userId", userId);
    context.startActivity(intent);
  }
}
