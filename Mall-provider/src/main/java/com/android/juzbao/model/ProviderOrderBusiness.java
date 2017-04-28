
package com.android.juzbao.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.juzbao.activity.ReadReviewActivity_;
import com.android.juzbao.constant.ProviderResultActivity;
import com.android.juzbao.dao.ProviderOrder;
import com.android.juzbao.enumerate.ProviderOrderStatus;
import com.android.juzbao.provider.R;
import com.android.zcomponent.common.uiframe.BaseActivity;
import com.android.zcomponent.common.uiframe.FramewrokApplication;
import com.android.zcomponent.http.HttpDataLoader;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.http.api.model.MessageData;
import com.android.zcomponent.http.constant.ErrorCode;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.server.api.model.CommonReturn;
import com.server.api.model.Order;
import com.server.api.model.OrderItem;
import com.server.api.service.OrderService;

import java.math.BigDecimal;

/**
 * 商家订单业务
 */
public class ProviderOrderBusiness {

    /**
     * 查询商家订单
     */
    public static void queryProviderOrders(HttpDataLoader httpDataLoader, int page,
                                           ProviderOrderStatus orderStatus) {

        String status = null;

        if (orderStatus == ProviderOrderStatus.ALL) {
            //查询所有订单
            ProviderOrder.sendCmdQueryQueryAllProviderOrder(httpDataLoader, page);
            return;
        } else if (orderStatus == ProviderOrderStatus.PAY) {
            status = orderStatus.getValue();
        } else if (orderStatus == ProviderOrderStatus.DELIVERY) {
            status = orderStatus.getValue();
        } else if (orderStatus == ProviderOrderStatus.RECEIPT1) {
            status = orderStatus.getValue();
        } else if (orderStatus == ProviderOrderStatus.RECEIPT2) {
            status = orderStatus.getValue();
        } else if (orderStatus == ProviderOrderStatus.RECEIPT3) {
            status = orderStatus.getValue();
        } else if (orderStatus == ProviderOrderStatus.ENSURE) {
            status = orderStatus.getValue();
        } else if (orderStatus == ProviderOrderStatus.REFUND) {
            status = orderStatus.getValue();
        } else if (orderStatus.getValue().equals("7")) {
            status = "7";
        } else if (orderStatus.getValue().equals("8")) {
            status = "8";
        }
        ProviderOrder.sendCmdQueryQueryProviderOrder(httpDataLoader, page, status);
    }

    public static void queryProviderOrderDetail(HttpDataLoader httpDataLoader,
                                                String orderId) {
        ProviderOrder.sendCmdQueryProviderOrderDetail(httpDataLoader, orderId);
    }

    public static void queryProviderOrderSh(HttpDataLoader httpDataLoader,
                                            String orderId, String shippingCode, String code,
                                            String[] coverIds) {

        ProviderOrder.sendCmdProviderOrderShippment(httpDataLoader, orderId,
                shippingCode, code, coverIds);
    }

    public static void queryProviderOrderRefund(HttpDataLoader httpDataLoader,
                                                String orderId, String productId) {
        ProviderOrder.sendCmdProviderOrderRefund(httpDataLoader, orderId,
                productId);
    }

    public static void queryProviderOrderRefuseRefund(
            HttpDataLoader httpDataLoader, String orderId, String productId) {
        ProviderOrder.sendCmdProviderOrderRefuseRefund(httpDataLoader, orderId,
                productId);
    }

    public static void showAddressInfo(
            com.server.api.model.Order order,
            TextView tvewPersonal, TextView tvewPhone, TextView tvewAddress) {
        if (null == order) {
            return;
        }
        tvewPersonal.setText("收货人：" + order.consignee);
        tvewPhone.setText(order.mobile);
        tvewAddress.setText(order.address);
    }

    public static BigDecimal getTotalIdentifyPrice(OrderItem[] products) {
        BigDecimal identifyPrice = new BigDecimal(0);
        if (null == products) {
            return identifyPrice;
        }
        for (int i = 0; i < products.length; i++) {
            if (null != products[i].identify_price) {
                identifyPrice.add(products[i].identify_price);
            }
        }

        return StringUtil.formatProgress(identifyPrice);
    }

