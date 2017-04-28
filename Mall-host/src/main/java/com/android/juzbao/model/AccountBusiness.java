
package com.android.juzbao.model;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.Config;
import com.android.juzbao.dao.AccountDao;
import com.android.mall.resource.R;
import com.android.zcomponent.common.uiframe.IBaseView;
import com.android.zcomponent.http.HttpDataLoader;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.util.SettingsBase;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.util.encrypt.AES;
import com.android.zcomponent.util.update.AppUpdate;
import com.android.zcomponent.views.CircleImageView;
import com.easemob.easeui.simple.EaseHelper;
import com.server.api.model.CommonReturn;
import com.server.api.model.Gift;
import com.server.api.model.IsSigned;
import com.server.api.model.Statistics;
import com.server.api.model.Version;
import com.server.api.service.AccountService;

public class AccountBusiness {

  public static void queryVersion(HttpDataLoader httpDataLoader) {
    AccountDao.sendCmdQueryVersion(httpDataLoader);
  }

  public static void querySign(HttpDataLoader httpDataLoader) {
    AccountService.SignRequest signRequest =
        new AccountService.SignRequest();
    httpDataLoader.doPostProcess(signRequest,
        CommonReturn.class);
  }

  public static void queryIsSign(HttpDataLoader httpDataLoader) {
    AccountService.IsSignRequest signRequest =
        new AccountService.IsSignRequest();
    httpDataLoader.doPostProcess(signRequest,
        IsSigned.class);
  }

  public static void queryGift(HttpDataLoader httpDataLoader) {
    AccountService.GiftRequest signRequest =
        new AccountService.GiftRequest();
    httpDataLoader.doPostProcess(signRequest,
        Gift.class);
  }

  public static void queryUseGift(HttpDataLoader httpDataLoader, String shopId, String gift,
                                  String quantity) {
    AccountService.UseGiftRequest signRequest =
        new AccountService.UseGiftRequest();
    signRequest.ShopId = shopId;
    signRequest.Gift = gift;
    signRequest.Quantity = quantity;
    httpDataLoader.doPostProcess(signRequest,
        CommonReturn.class);
  }

  public static void queryAppInfo(HttpDataLoader httpDataLoader) {
    AccountDao.sendCmdQueryAppInfo(httpDataLoader);
  }

  public static void showLoginState(TextView tvewAccount, TextView tvewLevel,
                                    CircleImageView imgvewHeadPicture) {
    if (BaseApplication.isLogin()) {
      String mobile = SettingsBase.getInstance().readStringByKey(Config.USER_MOBILE);
      tvewAccount.setText(mobile);
//            tvewLevel.setVisibility(View.VISIBLE);
    } else {
      imgvewHeadPicture.setImageResource(R.drawable.transparent);
      tvewAccount.setText("请登录");
//            tvewLevel.setVisibility(View.GONE);
    }
  }

  public static boolean checkAppUpdate(Version.Data version) {
    if (null == version) {
      return true;
    }
    AppUpdate appUpdate =
        new AppUpdate(BaseApplication.getInstance().getCurActivity());

    return appUpdate.checkUpdateInfo(version.url, version.number,
        version.title, version.description,
        Integer.parseInt(version.mask));
  }

  public static void doLogout() {
    SettingsBase.getInstance().writeStringByKey(Config.USER_ID, "");
    SettingsBase.getInstance().writeStringByKey(Config.USER_PASSWORD, "");
    SettingsBase.getInstance().writeStringByKey(Config.USER_MOBILE, "");
    SettingsBase.getInstance().writeStringByKey(Config.WEIBO_LOGIN, "");
    BaseApplication.getInstance().setLogin(false);
    Endpoint.Cookie = null;
    Endpoint.Token = null;
  }

