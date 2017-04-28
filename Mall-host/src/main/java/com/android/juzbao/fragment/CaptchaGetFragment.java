
package com.android.juzbao.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.server.api.model.VerifyCode;
import com.android.mall.resource.R;
import com.android.juzbao.model.AccountBusiness;
import com.android.zcomponent.common.uiframe.fragment.BaseFragment;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.util.ShowMsg;
import com.server.api.service.AccountService;

public abstract class CaptchaGetFragment extends BaseFragment {

  private OnClickListener mOnClickListener;

  private RelativeLayout mrlayoutCaptcha;

  private Button mbtnGetCaptcha;

  private ProgressBar mProgressBar;

  private int INT_CODE_TIME_OUT = 60 * 3;

  private Handler mHandler = new Handler();

  private TimeRunable mTimeRunable = new TimeRunable();

  private int timeOut = 180;

  private String verify = "";

  public CaptchaGetFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view =
        inflater.inflate(R.layout.fragment_get_captcha, null, false);
    mrlayoutCaptcha = (RelativeLayout) view.findViewById(R.id.rlayout_get_captcha);
    mbtnGetCaptcha = (Button) view.findViewById(R.id.btn_get_captcha);
    mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar_review);
    mrlayoutCaptcha.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (null != mOnClickListener) {
          mOnClickListener.onClick(mbtnGetCaptcha);
        }
      }
    });
    mbtnGetCaptcha.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        if (null != mOnClickListener) {
          mOnClickListener.onClick(mbtnGetCaptcha);
        }
        boolean isSend = AccountBusiness.getVerifyCode(getActivity(), getHttpDataLoader(),
            getMobile(), getCodeType());
        if (isSend) {
          setGetCaptchaBtnClickable(false);
        }
      }
    });
    return view;
  }

  @Override
  public void onRecvMsg(MessageData msg) throws Exception {
    if (msg.valiateReq(AccountService.GetCaptchaRequest.class)) {
      VerifyCode response = (VerifyCode) msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          ShowMsg.showToast(getActivity().getApplicationContext(), R.string.get_code_success);
          doTimeOut();
          EditText editTextCode = getCodeEditText();
          if (null != editTextCode) {
//                        editTextCode.setText(response.data.verify);
            verify = response.data.verify;

          }
        } else {
          setGetCaptchaBtnClickable(true);
          ShowMsg.showToast(getActivity().getApplicationContext(), response.message);
        }
      } else {
        setGetCaptchaBtnClickable(true);
        ShowMsg.showToast(getActivity().getApplicationContext(),
            R.string.get_code_failed);
      }
    }
  }

  public void setGetCaptchaBtnClickable(boolean clickable) {
    if (null == getActivity()) {
      return;
    }

    mbtnGetCaptcha.setClickable(clickable);
    mrlayoutCaptcha.setClickable(clickable);
    if (clickable) {
      mProgressBar.setVisibility(View.GONE);
      mbtnGetCaptcha.setText("获取验证码");
      mHandler.removeCallbacks(mTimeRunable);
    } else {
      mProgressBar.setVisibility(View.VISIBLE);
    }
  }

  private void doTimeOut() {
    setGetCaptchaBtnClickable(false);
    mProgressBar.setVisibility(View.GONE);
    timeOut = INT_CODE_TIME_OUT;
    mHandler.post(mTimeRunable);
  }

  @Override
  public void onDestroy() {
    if (null != mHandler && null != mTimeRunable) {
      mHandler.removeCallbacks(mTimeRunable);
    }
    super.onDestroy();
  }

  private class TimeRunable implements Runnable {

    @Override
    public void run() {
      timeOut--;
      mbtnGetCaptcha.setText("重新获取(" + timeOut + ")");
      if (timeOut != 0) {
        mHandler.postDelayed(mTimeRunable, 1000);
      } else {
        setGetCaptchaBtnClickable(true);
      }
    }
  }

  public void setOnClickListener(OnClickListener onClickListener) {
    mOnClickListener = onClickListener;
  }

  public abstract String getCodeType();

  public abstract String getMobile();

  public abstract EditText getCodeEditText();
}
