
package com.android.juzbao.activity.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.dao.jifendao.JiFenDao;
import com.android.juzbao.model.OrderBusiness;
import com.android.juzbao.model.alipay.AliPay;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.server.api.model.Order;
import com.server.api.model.OrderDetail;
import com.server.api.model.jifenmodel.JifenPayOrderReturn;
import com.server.api.model.jifenmodel.OrderMergeReturn;
import com.server.api.service.JiFenService;
import com.server.api.service.OrderService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 支付方式选择
 * </p>
 *
 * @ClassName:OrderPayActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_order_pay)
public class OrderPayActivity extends SwipeBackActivity {

  /**
   * 开发者需要填一个服务端URL 该URL是用来请求支付需要的charge。务必确保，URL能返回json格式的charge对象。
   * 服务端生成charge 的方式可以参考ping++官方文档，地址
   * https://pingxx.com/guidance/server/import
   * <p>
   * 【 http://218.244.151.190/demo/charge 】是 ping++ 为了方便开发者体验 sdk 而提供的一个临时 url
   * 。 该 url 仅能调用【模拟支付控件】，开发者需要改为自己服务端的 url 。
   */
//    private static String YOUR_URL = "http://218.244.151.190/demo/charge";
//
//    public static final String URL = YOUR_URL;

  private static final int REQUEST_CODE_PAYMENT = 1;

  private static final String CHANNEL_LIMIT = "limit";
  /**
   * 银联支付渠道
   */
  private static final String CHANNEL_UPACP = "upacp";
  /**
   * 微信支付渠道
   */
  private static final String CHANNEL_WECHAT = "wx";
  /**
   * 支付宝支付渠道
   */
  private static final String CHANNEL_ALIPAY = "alipay";
  /**
   * 百度支付渠道
   */
  private static final String CHANNEL_BFB = "bfb";
  /**
   * 京东支付渠道
   */
  private static final String CHANNEL_JDPAY_WAP = "jy_wap";

  @ViewById(R.id.checkbox_pay_limit_show)
  CheckBox mCheckboxPayLimit;

  @ViewById(R.id.checkbox_pay_zhifb_show)
  CheckBox mCheckboxPayZhifb;

  @ViewById(R.id.checkbox_pay_weixin_show)
  CheckBox mCheckboxPayWeixin;

  @ViewById(R.id.checkbox_pay_yinlian_show)
  CheckBox mCheckboxPayYinlian;

  @ViewById(R.id.tvew_amount_show)
  TextView mTvewAmount;

  //可以显示我的积分
  @ViewById(R.id.tvew_wallet_blance_show)
  TextView mTvewBlance;

  @ViewById(R.id.editvew_input_money)
  EditText mEditvewMoney;

  @ViewById(R.id.tvew_settle_show)
  TextView mTvewSettle;

  @ViewById(R.id.rlayout_pay_limit_click)
  LinearLayout mRlayoutLimit;

  private Order mOrder;
  //    private DistinguishItem mDistinguish;
  private int[] orderIds;

  private OrderMergeReturn.Data mergeOrder;

  private String mTotalPrice;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @AfterViews
  void initUI() {
    getTitleBar().setTitleText("选择支付方式");
    mEditvewMoney.addTextChangedListener(new StringUtil.DecimalTextWatcher(mEditvewMoney, 2));
    Intent intent = getIntent();
//        if (getIntentHandle().isIntentFrom(DistinguishEnsureActivity_.class)) {
//            mDistinguish = JsonSerializerFactory.Create().decode(
//                    intent.getStringExtra("order"),
//                    DistinguishItem.class);
//            mTvewAmount.setText("应支付：¥" + StringUtil.formatProgress(mDistinguish.total));
//        } else if (getIntentHandle().isIntentFrom(MyWalletActivity_.class)) {
//            getTitleBar().setTitleText("充值");
//            mTvewAmount.setVisibility(View.GONE);
//            mEditvewMoney.setVisibility(View.VISIBLE);
//            mRlayoutLimit.setVisibility(View.GONE);
//        } else {
    mOrder = JsonSerializerFactory.Create().decode(intent.getStringExtra("order"), Order.class);
    orderIds = intent.getIntArrayExtra("orderIds");
    mTotalPrice = intent.getStringExtra("price");
    mTvewAmount.setText("应支付：¥" + mTotalPrice);
    if (orderIds != null && orderIds.length > 0) {
      if (orderIds.length == 1) {
        OrderBusiness.queryOrderDetail(getHttpDataLoader(), "" + orderIds[0]);
        getDataEmptyView().showViewWaiting();
      } else {
        JiFenDao.sendOrderMergeOrder(getHttpDataLoader(), orderIds);
        getDataEmptyView().showViewWaiting();
      }
    }
    if (mOrder != null) {
      mTvewAmount.setText("应支付：¥" + StringUtil.formatProgress(mOrder.total_price));
    }
  }

