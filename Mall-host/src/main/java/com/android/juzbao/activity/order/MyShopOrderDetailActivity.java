
package com.android.juzbao.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.juzbao.activity.AddReviewActivity_;
import com.android.juzbao.activity.ExpressDetailActivity_;
import com.android.juzbao.activity.jifen.OrderRefundActivity_;
import com.android.juzbao.application.BaseApplication;
import com.android.juzbao.constant.ProviderGlobalConst;
import com.android.juzbao.constant.ResultActivity;
import com.android.juzbao.enumerate.OrderStatus;
import com.android.juzbao.model.OrderBusiness;
import com.android.juzbao.model.ProductBusiness;
import com.android.juzbao.model.ProviderOrderBusiness;
import com.android.juzbao.util.CommonValidate;
import com.android.mall.resource.R;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ImageLoaderUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.ShowMsg.IConfirmDialog;
import com.android.zcomponent.util.StringUtil;
import com.android.zcomponent.util.TimeUtil;
import com.android.zcomponent.views.swipebacklayout.lib.app.SwipeBackActivity;
import com.hyphenate.chatui.ChatActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.server.api.model.Area;
import com.server.api.model.City;
import com.server.api.model.CommonReturn;
import com.server.api.model.Order;
import com.server.api.model.OrderDetail;
import com.server.api.model.Product;
import com.server.api.model.Province;
import com.server.api.service.AddressService;
import com.server.api.service.OrderService;
import com.server.api.service.ProductService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * <p>
 * Description: 我的订单详情
 * </p>
 *
 * @ClassName:MyShopOrderDetailActivity
 * @author: wei
 * @date: 2015-11-10
 */
@EActivity(R.layout.activity_my_shop_order_detail)
public class MyShopOrderDetailActivity extends SwipeBackActivity {

  @ViewById(R.id.imgvew_photo_show)
  ImageView mImgvewPhoto;

  @ViewById(R.id.llayout_pay_time_show)
  LinearLayout mLlayoutPayTime;

  @ViewById(R.id.llayout_send_time_show)
  LinearLayout mLlayoutSendTime;

  @ViewById(R.id.llayout_receive_time_show)
  LinearLayout mLlayoutReceiveTime;

  @ViewById(R.id.llayout_order_item)
  LinearLayout mLlayoutOrderItem;

  @ViewById(R.id.rlayout_delivery_info_show_click)
  RelativeLayout mRlayoutDeliveryInfo;

  @ViewById(R.id.tvew_delivery_state_show)
  TextView mTvewDeliveryState;

  @ViewById(R.id.tvew_delivery_time_show)
  TextView mTvewDeliveryTime;

  @ViewById(R.id.tvew_personal_show)
  TextView mTvewPersonal;

  @ViewById(R.id.tvew_phone_show)
  TextView mTvewPhone;

  @ViewById(R.id.tvew_address_show)
  TextView mTvewAddress;

  @ViewById(R.id.tvew_order_name_show)
  TextView mTvewOrderName;

  @ViewById(R.id.tvew_order_time_show)
  TextView mTvewOrderTime;

  @ViewById(R.id.tvew_pay_time_show)
  TextView mTvewPayTime;

  @ViewById(R.id.tvew_send_time_show)
  TextView mTvewSendTime;

  @ViewById(R.id.tvew_receive_time_show)
  TextView mTvewReceiveTime;

  @ViewById(R.id.tvew_order_review_show_click)
  TextView mTvewOrderReview;

  @ViewById(R.id.tvew_order_delivery_show_click)
  TextView mTvewOrderDelivery;

  @ViewById(R.id.tvew_order_refund_show_click)
  TextView mTvewOrderRefund;

  @ViewById(R.id.tvew_order_cancel_show_click)
  TextView mTvewOrderCancel;

  @ViewById(R.id.tvew_order_del_show_click)
  TextView mTvewOrderDel;

  @ViewById(R.id.tvew_order_submit_show_click)
  TextView mTvewOrderSubmit;

  //查看订单
  @ViewById(R.id.tvew_order_complete_show_click)
  TextView mTvewOrderComplete;

  //确认收货
  @ViewById(R.id.tvew_order_receiver_show_click)
  TextView tvewOrderReceiver;

  //等待发货
  @ViewById(R.id.tvew_order_receiving_show_click)
  TextView tvewOrderReceiving;

  @ViewById(R.id.tvew_order_pay_show_click)
  TextView tvewOrderPay;

  @ViewById(R.id.tvew_order_refund_show_click)
  TextView tvewOrderRefund;

