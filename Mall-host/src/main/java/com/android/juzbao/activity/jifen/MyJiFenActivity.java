package com.android.juzbao.activity.jifen;

import android.widget.Button;
import android.widget.TextView;

import com.android.juzbao.dao.jifendao.JiFenDao;
import com.android.quna.activity.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.jifenmodel.UserScore;
import com.server.api.service.JiFenService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_my_jifen)
public class MyJiFenActivity extends SwipeBackActivity {

  @ViewById(R.id.tvew_jifen_count)
  TextView mJifenCountTv;

  @ViewById(R.id.btn_jifen_convert_click)
  Button mJifenPayBtn;

  @AfterViews
  void initUI() {
    getTitleBar().setTitleText("我的积分");
    JiFenDao.sendJiFenGetUserScoreRequest(getHttpDataLoader());
  }

  @Override public void onRecvMsg(MessageData msg) throws Exception {
    if (msg.valiateReq(JiFenService.JifenGetUserScoreRequset.class)) {
      UserScore response = msg.getRspObject();
      if (response != null) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          mJifenCountTv.setText(response.data.score);
        }
      }
    }
  }

  @Click(R.id.btn_jifen_convert_click)
  void onClickJifenPayBtn() {
    getIntentHandle().intentToActivity(MyJifenSendPayActivity_.class);
  }

  @Override protected void onRestart() {
    super.onRestart();
    JiFenDao.sendJiFenGetUserScoreRequest(getHttpDataLoader());
  }
}