  @Override
  public void onRecvMsg(MessageData msg) throws Exception {
    if (msg.valiateReq(OrderService.OrderDetailRequest.class)) {
      getDataEmptyView().dismiss();
      OrderDetail response = (OrderDetail) msg.getRspObject();
      if (CommonValidate.validateQueryState(this, msg, response)) {
        if (null != response && null != response.Data) {
          mOrder = response.Data;
//          mTvewAmount.setText("应支付：¥" + StringUtil.formatProgress(mOrder.total_price));
        }
      }
    } else if (msg.valiateReq(JiFenService.JifenOrderMergeRequest.class)) {
      getDataEmptyView().dismiss();
      OrderMergeReturn response = msg.getRspObject();
      if (CommonValidate.validateQueryState(this, msg, response)) {
        if (null != response && null != response.data) {
          mergeOrder = response.data;
          mTvewAmount.setText("应支付：￥" + mergeOrder.price);
        }
      }
    } else if (msg.valiateReq(JiFenService.JifenPayProductRequest.class)) {
      getDataEmptyView().dismiss();
      JifenPayOrderReturn response = msg.getRspObject();
      if (response != null) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          getIntentHandle().intentToActivity(OrderSuccessActivity_.class);
          finish();
        } else {
          showToast(response.message);
        }
      } else {
        showToast("积分不足");
      }
    }

//        if (msg.valiateReq(PayService.PayOrderRequest.class)) {
//            Payment response = (Payment) msg.getRspObject();