  /**
   * 点击确认收货
   */
  @Click(R.id.tvew_order_receiver_show_click)
  void onClicktvewOrderReceiver() {
    if (mOrder != null) {
      OrderService.ReceiveOrderRequest request = new OrderService.ReceiveOrderRequest();
      request.OrderId = mOrder.order_id;
      getHttpDataLoader().doPostProcess(request, CommonReturn.class);
    }
  }

  /**
   * 点击等待发货
   */
  @Click(R.id.tvew_order_receiving_show_click)
  void onClicktvewOrderReceiving() {
    ShowMsg.showToast(this, "请等待卖家发货");
  }

  private Order mOrder;
  /**
   * 完整的收货地址
   */
  private String mFullAddress = "";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @AfterViews
  void initUI() {
    getTitleBar().setTitleText("订单详情");

    Intent intent = getIntent();
    String order = intent.getStringExtra("order");
    String id = intent.getStringExtra("id");

    mOrder = JsonSerializerFactory.Create().decode(order, Order.class);
    if (null != mOrder) {
      id = mOrder.order_id;
      showOrderInfo(mOrder);
      this.queryProvince();
      ProductBusiness.queryProduct(getHttpDataLoader(), Integer.parseInt(mOrder.products[0]
          .product_id));
      OrderBusiness.queryOrderDetail(getHttpDataLoader(), mOrder.order_id);
    } else {
      OrderBusiness.queryOrderDetail(getHttpDataLoader(), mOrder.order_id);
    }
  }

