
package com.android.juzbao.model;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.juzbao.dao.OrderDao;
import com.android.juzbao.enumerate.OrderStatus;
import com.android.mall.resource.R;
import com.android.zcomponent.http.HttpDataLoader;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.util.ImageLoaderUtil;
import com.android.zcomponent.util.ShowMsg;
import com.android.zcomponent.util.StringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.server.api.model.Address;
import com.server.api.model.CartItem;
import com.server.api.model.Order;
import com.server.api.model.OrderItem;
import com.server.api.service.OrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderBusiness {

    public static boolean addOrder(Context context,
                                   HttpDataLoader httpDataLoader, Address.Data address,
                                   List<CartItem> carts) {
        if (null == address) {
            ShowMsg.showToast(context, "请选择配送地址");
            return false;
        }

        if (null == carts) {
            ShowMsg.showToast(context, "请选择需购买的商品");
            return false;
        }

        List<Integer> listCIds = new ArrayList<Integer>();

        for (int i = 0; i < carts.size(); i++) {
            CartItem.Data[] products = carts.get(i).product;
            for (int j = 0; j < products.length; j++) {
                listCIds.add(Integer.parseInt(products[j].cart_id));
            }
        }

        StringBuilder builder = new StringBuilder();

//        Integer[] arrayCIds = new Integer[listCIds.size()];
        for (int i = 0; i < listCIds.size(); i++) {
            builder.append(listCIds.get(i));
            if (i != listCIds.size() - 1) {
                builder.append(",");
            }
//            arrayCIds[i] = listCIds.get(i);
        }

        OrderService.SubmitOrderRequest request = new OrderService.SubmitOrderRequest();
        request.Cids = builder.toString();
        request.Address = address.id;
        OrderDao.sendCmdQueryAddOrder(httpDataLoader, request);

        return true;
    }

    public static void queryOrders(HttpDataLoader httpDataLoader, int page,
                                   OrderStatus orderStatus) {
        if (null == orderStatus) {
            return;
        }
        String status = null;
        if (orderStatus == OrderStatus.SUBMIT) {
            status = "0";
        } else if (orderStatus == OrderStatus.SUBMITED) {
            status = "1";
        } else if (orderStatus == OrderStatus.REFUND) {
            status = "7";
        }
        OrderDao.sendCmdQueryQueryOrder(httpDataLoader, page, status);
    }

    public static void queryAllOrders(HttpDataLoader httpDataLoader, int page) {
        OrderDao.sendCmdQueryQueryAllOrder(httpDataLoader, page);
    }

    public static void queryOrderDetail(HttpDataLoader httpDataLoader,
                                        String orderId) {
        OrderDao.sendCmdQueryOrderDetail(httpDataLoader, orderId);
    }

    public static void queryConfirmOrder(HttpDataLoader httpDataLoader, String orderId) {
        OrderDao.sendCmdConfirmOrder(httpDataLoader, orderId);
    }

    public static void queryCancelOrder(HttpDataLoader httpDataLoader,
                                        String orderId) {
        OrderDao.sendCmdOrderCancel(httpDataLoader, orderId);
    }

    public static void queryDelOrder(HttpDataLoader httpDataLoader, String order_id) {
        OrderDao.sendCmdOrderDel(httpDataLoader, order_id);
    }

    public static boolean queryRefundOrder(Context context,
                                           HttpDataLoader httpDataLoader, String type, String orderId, String productId,
                                           BigDecimal money, String reason, String explain, String[] coverIds) {
        if (TextUtils.isEmpty(productId)) {
            ShowMsg.showToast(context, "请选择要退款的商品");
            return false;
        }

        if (TextUtils.isEmpty(reason)) {
            ShowMsg.showToast(context, "请输入退款原因");
            return false;
        }

        if (null == money || money.doubleValue() == 0) {
            ShowMsg.showToast(context, "请输入退款金额");
            return false;
        }

        OrderDao.sendCmdOrderRefund(httpDataLoader, type, orderId, productId, money,
                reason, explain, coverIds);
        return true;
    }

    /**
     * 显示地址信息
     *
     * @param order        order
     * @param tvewPersonal tvewPersonal
     * @param tvewPhone    tvewPhone
     * @param tvewAddress  tvewAddress
     */
    public static void showAddressInfo(
            com.server.api.model.Order order,
            TextView tvewPersonal, TextView tvewPhone, TextView tvewAddress) {
        if (null == order) {
            return;
        }
        tvewPersonal.setText("收货人：" + order.consignee);
        tvewPhone.setText(order.mobile);
//        if (null != order.address_full) {
//            tvewAddress.setText(order.address_full);
//        } else {
//            tvewAddress.setText(order.address);
//        }
    }

    public static BigDecimal getTotalIdentifyPrice(OrderItem[] products) {
        BigDecimal identifyPrice = new BigDecimal(0);
        if (null == products) {
            return identifyPrice;
        }
        for (int i = 0; i < products.length; i++) {
            if (null != products[i].identify_price) {
                identifyPrice = identifyPrice.add(products[i].identify_price);
            }
        }

        return StringUtil.formatProgress(identifyPrice);
    }

    public static boolean isAllOrderItemCommented(OrderItem[] products) {
        if (null == products) {
            return true;
        }

        boolean isAllCommented = true;
        for (int j = 0; j < products.length; j++) {
            if ("0".equals(products[j].is_comment)) {
                isAllCommented = false;
            }
        }
        return isAllCommented;
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
            imageLoader.displayImage(Endpoint.HOST + orderItem.image_path,
                    imgvewPhoto, options);
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
            tvewProductAttr.setText(orderItem.product_attr);
            tvewProductName.setText(orderItem.product_title);
            if (StringUtil.formatProgress(orderItem.price).doubleValue() > 0) {
                tvewProductNowPrice.setText("¥"
                        + StringUtil.formatProgress(orderItem.price));
            } else {
                tvewProductNowPrice.setText("¥"
                        + StringUtil.formatProgress(orderItem.unit_price));
            }

            tvewProductNum.setText("x" + orderItem.quantity);

            orderItemParent.addView(view);
        }
    }

    public static void showOrderStateAction(Order order,
                                            TextView tvewOrderReceiving,
                                            TextView tvewOrderReceiver,
                                            TextView tvewOrderComplete,
                                            TextView tvewOrderSubmit,
                                            TextView tvewOrderCancel,
                                            TextView tvewOrderDel,
                                            TextView tvewOrderReview,
                                            TextView tvewOrderPay,
                                            TextView tvewOrderRefund) {
        //查看订单
        tvewOrderComplete.setVisibility(View.GONE);
        tvewOrderCancel.setVisibility(View.GONE);
        tvewOrderReview.setVisibility(View.GONE);
        tvewOrderSubmit.setVisibility(View.GONE);
        tvewOrderDel.setVisibility(View.GONE);
        tvewOrderReceiver.setVisibility(View.GONE);
        tvewOrderReceiving.setVisibility(View.GONE);
        tvewOrderPay.setVisibility(View.GONE);
        tvewOrderRefund.setVisibility(View.GONE);

        if ("待付款".equals(order.status_text)) {
            tvewOrderCancel.setVisibility(View.VISIBLE);
            tvewOrderPay.setVisibility(View.VISIBLE);
        } else if ("交易取消".equals(order.status_text)) {
            tvewOrderComplete.setVisibility(View.VISIBLE);
            tvewOrderDel.setVisibility(View.VISIBLE);
        } else if ("待发货".equals(order.status_text)) {
//            tvewOrderComplete.setVisibility(View.VISIBLE);
            tvewOrderReceiving.setVisibility(View.VISIBLE);
            tvewOrderRefund.setVisibility(View.VISIBLE);
        } else if ("待收货".equals(order.status_text)) {
            tvewOrderReceiver.setVisibility(View.VISIBLE);
            tvewOrderRefund.setVisibility(View.VISIBLE);
        } else if ("待评价".equals(order.status_text)) {
            tvewOrderReview.setVisibility(View.VISIBLE);
        } else if ("交易完成".equals(order.status_text)) {
            tvewOrderDel.setVisibility(View.VISIBLE);
            tvewOrderComplete.setVisibility(View.VISIBLE);
        } else if ("申请退款".equals(order.status_text)) {
            tvewOrderComplete.setVisibility(View.VISIBLE);
            tvewOrderDel.setVisibility(View.VISIBLE);
        } else if ("已退款".equals(order.status_text)) {
            tvewOrderComplete.setVisibility(View.VISIBLE);
            tvewOrderDel.setVisibility(View.VISIBLE);
        }
    }

    public static boolean isAllOrderItemRefunded(com.server.api.model.Order order) {
        if (null == order || null == order.products) {
            return true;
        }

        for (int i = 0; i < order.products.length; i++) {
            if ("0".equals(order.products[i].return_status)) {
                return false;
            }
        }

        return true;
    }

    public static class OrderHelper {

        private int miSelectPosition = -1;

        private LinearLayout mLlayoutOrderItem;

        public void showOrderItem(Context context, LinearLayout layoutOrderItem,
                                  com.server.api.model.Order order) {
            mLlayoutOrderItem = layoutOrderItem;

            DisplayImageOptions options =
                    ImageLoaderUtil
                            .getDisplayImageOptions(R.drawable.img_empty_logo_small);
            OrderBusiness.showOrderItem(context, layoutOrderItem, order.products,
                    ImageLoader.getInstance(), options);
            for (int i = 0; i < layoutOrderItem.getChildCount(); i++) {
                View view = layoutOrderItem.getChildAt(i);
                TextView selectMark =
                        (TextView) view
                                .findViewById(R.id.tvew_product_commented_show);
                if ("1".equals(order.products[i].is_comment)
                        || "1".equals(order.products[i].return_status)) {
                    if ("1".equals(order.products[i].return_status)) {
                        selectMark.setText("已申请退款");
                    } else {
                        selectMark.setText("已评价");
                    }
                    selectMark.setVisibility(View.VISIBLE);
                } else {
                    selectMark.setVisibility(View.GONE);
                    view.setOnClickListener(new OrderItemClickListener(i));
                }
            }
        }

        public int getSelectPosition() {
            return miSelectPosition;
        }

        public class OrderItemClickListener implements OnClickListener {

            private int selectPosition;

            public OrderItemClickListener(int position) {
                selectPosition = position;
            }

            @Override
            public void onClick(View v) {
                miSelectPosition = selectPosition;

                for (int i = 0; i < mLlayoutOrderItem.getChildCount(); i++) {
                    View view = mLlayoutOrderItem.getChildAt(i);
                    ImageView selectMark =
                            (ImageView) view.findViewById(R.id.imgvew_select);
                    if (selectPosition == i) {
                        selectMark.setVisibility(View.VISIBLE);
                    } else {
                        selectMark.setVisibility(View.GONE);
                    }
                }
            }

        }
    }
}
