
package com.android.juzbao.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.juzbao.activity.MyOrderDetailActivity_;
import com.android.juzbao.activity.SendGoodsActivity;
import com.android.juzbao.model.ProviderOrderBusiness;
import com.android.juzbao.provider.R;
import com.android.zcomponent.adapter.CommonAdapter;
import com.android.zcomponent.json.JsonSerializerFactory;
import com.android.zcomponent.util.CommonUtil;
import com.android.zcomponent.util.StringUtil;
import com.server.api.model.Order;
import com.server.api.model.OrderItem;

import java.util.List;

/**
 * Description: 我的购买订单列表项（商家）
 */
public class OrderAdapter extends CommonAdapter {

    public OrderAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.adapter_order, null);
        }

        LinearLayout llayoutOrderItem = findViewById(convertView, R.id.llayout_order_item);

        TextView tvewShopName = findViewById(convertView, R.id.tvew_order_name_show);
        TextView tvewOrderPrice = findViewById(convertView, R.id.tvew_total_price_show);
        TextView tvewOrderStatus = findViewById(convertView, R.id.tvew_order_status_show);

        TextView tvewOrderPay = findViewById(convertView, R.id.tvew_order_pay_show_click);
        TextView tvewOrderSend = findViewById(convertView, R.id.tvew_order_send_show_click);
        TextView tvewOrderReceiving = findViewById(convertView, R.id
                .tvew_order_receiving_show_click);
        TextView tvewOrderReceived = findViewById(convertView, R.id.tvew_order_received_show_click);
        TextView tvewOrderRefund = findViewById(convertView, R.id.tvew_order_refund_show_click);
        TextView tvewOrderRefuseRefund = findViewById(convertView, R.id
                .tvew_order_refuse_refund_show_click);
        TextView tvewOrderDelete = findViewById(convertView, R.id.tvew_order_del_show_click);
        TextView tvewOrderDetail = findViewById(convertView, R.id.tvew_order_detail_show_click);
        TextView tvewOrderReview = findViewById(convertView, R.id.tvew_order_review_show_click);

        final Order order = (Order) mList.get(position);

        OrderItem[] orderItems = order.products;

        if (null != orderItems && orderItems.length > 0) {
            tvewShopName.setText(order.products[0].shop_title);
        }

        tvewOrderStatus.setText(order.status_text);
        tvewOrderPrice.setText("合计：¥" + StringUtil.formatProgress(order.total_price));

        ProviderOrderBusiness.showOrderItem(mContext, llayoutOrderItem, orderItems, mImageLoader,
                options);

        ProviderOrderBusiness.showProviderOrderStateAction(order,
                tvewOrderPay,
                tvewOrderSend,
                tvewOrderReceiving,
                tvewOrderReceived,
                tvewOrderRefund,
                tvewOrderRefuseRefund,
                tvewOrderDelete,
                tvewOrderDetail,
                tvewOrderReview);

        tvewOrderRefund.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!CommonUtil.isLeastSingleClick()) {
                    return;
                }
                if (null != face) {
                    face.onClickUpdate(position);
                }
                if (null != face) {
                    face.onClickRefund(position);
                }
            }
        });

        tvewOrderRefuseRefund.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!CommonUtil.isLeastSingleClick()) {
                    return;
                }
                if (null != face) {
                    face.onClickUpdate(position);
                }
                if (null != face) {
                    face.onClickRefuseRefund(position);
                }
            }
        });

//        //查看物流
//        tvewOrderDelivery.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if (!CommonUtil.isLeastSingleClick()) {
//                    return;
//                }
//                if (null != face) {
//                    face.onClickUpdate(position);
//                }
//
//                ProviderOrderBusiness.intentToExpressDetailActivity(mContext,
//                        order.order_id, order.products[0].image_path);
//            }
//        });

        tvewOrderDetail.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if (!CommonUtil.isLeastSingleClick()) {
                    return;
                }
                if (null != face) {
                    face.onClickUpdate(position);
                }
                Bundle bundle = new Bundle();
                bundle.putString("order", JsonSerializerFactory.Create().encode(order));
                intentToActivity(bundle, MyOrderDetailActivity_.class);
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
                bundle.putBoolean("isSaler", true);
                ProviderOrderBusiness.intentToReadReviewActivity(mContext, bundle);
            }
        });

        //发货
        tvewOrderSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!CommonUtil.isLeastSingleClick()) {
                    return;
                }
                if (null != face) {
                    face.onClickUpdate(position);
                }
//                ProviderOrderBusiness.intentToSubmitExpressActivity(mContext, order.order_id);
                Bundle bundle = new Bundle();
                bundle.putString("order", JsonSerializerFactory.Create().encode(order));
                intentToActivity(bundle, SendGoodsActivity.class);
            }
        });

        tvewOrderDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!CommonUtil.isLeastSingleClick()) {
                    return;
                }
                if (null != face) {
                    face.onClickUpdate(position);
                }
                if (null != face) {
                    face.onClickDelete(position);
                }
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
                intentToActivity(bundle, MyOrderDetailActivity_.class);
            }
        });
        return convertView;
    }

    CallBackInteface face;

    public void setCallBackInteface(CallBackInteface cbif) {
        this.face = cbif;
    }

    public interface CallBackInteface {

        public void onClickUpdate(int position);

        public void onClickDelete(int position);

        public void onClickRefund(int position);

        public void onClickRefuseRefund(int position);

    }
}