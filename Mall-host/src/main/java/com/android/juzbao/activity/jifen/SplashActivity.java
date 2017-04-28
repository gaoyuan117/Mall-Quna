package com.android.juzbao.activity.jifen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.android.juzbao.activity.TabHostActivity;
import com.android.juzbao.constant.Config;
import com.android.juzbao.dao.jifendao.JiFenDao;
import com.android.quna.activity.R;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.NetSateUtil;
import com.android.zcomponent.util.SharedPreferencesUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.server.api.model.jifenmodel.StartAd;
import com.server.api.service.JiFenService;

public class SplashActivity extends BaseActivity {

  private static final int CODE_MSG_TIEMOUT = 0X100;
  private static final int CODE_MSG_QUERY_AD = 0X101;
  private static final int CODE_MSG_QUERY_NULL = 0X110;

  private static final int DEFAULT_JUMP_TIME = 1000;

  private ImageView mSplashImgvew;

  private Handler mHandler = new Handler(new Handler.Callback() {
    @Override public boolean handleMessage(Message msg) {
      switch (msg.what) {
        case CODE_MSG_TIEMOUT:
          SplashActivity.this.jumpNextPage();
          break;
        case CODE_MSG_QUERY_AD:
          SplashActivity.this.jumpAdPage((StartAd.Data) msg.obj);
          break;
        case CODE_MSG_QUERY_NULL:
          SplashActivity.this.jumpNextPage();
          break;
      }
      return true;
    }
  });


  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    mSplashImgvew = (ImageView) findViewById(R.id.imgvew_splash);
    showViewAnim();
    if (!SharedPreferencesUtil.getBoolean(Config.IS_SHOW_GUIDE, false)) {
      mHandler.postDelayed(new Runnable() {
        @Override public void run() {
          startActivity(new Intent(SplashActivity.this, GuideActivity.class));
          finish();
        }
      }, DEFAULT_JUMP_TIME);
    } else {
      if (NetSateUtil.isNetworkAvailable(this)) {
        JiFenDao.sendGetStartAd(getHttpDataLoader());
      } else {
        mHandler.postDelayed(new Runnable() {
          @Override public void run() {
            startActivity(new Intent(SplashActivity.this, TabHostActivity.class));
            finish();
          }
        }, DEFAULT_JUMP_TIME);
      }
    }
  }

  private void showViewAnim() {

  }

  @Override public void onRecvMsg(MessageData msg) throws Exception {
    if (msg.valiateReq(JiFenService.GetStartAdRequest.class)) {
      StartAd response = msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS && response.data != null &&
            response.data.status == 1) {
          mHandler.removeMessages(CODE_MSG_TIEMOUT);
          Message message = Message.obtain();
          message.what = CODE_MSG_QUERY_AD;
          message.obj = response.data;
          mHandler.sendMessageDelayed(message, DEFAULT_JUMP_TIME - 800);
        } else {
          mHandler.sendEmptyMessage(CODE_MSG_QUERY_NULL);
        }
      } else {
        mHandler.sendEmptyMessage(CODE_MSG_QUERY_NULL);
      }
    }
  }

  private void jumpNextPage() {
    boolean isShowGuide;
    isShowGuide = SharedPreferencesUtil.getBoolean(Config.IS_SHOW_GUIDE, false);
    if (isShowGuide) {
      startActivity(new Intent(this, TabHostActivity.class));
    } else {
      startActivity(new Intent(this, GuideActivity.class));
    }
    finish();
  }

  private void jumpAdPage(final StartAd.Data startAd) {

    ImageLoader.getInstance().loadImage(Endpoint.HOST + startAd.logo, new ImageLoadingListener() {
      @Override public void onLoadingStarted(String s, View view) {
      }

      @Override public void onLoadingFailed(String s, View view, FailReason failReason) {
        jumpNextPage();
      }

      @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
        goToAdPage(startAd, bitmap);
      }

      @Override public void onLoadingCancelled(String s, View view) {
        jumpNextPage();
      }
    });
  }

  private void goToAdPage(StartAd.Data startAd, Bitmap logo) {
    Intent intent = new Intent(this, StartAdActivity_.class);
    Bundle bundle = new Bundle();
    bundle.putString("ad", JsonSerializerFactory.Create().encode(startAd));
//    bundle.putParcelable("logo", logo);
    intent.putExtras(bundle);
    startActivity(intent);
    finish();
  }

  @Override public boolean onKeyBack(int iKeyCode, KeyEvent event) {
    return true;
  }
}