  @Override
  public void onRecvMsg(MessageData msg) throws Exception {
    if (msg.valiateReq(OrderService.CancelOrderRequest.class)) {
      CommonReturn response = msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          ShowMsg.showToast(getApplicationContext(), msg, "订单已取消");
          OrderBusiness.queryOrderDetail(getHttpDataLoader(), mOrder.order_id);
        } else {
          ShowMsg.showToast(getApplicationContext(), response.message);
        }
      } else {
        ShowMsg.showToast(getApplicationContext(), msg, "取消订单失败");
      }
    } else if (msg.valiateReq(OrderService.ReceiveOrderRequest.class)) {
      CommonReturn response = msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          ShowMsg.showToast(getApplicationContext(), msg, "已确认收货");
          OrderBusiness.queryOrderDetail(getHttpDataLoader(), mOrder.order_id);
          finish();
        } else {
          ShowMsg.showToast(getApplicationContext(), response.message);
        }
      } else {
        ShowMsg.showToast(getApplicationContext(), msg, "确认收货失败");
      }
    } else if (msg.valiateReq(OrderService.DelOrderRequest.class)) {
      CommonReturn response = (CommonReturn) msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          ShowMsg.showToast(this, msg, "已成功删除");
          dismissWaitDialog();
          new Handler().postDelayed(new Runnable() {
            @Override public void run() {
              finish();
            }
          }, 200);
          //向Fragment发消息，删除当前Item。
          BaseApplication.getInstance().setActivityResult(ResultActivity.CODE_DEL_ORDER, null);
        } else {
          dismissWaitDialog();
          ShowMsg.showToast(this, response.message);
        }
      } else {
        dismissWaitDialog();
        ShowMsg.showToast(this, msg, "删除失败");
      }
    } else if (msg.valiateReq(OrderService.OrderDetailRequest.class)) {
      OrderDetail response = msg.getRspObject();
      if (CommonValidate.validateQueryState(this, msg, response)) {
        if (null != response) {
          if (null != mOrder && TextUtils.isEmpty(response.Data.status_text)) {
            response.Data.status_text = mOrder.status_text;
          }
          mOrder = response.Data;
          Intent intent = new Intent();
          intent.putExtra("order", JsonSerializerFactory.Create().encode(mOrder));
          BaseApplication.getInstance().setActivityResult(ResultActivity.CODE_UPDATE_ORDER, intent);
          OrderBusiness.showAddressInfo(mOrder, mTvewPersonal, mTvewPhone, mTvewAddress);
          showOrderInfo(mOrder);
        }
      }
    } else if (msg.valiateReq(AddressService.GetProvincesRequest.class)) {
      Province response = (Province) msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          if (null != mOrder) {
            for (int i = 0; i < response.Data.length; i++) {
              if (mOrder.province_id.equals(response.Data[i].province_id)) {
                mFullAddress += response.Data[i].province;
                this.queryCity(mOrder.province_id);
                break;
              }
            }
          }
        }
      }
    } else if (msg.valiateReq(AddressService.GetCitiesRequest.class)) {
      City response = (City) msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          if (null != mOrder) {
            for (int i = 0; i < response.Data.length; i++) {
              if (mOrder.city_id.equals(response.Data[i].city_id)) {
                mFullAddress += response.Data[i].city;
                this.queryAres(mOrder.city_id);
                break;
              }
            }
          }
        }
      }
    } else if (msg.valiateReq(AddressService.GetAreasRequest.class)) {
      Area response = (Area) msg.getRspObject();
      if (null != response) {
        if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
          if (null != mOrder) {
            for (int i = 0; i < response.Data.length; i++) {
              if (mOrder.area_id.equals(response.Data[i].area_id)) {
                mFullAddress += response.Data[i].area;
                this.setAddressInfo();
                break;
              }
            }
          }
        }
      }
    } else if (msg.valiateReq(ProductService.ProductRequest.class)) {
      Product response = (Product) msg.getRspObject();
      if (null != response && response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
        mOrder.im_account = response.Data.im_account;
      }
    }
  }

  private void setAddressInfo() {
    mTvewPersonal.setText("收货人：" + mOrder.consignee);
    mTvewPhone.setText(mOrder.mobile);
    mTvewAddress.setText(mFullAddress + mOrder.address);
  }

  private void queryProvince() {
    AddressService.GetProvincesRequest request = new AddressService.GetProvincesRequest();
    getHttpDataLoader().doPostProcess(request, Province.class);
  }

  private void queryCity(String province_id) {
    AddressService.GetCitiesRequest request = new AddressService.GetCitiesRequest();
    request.ProvinceId = province_id;
    getHttpDataLoader().doPostProcess(request, City.class);
  }

  private void queryAres(String city_id) {
    AddressService.GetAreasRequest request = new AddressService.GetAreasRequest();
    request.CityId = city_id;
    getHttpDataLoader().doPostProcess(request, Area.class);
  }

  /**
   * 显示订单详情信息
   *
   * @param order order
   */
  private void showOrderInfo(Order order) {
    TextView tvewState = (TextView) findViewById(R.id.tvew_order_state);
    if (TextUtils.isEmpty(order.status_text)) {
      tvewState.setText(OrderStatus.getNameById(order.status));
    } else {
      tvewState.setText(order.status_text);
    }

    TextView tvewOrderId = (TextView) findViewById(R.id.tvew_order_id_show);
    TextView tvewOrderPayId = (TextView) findViewById(R.id.tvew_order_pay_id_show);
    TextView tvewAuctionPay = (TextView) findViewById(R.id.tvew_auction_pay_show);
    TextView tvewTotalPay = (TextView) findViewById(R.id.tvew_total_pay_show);
//        tvewAuctionPay.setText("¥" + OrderBusiness.getTotalIdentifyPrice(order.products));
    tvewOrderId.setText(order.order_no);
    if (null != order.products && order.products.length > 0) {
      mTvewOrderName.setText(order.products[0].shop_title);
    }

    if (order.is_score_product != null && "1".equals(order.is_score_product)) {
      tvewTotalPay.setText("积分：" + order.score_num);
      mTvewOrderName.setText("大众积分平台");
    } else {
      tvewTotalPay.setText("合计：" + StringUtil.formatProgress(order.total_price));
    }
    LinearLayout llayoutPayNum = (LinearLayout) findViewById(R.id.llayout_order_pay);
    llayoutPayNum.setVisibility(View.GONE);
    mLlayoutPayTime.setVisibility(View.GONE);
    mLlayoutSendTime.setVisibility(View.GONE);
    mLlayoutReceiveTime.setVisibility(View.GONE);

    mTvewOrderTime.setText(TimeUtil.transformLongTimeFormat(
        Long.parseLong(order.create_time) * 1000,
        TimeUtil.STR_FORMAT_DATE_TIME));

    DisplayImageOptions options = ImageLoaderUtil
        .getDisplayImageOptions(R.drawable.img_empty_logo_small);
    OrderBusiness.showOrderItem(this, mLlayoutOrderItem, order.products,
        ImageLoader.getInstance(), options);

    OrderBusiness.showOrderStateAction(order, tvewOrderReceiving, tvewOrderReceiver,
        mTvewOrderComplete, mTvewOrderSubmit,
        mTvewOrderCancel, mTvewOrderDel, mTvewOrderReview, tvewOrderPay, tvewOrderRefund);

    //设置查询订单Action
    mTvewOrderComplete.setVisibility(View.GONE);

    //如果当前状态 == 完成交易,那么显示追加评价
    if ("交易完成".equals(mOrder.status_text)) {
      TextView moreReviewTv = (TextView) findViewById(R.id.tvew_order_more_review_show_click);
      moreReviewTv.setVisibility(View.VISIBLE);
      moreReviewTv.setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          //追加评价，由于追加评价次数不限制，这里卖家和买家在同一个页面，
          // -> ReadReView.av
//                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("order", JsonSerializerFactory.Create().encode(mOrder));
//                    bundle.putBoolean("isMoreContent", true);
//                    intent.putExtras(bundle);
//                    intent.setClass(MyShopOrderDetailActivity.this, AddReviewActivity_.class);
//                    startActivity(intent);

          Bundle bundle = new Bundle();
          bundle.putString("order", JsonSerializerFactory.Create().encode(mOrder));
          ProviderOrderBusiness.intentToReadReviewActivity(MyShopOrderDetailActivity.this, bundle);
        }
      });
    }
    //订单item点击事件
    for (int i = 0; i < mLlayoutOrderItem.getChildCount(); i++) {
      mLlayoutOrderItem.getChildAt(i).setOnClickListener(new OrderItemClickListener(i));
    }
  }

  private class OrderItemClickListener implements OnClickListener {

    private int position;

    public OrderItemClickListener(int position) {
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      ProductBusiness.intentToProductDetailActivity(
          MyShopOrderDetailActivity.this, null,
          Integer.parseInt(mOrder.products[position].product_id));
    }
  }

  @Click(R.id.rlayout_delivery_info_show_click)
  void onClickRlayoutDeliveryInfo() {

  }

  @Click(R.id.tvew_order_link_click)
  void onClickTvewOrderLink() {
    if (!TextUtils.isEmpty(mOrder.im_account)) {
      ChatActivity.startChart(this, mOrder.im_account);
    } else {
      ShowMsg.showConfirmDialog(this, new IConfirmDialog() {

        @Override
        public void onConfirm(boolean confirmValue) {
          if (confirmValue) {
            ChatActivity.startChart(MyShopOrderDetailActivity.this,
                ProviderGlobalConst.IM_ACCOUNT);
          }
        }
      }, "联系趣那客服", "取消", "该商户暂未设置在线客服，您可以与趣那客服联系，我们将尽快反馈给商户！");
    }
  }

  @Click(R.id.tvew_order_delivery_show_click)
  void onClickTvewOrderDelivery() {
    getIntentHandle().intentToActivity(ExpressDetailActivity_.class);
  }

  @Click(R.id.tvew_order_refund_show_click)
  void onClickTvewOrderRefund() {
    Bundle bundle = new Bundle();
    bundle.putString("order", JsonSerializerFactory.Create().encode(mOrder));
    getIntentHandle().intentToActivity(bundle, OrderRefundActivity_.class);
  }

  @Click(R.id.tvew_order_complete_show_click)
  void onClickTvewOrderSure() {
    OrderBusiness.queryConfirmOrder(getHttpDataLoader(), mOrder.order_id);
    showWaitDialog(2, false, R.string.common_submit_data);
  }

  @Click(R.id.tvew_order_cancel_show_click)
  void onClickTvewOrderCancel() {
    OrderBusiness.queryCancelOrder(getHttpDataLoader(), mOrder.order_id);
    showWaitDialog(2, false, R.string.common_submit_data);
  }

  @Click(R.id.tvew_order_review_show_click)
  void onClickTvewOrderReview() {
    Bundle bundle = new Bundle();
    bundle.putString("order", JsonSerializerFactory.Create().encode(mOrder));
    getIntentHandle().intentToActivity(bundle, AddReviewActivity_.class);
  }

  @Click(R.id.tvew_order_pay_show_click)
  void onClickTvewOrderPay() {
    Bundle bundle = new Bundle();
    bundle.putString("order", JsonSerializerFactory.Create().encode(mOrder));
    getIntentHandle().intentToActivity(bundle, OrderPayActivity_.class);
  }


  @Click(R.id.tvew_order_del_show_click) void onClickTvewOrderDel() {
    OrderBusiness.queryDelOrder(getHttpDataLoader(), mOrder.order_id);
    showWaitDialog(2, false, "正在删除...");
  }

  @UiThread(delay = 3000)
  void queryOrderDetail() {
    OrderBusiness.queryOrderDetail(getHttpDataLoader(), mOrder.order_id);
  }

  @Override
  public void onActivityResultCallBack(int resultCode, Intent intent) {
    if (resultCode == ResultActivity.CODE_REFUND_SUCCESS || resultCode == ResultActivity
        .CODE_ADD_SHOP_REVIEW) {
      OrderBusiness.queryOrderDetail(getHttpDataLoader(), mOrder.order_id);
    } else if (resultCode == ResultActivity.CODE_PAY_ECO_SUCCESS) {
      queryOrderDetail();
    }
    showWaitDialog(1, false, R.string.common_submit_data);
  }

}