    public static void showOrderItem(Context context,
                                     LinearLayout orderItemParent, OrderItem[] orderItems,
                                     ImageLoader imageLoader, DisplayImageOptions options) {
        if (null == orderItems) {
            return;
        }
        orderItemParent.removeAllViews();
        for (int i = 0; i < orderItems.length; i++) {
            View view =
                    LayoutInflater.from(context).inflate(
                            R.layout.layout_shop_order_item, null);
            OrderItem orderItem = orderItems[i];
            ImageView imgvewPhoto =
                    (ImageView) view.findViewById(R.id.imgvew_photo_show);
            if (!TextUtils.isEmpty(orderItem.image_path)) {
                imageLoader.displayImage(Endpoint.HOST + orderItem.image_path,
                        imgvewPhoto, options);
            } else {
                if (!TextUtils.isEmpty(orderItem.image)) {
                    imageLoader.displayImage(Endpoint.HOST + orderItem.image,
                            imgvewPhoto, options);
                }
            }

            TextView tvewProductName =
                    (TextView) view.findViewById(R.id.tvew_product_name_show);
            TextView tvewProductAttr =
                    (TextView) view.findViewById(R.id.tvew_product_attr_show);
            TextView tvewProductNowPrice =
                    (TextView) view
                            .findViewById(R.id.tvew_product_now_price_show);
            TextView tvewProductPrice =
                    (TextView) view.findViewById(R.id.tvew_product_price_show);
            TextView tvewProductNum =
                    (TextView) view.findViewById(R.id.tvew_product_num_show);
            TextView tvewLine = (TextView) view.findViewById(R.id.tvew_line);
            if (i == orderItems.length - 1) {
                tvewLine.setVisibility(View.GONE);
            }
            tvewProductName.setText(orderItem.product_title);
            if (StringUtil.formatProgress(orderItem.price).doubleValue() > 0) {
                tvewProductNowPrice.setText("¥"
                        + StringUtil.formatProgress(orderItem.price));
            } else {
                tvewProductNowPrice.setText("¥"
                        + StringUtil.formatProgress(orderItem.unit_price));
            }
            tvewProductNum.setText("x" + orderItem.quantity);
            tvewProductAttr.setText(orderItem.product_attr);
            orderItemParent.addView(view);
        }
    }

    public static void showProviderOrderStateAction(Order order,
                                                    TextView tvewOrderPay,
                                                    TextView tvewOrderSend,
                                                    TextView tvewOrderReceiving,
                                                    TextView tvewOrderReceived,
                                                    TextView tvewOrderRefund,
                                                    TextView tvewOrderRefuseRefund,
                                                    TextView tvewOrderDelete,
                                                    TextView tvewOrderDetail,
                                                    TextView tvewOrderReview) {
        tvewOrderPay.setVisibility(View.GONE);
        tvewOrderSend.setVisibility(View.GONE);
        tvewOrderReceiving.setVisibility(View.GONE);
        tvewOrderReceived.setVisibility(View.GONE);
        tvewOrderRefund.setVisibility(View.GONE);
        tvewOrderRefuseRefund.setVisibility(View.GONE);
        tvewOrderDelete.setVisibility(View.GONE);
        tvewOrderReview.setVisibility(View.GONE);
        tvewOrderDetail.setVisibility(View.GONE);

        if ("待付款".equals(order.status_text)) {
            tvewOrderPay.setVisibility(View.VISIBLE);
        } else if ("待发货".equals(order.status_text)) {
            tvewOrderSend.setVisibility(View.VISIBLE);
        } else if ("待收货".equals(order.status_text)) {
            tvewOrderReceiving.setVisibility(View.VISIBLE);
        } else if ("待评价".equals(order.status_text)) {
            tvewOrderReceived.setVisibility(View.VISIBLE);
        } else if ("交易完成".equals(order.status_text)) {
            tvewOrderDelete.setVisibility(View.VISIBLE);
            tvewOrderReview.setVisibility(View.VISIBLE);
        } else if ("交易取消".equals(order.status_text)) {
            tvewOrderDelete.setVisibility(View.VISIBLE);
        } else if ("申请退款".equals(order.status_text)) {
            tvewOrderRefund.setVisibility(View.VISIBLE);
            tvewOrderDetail.setVisibility(View.VISIBLE);
        } else if ("已退款".equals(order.status_text)) {
            tvewOrderDelete.setVisibility(View.VISIBLE);
        }
    }

