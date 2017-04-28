
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Arrays;

public class Order {

    @SerializedName("id")
    public String order_id;

    public String order_no;

    public String shop_id;

    public String shop_title;

    public String im_account;

    public String status;

    public String create_time;

    @SerializedName("price")
    public BigDecimal total_price;

    public String province_id;

    public String city_id;

    public String area_id;
    //收货人
    public String consignee;
    //收货地址
    public String address;

    public String address_full;

    //收货人电话
    public String mobile;

    public String uid;

    public String status_text;

    public String order_status;

    public String shipping_status;

    public String review_status;

    public String buyer_status_text;

    public String seller_status_text;

    public String is_score_product;

    public String pay_status;

    public String score_num;

    //商品item
    public OrderItem[] products;

    @Override public String toString() {
        return "Order{" +
                "order_id='" + order_id + '\'' +
                ", order_no='" + order_no + '\'' +
                ", shop_id='" + shop_id + '\'' +
                ", shop_title='" + shop_title + '\'' +
                ", im_account='" + im_account + '\'' +
                ", status='" + status + '\'' +
                ", create_time='" + create_time + '\'' +
                ", total_price=" + total_price +
                ", province_id='" + province_id + '\'' +
                ", city_id='" + city_id + '\'' +
                ", consignee='" + consignee + '\'' +
                ", address='" + address + '\'' +
                ", address_full='" + address_full + '\'' +
                ", mobile='" + mobile + '\'' +
                ", uid='" + uid + '\'' +
                ", status_text='" + status_text + '\'' +
                ", order_status='" + order_status + '\'' +
                ", shipping_status='" + shipping_status + '\'' +
                ", review_status='" + review_status + '\'' +
                ", buyer_status_text='" + buyer_status_text + '\'' +
                ", seller_status_text='" + seller_status_text + '\'' +
                ", products=" + Arrays.toString(products) +
                '}';
    }
}
