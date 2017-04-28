
package com.android.juzbao.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.android.juzbao.constant.ProviderGlobalConst;
import com.android.juzbao.dao.JifenDao;
import com.android.juzbao.model.ProviderShopBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.easemob.easeui.simple.EaseHelper;
import com.hyphenate.chatui.ChatActivity;
import com.server.api.model.OnlineService;
import com.server.api.model.jifenmodel.ServicePhoneResult;
import com.server.api.service.JiFenService;
import com.server.api.service.ShopService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 服务大厅
 * </p>
 *
 * @ClassName:ServeHallActivity
 * @author: wei
 * @date: 2015-11-11
 */
@EActivity(resName = "activity_serve_hall")
public class ServeHallActivity extends SwipeBackActivity {

  @ViewById(resName = "tvew_serve_hall_phone_show")
  TextView mTvewServeHallPhone;

  private OnlineService mOnlineService;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @AfterViews void initUI() {
    getTitleBar().setTitleText("服务大厅");

    String strPhone = getIntent().getStringExtra("phone");
    JifenDao.sendQueryServicePhone(getHttpDataLoader());
    if (!TextUtils.isEmpty(strPhone)) {
      mOnlineService = JsonSerializerFactory.Create().decode(strPhone, OnlineService.class);
    } else {
      ProviderShopBusiness.queryShopOnlineSerivce(getHttpDataLoader());
      showWaitDialog(1, false, "正在查询信息...");
    }
  }

  @Override
  public void onRecvMsg(MessageData msg) throws Exception {
    if (msg.valiateReq(ShopService.OnlineServiceRequest.class)) {
      mOnlineService = msg.getRspObject();
      if (CommonValidate.validateQueryState(this, msg, mOnlineService)) {
        mTvewServeHallPhone.setText(mOnlineService.data.phone);
      }
    } else if (msg.valiateReq(JiFenService.jifenServicePhoneRequest.class)) {
      ServicePhoneResult result = msg.getRspObject();
      if (result.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
        mTvewServeHallPhone.setText(result.data);
        mTvewServeHallPhone.setText("15689788825");
      }
    }
  }

  @Click(resName = "llayout_serve_hall_online_click")
  void onClickLlayoutServeHallOnline() {
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

  @Click(resName = "llayout_serve_hall_call_click")
  void onClickLlayoutServeHallCall() {
    if (!TextUtils.isEmpty(mTvewServeHallPhone.getText().toString())) {
      ShowMsg.showCallDialog(this, mTvewServeHallPhone.getText().toString());
    } else {
      ProviderShopBusiness.queryShopOnlineSerivce(getHttpDataLoader());
    }
  }
}