    public static void updateOrderList(Order order) {
        Intent intent = new Intent();
        intent.putExtra("order", JsonSerializerFactory.Create().encode(order));
        FramewrokApplication.getInstance().setActivityResult(ProviderResultActivity
                .CODE_UPDATE_ORDER, intent);
    }

    /**
     * 查看物流详情
     *
     * @param context context
     * @param orderId orderId
     * @param image   image
     */
    @Deprecated
    public static void intentToExpressDetailActivity(Context context, String orderId, String
            image) {
        ComponentName cn =
                new ComponentName("com.android.juzbao.activity",
                        "com.android.juzbao.activity.ExpressDetailActivity_");
        Bundle bundle = new Bundle();
        bundle.putString("id", orderId);
        bundle.putString("image", image);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setComponent(cn);
        context.startActivity(intent);
    }

    /**
     * 跳转到提交物流
     *
     * @param context context
     * @param orderId orderId
     */
    @Deprecated
    public static void intentToSubmitExpressActivity(Context context, String orderId) {
        Bundle bundle = new Bundle();
        bundle.putString("id", orderId);
        ComponentName cn = new ComponentName
                ("com.android.juzbao.activity", "com.android.juzbao.activity" +
                        ".SubmitExpressActivity_");
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setComponent(cn);
        context.startActivity(intent);
    }

    /**
     * 跳转到查看评价页面
     */
    public static void intentToReadReviewActivity(Context context, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(context, ReadReviewActivity_.class);
        context.startActivity(intent);
    }

    public static class OrderHelper {

        private BaseActivity mContext;

        private HttpDataLoader mHttpDataLoader;

        private String mOrderId;

        public OrderHelper(Context context, HttpDataLoader httpDataLoader,
                           String orderSn) {
            mHttpDataLoader = httpDataLoader;
            mContext = (BaseActivity) context;
            mOrderId = orderSn;
        }

        public OrderHelper(Context context, HttpDataLoader httpDataLoader) {
            mHttpDataLoader = httpDataLoader;
            mContext = (BaseActivity) context;
        }

        public void setOrderId(String orderSn) {
            mOrderId = orderSn;
        }

        public void onRecvMsg(MessageData msg) throws Exception {
            if (msg.valiateReq(OrderService.RefundOperationRequest.class)) {
                CommonReturn response = msg.getRspObject();
                if (null != response) {
                    if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                        ShowMsg.showToast(mContext, msg, "已同意退款");
                        ProviderOrderBusiness.queryProviderOrderDetail(mHttpDataLoader,
                                mOrderId);
                    } else {
                        mContext.dismissWaitDialog();
                        ShowMsg.showToast(mContext, response.message);
                    }
                } else {
                    mContext.dismissWaitDialog();
                    ShowMsg.showToast(mContext, msg, "同意退款失败");
                }
            } else if (msg.valiateReq(OrderService.RefuseRefundOperationRequest.class)) {
                CommonReturn response = msg.getRspObject();
                if (null != response) {
                    if (response.code == ErrorCode.INT_QUERY_DATA_SUCCESS) {
                        ShowMsg.showToast(mContext, msg, "已拒绝退款");
                        ProviderOrderBusiness.queryProviderOrderDetail(mHttpDataLoader,
                                mOrderId);
                    } else {
                        mContext.dismissWaitDialog();
                        ShowMsg.showToast(mContext, response.message);
                    }
                } else {
                    mContext.dismissWaitDialog();
                    ShowMsg.showToast(mContext, msg, "拒绝退款失败");
                }
            }
        }
    }
}