  public static void doDefaultLogin(HttpDataLoader httpDataLoader) {
    Endpoint.Token = null;

    String user =
        SettingsBase.getInstance().readStringByKey(Config.USER_ID);

    String isWeiboLogin = SettingsBase.getInstance().readStringByKey(Config.WEIBO_LOGIN);

    if ("true".equals(isWeiboLogin)) {
      String type =
          SettingsBase.getInstance()
              .readStringByKey(Config.USER_TYPE);
      AccountDao.sendCmdLoginVister(httpDataLoader, user, type);
    } else {
      String password = "";
      try {
        password = SettingsBase.getInstance().readStringByKey(Config.USER_PASSWORD);
        if (!TextUtils.isEmpty(password)) {
          password = AES.decrypt(password);
        }
      } catch (Exception e1) {
        e1.printStackTrace();
      }

      if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)) {
        try {
          AccountDao.sendCmdLogin(httpDataLoader, user, password);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static void loginEase(String mobile) {
    EaseHelper.getInstance().login(mobile, "123456");
    EaseHelper.getInstance().register(mobile, "123456");
  }

  public static void registerEase(String mobile) {
    EaseHelper.getInstance().register(mobile, "123456");
  }

  public static void loginVister(HttpDataLoader httpDataLoader,
                                 String openId, String type) {
    AccountDao.sendCmdLoginVister(httpDataLoader, openId, type);
  }

  public static boolean queryBindVister(Context context,
                                        HttpDataLoader httpDataLoader, String openId, String type,
                                        String mobile, String verify, String nickname) {
    if (TextUtils.isEmpty(mobile)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.phone_is_empty);
      return false;
    }

    if (!StringUtil.isPhoneNum(mobile)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.phone_num_invalid);
      return false;
    }
    if (TextUtils.isEmpty(verify)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.input_code);
      return false;
    }
    AccountDao.sendCmdBindVisiter(httpDataLoader, openId, type, mobile,
        verify, nickname);

