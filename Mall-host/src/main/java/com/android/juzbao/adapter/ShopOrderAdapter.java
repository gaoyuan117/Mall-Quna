
package com.android.juzbao.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.juzbao.activity.AddReviewActivity_;
import com.android.juzbao.activity.jifen.OrderRefundActivity_;
import com.android.juzbao.activity.order.MyShopOrderDetailActivity_;
import com.android.juzbao.activity.order.OrderPayActivity_;
import com.android.juzbao.model.OrderBusiness;
import com.android.mall.resource.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.server.api.model.Order;
import com.server.api.model.OrderItem;

import java.util.List;

/**
 * Description: 我的购买订单列表项
 */
public class ShopOrderAdapter extends CommonAdapter {
    public ShopOrderAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView =
                    layoutInflater.inflate(R.layout.adapter_shop_order, null);
        }

        TextView tvewShopName = findViewById(convertView, R.id.tvew_shop_name_show);
        LinearLayout llayoutOrderItem = findViewById(convertView, R.id.llayout_order_item);
        TextView tvewOrderStatus = findViewById(convertView, R.id.tvew_order_status_show);
        TextView tvewOrderPrice = findViewById(convertView, R.id.tvew_total_price_show);

        TextView tvewOrderSubmit = findViewById(convertView, R.id.tvew_order_submit_show_click);
        TextView tvewOrderPay = findViewById(convertView, R.id.tvew_order_pay_show_click);
        TextView tvewOrderRefund = findViewById(convertView, R.id.tvew_order_refund_show_click);
        TextView tvewOrderCancel = findViewById(convertView, R.id.tvew_order_cancel_show_click);
        TextView tvewOrderDel = findViewById(convertView, R.id.tvew_order_del_show_click);
        TextView tvewOrderComplete = findViewById(convertView, R.id.tvew_order_complete_show_click);
        TextView tvewOrderReview = findViewById(convertView, R.id.tvew_order_review_show_click);
        TextView tvewOrderReceiving = findViewById(convertView, R.id
                .tvew_order_receiving_show_click);
        TextView tvewOrderReceiver = findViewById(convertView, R.id.tvew_order_receiver_show_click);

        final Order order = (Order) mList.get(position);

        OrderItem[] orderItems = order.products;
        if (null != orderItems && orderItems.length > 0) {
            tvewShopName.setText(order.products[0].shop_title);
        }

        //显示订单状态
        if (!TextUtils.isEmpty(order.status_text)) {
            tvewOrderStatus.setText(order.status_text);
        } else {
            tvewOrderStatus.setText(order.status);
        }

        if (order.is_score_product != null && "1".equals(order.is_score_product)) {
            tvewOrderPrice.setText("积分：" + order.score_num);
            tvewShopName.setText("大众积分平台");
        } else {
            tvewOrderPrice.setText("合计：" + StringUtil.formatProgress(order.total_price));
        }
        OrderBusiness.showOrderItem(mContext, llayoutOrderItem, orderItems, mImageLoader, options);

        //显示订单的action
        OrderBusiness.showOrderStateAction(order, tvewOrderReceiving, tvewOrderReceiver,
                tvewOrderComplete, tvewOrderSubmit,
                tvewOrderCancel, tvewOrderDel, tvewOrderReview, tvewOrderPay, tvewOrderRefund);

        tvewOrderPay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLeastSingleClick()) {
                    return;
                }
                if (null != face) {
                    face.onClickUpdate(position);
                }
                Bundle bundle = new Bundle();
                bundle.putString("order", JsonSerializerFactory.Create().encode(order));
                intentToActivity(bundle, OrderPayActivity_.class);
            }
        });

        tvewOrderRefund.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLeastSingleClick()) {
                    return;
                }
                if (null != face) {
                    face.onClickUpdate(position);
                }
                Bundle bundle = new Bundle();
                bundle.putString("order", JsonSerializerFactory.Create().encode(order));
                intentToActivity(bundle, OrderRefundActivity_.class);
            }
        });

        tvewOrderSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!CommonUtil.isLeastSingleClick()) {
                    return;
                }

                if (null != face) {
                    face.onClickUpdate(position);
                }
                if (null != face) {
                    face.onClickUpdate(position);
                    face.onClickSubmit(position);
                }

            }
        });

        tvewOrderReview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLeastSingleClick()) {
                    return;
                }
                if (null != face) {
                    face.onClickUpdate(position);
                }
                Bundle bundle = new Bundle();
                bundle.putString("order", JsonSerializerFactory.Create().encode(order));
                intentToActivity(bundle, AddReviewActivity_.class);
            }
        });

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLeastSingleClick()) {
                    return;
                }
                if (null != face) {
                    face.onClickUpdate(position);
                }
                Bundle bundle = new Bundle();
                bundle.putString("order", JsonSerializerFactory.Create().encode(order));
                intentToActivity(bundle, MyShopOrderDetailActivity_.class);
            }
        });

        tvewOrderCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!CommonUtil.isLeastSingleClick()) {
                    return;
                }
                if (null != face) {
                    face.onClickCancel(position);
                    face.onClickUpdate(position);
                }
            }
        });

        tvewOrderComplete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!CommonUtil.isLeastSingleClick()) {
                    return;
                }
                if (null != face) {
                    face.onClickUpdate(position);
                }
                Bundle bundle = new Bundle();
                bundle.putString("order", JsonSerializerFactory.Create().encode(order));
                intentToActivity(bundle, MyShopOrderDetailActivity_.class);
            }
        });

        tvewOrderDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLeastSingleClick()) {
                    return;
                }
                if (null != face) {
                    face.onClickDel(position);
                    face.onClickUpdate(position);
                }
            }
        });

        tvewOrderReceiving.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMsg.showToast(mContext, "请等待卖家发货");
            }
        });

        tvewOrderReceiver.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLeastSingleClick()) {
                    return;
                }
                if (null != face) {
                    face.onClickReceiver(position);
                    face.onClickUpdate(position);
                }
            }
        });
        return convertView;
    }

    CallBackInteface face;

    public void setCallBackInteface(CallBackInteface cbif) {
        this.face = cbif;
    }

    public interface CallBackInteface {

        public void onClickSubmit(int position);

        public void onClickCancel(int position);

        public void onClickDel(int position);

        public void onClickReceiver(int position);

        public void onClickUpdate(int position);

    }
}