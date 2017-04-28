package com.android.juzbao.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.juzbao.model.ProviderShopBusiness;
import com.android.juzbao.provider.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.service.ShopService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


/**
 * <p>
 * Description:  开店协议
 * </p>
 *
 * @ClassName:ProtocolActivity
 * @author: wei
 * @date: 2015-11-11
 */
@EActivity(resName = "activity_protocol")
public class ProtocolActivity extends SwipeBackActivity {
  @ViewById(resName = "wvew_protocol_show")
  TextView mWvewProtocol;

  @ViewById(resName = "btn_openshop_click")
  TextView mBtnOpenShop;

  @ViewById(resName = "check_protocol_click")
  CheckBox mCheckBox;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @AfterViews void initUI() {
    getTitleBar().setTitleText("开店协议");
    ProviderShopBusiness.queryShopProtocol(getHttpDataLoader());
  }

  @Override
  public void onRecvMsg(MessageData msg) throws Exception {
    if (msg.valiateReq(ShopService.ShopProtocolRequest.class)) {
      if (null != msg.getContext().data()) {
        String response = new String(msg.getContext().data());
        mWvewProtocol.setText(response);
      } else {
        ShowMsg.showToast(getApplicationContext(), msg, "查询开店协议失败");
        finish();
      }
    }
  }

  @Click(resName = "btn_openshop_click") void onClickBtnOpenshop() {
    if (mCheckBox.isChecked()) {
      getIntentHandle().intentToActivity(ShopSetActivity_.class);
      finish();
    } else {
      ShowMsg.showToast(this, "请勾选我已阅读并同意用户协议！");
    }
  }

  @CheckedChange(resName = "check_protocol_click") void onCheckProtocol(boolean isChecked) {
    mBtnOpenShop.setEnabled(isChecked);
    if (isChecked) {
      mBtnOpenShop.setTextColor(getResources().getColor(
          R.color.common_btn_text_write_color_selector));
    } else {
      mBtnOpenShop.setTextColor(getResources().getColor(R.color.gray));
    }
  }

}