    return true;
  }

  public static boolean login(IBaseView view, HttpDataLoader httpDataLoader,
                              String user, String password) {
    if (TextUtils.isEmpty(user)) {
      view.showToast(R.string.phone_is_empty);
      return false;
    }

    if (!StringUtil.isPhoneNum(user)) {
      view.showToast(R.string.phone_num_invalid);
      return false;
    }

    if (TextUtils.isEmpty(password)) {
      view.showToast(R.string.pwd_not_input);
      return false;
    }
    AccountDao.sendCmdLogin(httpDataLoader, user, password);
    return true;
  }

  public static boolean register(Context context,
                                 HttpDataLoader httpDataLoader, String user, String password,
                                 String conformPassword, String verify, String referrer) {
    if (TextUtils.isEmpty(user)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.phone_is_empty);
      return false;
    }

    if (!StringUtil.isPhoneNum(user)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.phone_num_invalid);
      return false;
    }

    if (TextUtils.isEmpty(password)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.pwd_not_input);
      return false;
    }

    if (TextUtils.isEmpty(conformPassword)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.pwd_not_input_confirm);
      return false;
    }

    if (!conformPassword.equals(password)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.pwd_not_same);
      return false;
    }

    if (TextUtils.isEmpty(verify)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.input_code);
      return false;
    }

    AccountDao.sendCmdRegister(httpDataLoader, user, password, verify, referrer);

    return true;
  }

  public static boolean resetPassword(Context context,
                                      HttpDataLoader httpDataLoader, String user, String password,
                                      String conformPassword, String verify) {
    if (TextUtils.isEmpty(user)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.phone_is_empty);
      return false;
    }

    if (!StringUtil.isPhoneNum(user)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.phone_num_invalid);
      return false;
    }

    if (TextUtils.isEmpty(password)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.pwd_not_input);
      return false;
    }

    if (TextUtils.isEmpty(conformPassword)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.pwd_not_input_confirm);
      return false;
    }

    if (!conformPassword.equals(password)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.pwd_not_same);
      return false;
    }

    if (TextUtils.isEmpty(verify)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.input_code);
      return false;
    }

    AccountDao.sendCmdResetPassword(httpDataLoader, user, password, verify);
    return true;
  }

  public static boolean editPassword(Context context,
                                     HttpDataLoader httpDataLoader, String passwordOld,
                                     String passwordNew, String passwordNewConfirm) {
    if (TextUtils.isEmpty(passwordOld)) {
      ShowMsg.showToast(context.getApplicationContext(), "请输入原密码");
      return false;
    }

    if (TextUtils.isEmpty(passwordNew)) {
      ShowMsg.showToast(context.getApplicationContext(), "请输入新密码");
      return false;
    }

    if (TextUtils.isEmpty(passwordNewConfirm)) {
      ShowMsg.showToast(context.getApplicationContext(), "请再输入一次新密码");
      return false;
    }

    if (!passwordNew.equals(passwordNewConfirm)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.pwd_not_same);
      return false;
    }

    AccountDao.sendCmdEditPassword(httpDataLoader, passwordOld, passwordNew,
        passwordNewConfirm);
    return true;
  }

  public static boolean getVerifyCode(Context context,
                                      HttpDataLoader httpDataLoader, String user, String type) {
    if (TextUtils.isEmpty(user)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.phone_is_empty);
      return false;
    }

    if (!StringUtil.isPhoneNum(user)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.phone_num_invalid);
      return false;
    }

    AccountDao.sendCmdGetCaptcha(httpDataLoader, user, type);
    return true;
  }

  public static void editNickName(HttpDataLoader httpDataLoader,
                                  String nickname) {
    AccountDao.sendCmdEditNickName(httpDataLoader, nickname);
  }

  public static void queryBaseInfo(HttpDataLoader httpDataLoader) {
    AccountDao.sendCmdQueryBaseInfo(httpDataLoader);
  }

  public static void getNickName(HttpDataLoader httpDataLoader) {
    AccountDao.sendCmdGetNickName(httpDataLoader);
  }

  public static void editSex(HttpDataLoader httpDataLoader, int sex) {
    AccountDao.sendCmdEditSex(httpDataLoader, sex);
  }

  public static void getSex(HttpDataLoader httpDataLoader) {
    AccountDao.sendCmdGetSex(httpDataLoader);
  }

  public static void editHead(HttpDataLoader httpDataLoader, String id) {
    AccountDao.sendCmdModifyHead(httpDataLoader, id);
  }

  public static void getHead(HttpDataLoader httpDataLoader) {
    AccountDao.sendCmdGetHead(httpDataLoader);
  }

  public static boolean editMobile(Context context,
                                   HttpDataLoader httpDataLoader, String mobile, String verify) {
    if (TextUtils.isEmpty(mobile)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.phone_is_empty);
      return false;
    }

    if (!StringUtil.isPhoneNum(mobile)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.phone_num_invalid);
      return false;
    }

    if (TextUtils.isEmpty(verify)) {
      ShowMsg.showToast(context.getApplicationContext(),
          R.string.input_code);
      return false;
    }

    AccountDao.sendCmdEditMobile(httpDataLoader, mobile, verify);

    return true;
  }

  public static void getMobile(HttpDataLoader httpDataLoader) {
    AccountDao.sendCmdGetMobile(httpDataLoader);
  }

  public static void statisticsCount(HttpDataLoader httpDataLoader) {
    AccountDao.sendCmdStatistics(httpDataLoader);
  }


  public static void showStatisticsCount(View view, Statistics.Data statistics) {
    if (null == view) {
      return;
    }
    TextView mTvewFavoriteProduct =
        (TextView) view.findViewById(R.id.tvew_favorite_product_show);

    TextView mTvewFavoriteShop =
        (TextView) view.findViewById(R.id.tvew_favorite_shop_show);

    TextView mTvewFootprint =
        (TextView) view.findViewById(R.id.tvew_footprint_show);
//        TextView mTvewPayTip =
//                (TextView) view.findViewById(R.id.tvew_pay_tip_show);
//
//        TextView mTvewDeliveryTip =
//                (TextView) view.findViewById(R.id.tvew_delivery_tip_show);
//
//        TextView mTvewReceiptTip =
//                (TextView) view.findViewById(R.id.tvew_receipt_tip_show);
//
//        TextView mTvewReviewTip =
//                (TextView) view.findViewById(R.id.tvew_review_tip_show);
//
//        TextView mTvewRefundTip =
//                (TextView) view.findViewById(R.id.tvew_refund_tip_show);
    if (null == statistics) {
      mTvewFavoriteProduct.setText("0");
      mTvewFavoriteShop.setText("0");
      mTvewFootprint.setText("0");
//            mTvewPayTip.setVisibility(View.GONE);
//            mTvewDeliveryTip.setVisibility(View.GONE);
//            mTvewReceiptTip.setVisibility(View.GONE);
//            mTvewReviewTip.setVisibility(View.GONE);
//            mTvewRefundTip.setVisibility(View.GONE);
      return;
    }
    showCollectCount(mTvewFavoriteProduct, statistics.collect_product);
    showCollectCount(mTvewFavoriteShop, statistics.collect_shop);
    showCollectCount(mTvewFootprint, statistics.collect_browse);

//        showOrderCount(mTvewPayTip, statistics.order_no_pay);
//        showOrderCount(mTvewDeliveryTip, statistics.order_no_shipments);
//        showOrderCount(mTvewReceiptTip, statistics.order_no_receipt);
//        showOrderCount(mTvewReviewTip, statistics.order_no_comment);
//        showOrderCount(mTvewRefundTip, statistics.order_goods_return);
  }

  private static void showCollectCount(TextView textView, String count) {
    if (null == textView) {
      return;
    }
    if (!TextUtils.isEmpty(count) && !"0".equals(count)) {
      textView.setText(count);
    } else {
      textView.setText("0");
    }
  }

  private static void showOrderCount(TextView textView, String count) {
    if (null == textView) {
      return;
    }

    if (!TextUtils.isEmpty(count) && !"0".equals(count)) {
      textView.setVisibility(View.VISIBLE);
      textView.setText(count);
    } else {
      textView.setVisibility(View.GONE);
    }
  }
}