//              if (CommonValidate.validateQueryState(this, msg, response)) {
//                if (!TextUtils.isEmpty(response.Data)) {
//                    String data = response.Data;
//                    Intent intent =new Intent(OrderPayActivity.this, PaymentActivity.class);
//                    intent.putExtra(PaymentActivity.EXTRA_CHARGE, data);
//                    startActivityForResult(intent, REQUEST_CODE_PAYMENT);
//                } else {
//                    ShowMsg.showToast(getApplicationContext(), msg, "请求支付失败");
//                }
//
//            } else {
//                ShowMsg.showToast(getApplicationContext(), msg, "请求支付失败");
//            }
//        }
  }

  @Click(R.id.rlayout_pay_limit_click)
  void onClickRlayoutPayLimit() {
    initCheckBox();
    mCheckboxPayLimit.setChecked(true);
  }

  @Click(R.id.rlayout_pay_zhifb_click)
  void onClickRlayoutPayZhifb() {
    initCheckBox();
    mCheckboxPayZhifb.setChecked(true);
  }

  @Click(R.id.rlayout_pay_weixin_click)
  void onClickRlayoutPayWeixin() {
    initCheckBox();
    mCheckboxPayWeixin.setChecked(true);
  }

  @Click(R.id.rlayout_pay_yinlian_click)
  void onClickRlayoutPayYinlian() {
    initCheckBox();
    mCheckboxPayYinlian.setChecked(true);
  }

  @CheckedChange({R.id.checkbox_pay_limit_show,
      R.id.checkbox_pay_weixin_show, R.id.checkbox_pay_yinlian_show,
      R.id.checkbox_pay_zhifb_show})
  void onChecked(CompoundButton buttonView, boolean isChecked) {
    if (buttonView == mCheckboxPayLimit) {
      mCheckboxPayZhifb.setChecked(false);
      mCheckboxPayWeixin.setChecked(false);
      mCheckboxPayYinlian.setChecked(false);
    } else if (buttonView == mCheckboxPayZhifb) {
      mCheckboxPayLimit.setChecked(false);
      mCheckboxPayWeixin.setChecked(false);
      mCheckboxPayYinlian.setChecked(false);
    } else if (buttonView == mCheckboxPayWeixin) {
      mCheckboxPayLimit.setChecked(false);
      mCheckboxPayZhifb.setChecked(false);
      mCheckboxPayYinlian.setChecked(false);
    } else if (buttonView == mCheckboxPayYinlian) {
      mCheckboxPayLimit.setChecked(false);
      mCheckboxPayZhifb.setChecked(false);
      mCheckboxPayWeixin.setChecked(false);
    }
  }

  private void initCheckBox() {
    mCheckboxPayLimit.setChecked(false);
    mCheckboxPayZhifb.setChecked(false);
    mCheckboxPayWeixin.setChecked(false);
    mCheckboxPayYinlian.setChecked(false);
  }

  @Click(R.id.tvew_settle_show)
  void onClickTvewPaySubmit() {

    // 支付宝，微信支付，银联，百度钱包 按键的点击响应处理
    String payCode = null;
    if (mCheckboxPayYinlian.isChecked()) {
      payCode = CHANNEL_UPACP;
    } else if (mCheckboxPayZhifb.isChecked()) {
      payCode = CHANNEL_ALIPAY;
    } else if (mCheckboxPayWeixin.isChecked()) {
      payCode = CHANNEL_WECHAT;
    } else if (mCheckboxPayLimit.isChecked()) {
      payCode = CHANNEL_LIMIT;
    } else {
      ShowMsg.showToast(getApplicationContext(), "请选择支付方式");
      return;
    }
//        if (getIntentHandle().isIntentFrom(DistinguishEnsureActivity_.class)) {
//            if (null != mDistinguish) {
//                if (StringUtil.formatProgress(mDistinguish.total).doubleValue() <= 0) {
//                    ShowMsg.showToast(getApplicationContext(), "支付金额需大于零");
//                    return;
//                }
//                if (mCheckboxPayLimit.isChecked()) {
//                    PayBusiness.queryDistinguishBalancePay(getHttpDataLoader(),
//                            mDistinguish.id, mDistinguish.checkup_no,
//                            StringUtil.formatProgress(mDistinguish.total));
//                } else {
//                    PayBusiness.queryDistinguishPay(getHttpDataLoader(),
//                            payCode, mDistinguish.id, mDistinguish.checkup_no,
//                            StringUtil.formatProgress(mDistinguish.total));
//                }
//                showWaitDialog(1, false, R.string.common_submit_data);
//            }
//        } else if (getIntentHandle().isIntentFrom(MyWalletActivity_.class)) {
//            boolean isSend =
//                    PayBusiness.queryPayRecharge(this, getHttpDataLoader(),
//                            payCode, mEditvewMoney.getText().toString());
//            if (isSend) {
//                showWaitDialog(1, false, R.string.common_submit_data);
//            }
//        } else {
    if (null != mOrder || null != mergeOrder) {
      if (null != mOrder) {
        if (StringUtil.formatProgress(mOrder.total_price).doubleValue() <= 0) {
          ShowMsg.showToast(getApplicationContext(), "支付金额需大于零");
          return;
        }
      }

      if (null != mergeOrder) {
        if (Double.parseDouble(mergeOrder.price) <= 0) {
          ShowMsg.showToast(getApplicationContext(), "支付金额需大于零");
          return;
        }
      }

      if (mCheckboxPayLimit.isChecked()) {

        if (mOrder == null && mergeOrder != null) {
          JiFenDao.sendJiFenPayProductRequest(getHttpDataLoader(), mergeOrder.order_no);
        } else if (mOrder != null && mergeOrder == null) {
          JiFenDao.sendJiFenPayProductRequest(getHttpDataLoader(), mOrder.order_id);
        }
      } else if (mCheckboxPayZhifb.isChecked()) {
        AliPay aliPay = new AliPay(OrderPayActivity.this);
        if (mOrder == null && mergeOrder != null) {
          aliPay.payV2(mergeOrder.price, mergeOrder.name, mergeOrder.name,
              mergeOrder.order_no, getAliPayCallBack());
        } else if (mOrder != null && mergeOrder == null) {
          aliPay.payV2("" + mTotalPrice, mOrder.products[0].product_title, mOrder.products[0]
                  .product_title,
              mOrder.order_no, getAliPayCallBack());
        }
      } else if (mCheckboxPayWeixin.isChecked()) {
        // TODO: 2016/8/23 微信支付
      }
//            showWaitDialog(1, false, R.string.common_submit_data);
    } else {
      showToast("正在查询订单信息,请稍等...");
    }
//        }
  }

  private AliPay.AlipayCallBack getAliPayCallBack() {
    return new AliPay.AlipayCallBack() {
      @Override
      public void onSuccess() {
        if (getIntentHandle().isIntentFrom(OrderEnsureActivity_.class)) {
          getIntentHandle().intentToActivity(OrderSuccessActivity_.class);
          finish();
        } else {
          BaseApplication.getInstance().setActivityResult(ResultActivity.CODE_PAY_ECO_SUCCESS,
              null);
          finish();
        }
      }

      @Override
      public void onDeeling() {
        ShowMsg.showToast(OrderPayActivity.this, "支付完成，后台处理中");
      }

      @Override
      public void onCancle() {

      }

      @Override
      public void onFailure(String msg) {
        ShowMsg.showToast(OrderPayActivity.this, msg);
      }
    };
  }

  /**
   * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。 最终支付成功根据异步通知为准
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // 支付页面返回处理
    if (requestCode == REQUEST_CODE_PAYMENT) {
      if (resultCode == Activity.RESULT_OK) {
        String result = data.getExtras().getString("pay_result");
                /*
                 * 处理返回值 "success" - payment succeed "fail" - payment failed
				 * "cancel" - user canceld "invalid" - payment plugin not
				 * installed
				 */
        if (result.equals("success")) {
          ShowMsg.showToast(getApplicationContext(), "支付成功");
          BaseApplication.getInstance().setActivityResult(ResultActivity.CODE_PAY_ECO_SUCCESS,
              null);
        } else if (result.equals("fail")) {
          ShowMsg.showToast(getApplicationContext(), "支付失败");
        } else if (result.equals("cancel")) {
          ShowMsg.showToast(getApplicationContext(), "支付取消");
        }
        finish();
      }
    }
  }
}