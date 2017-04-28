package com.server.api.model;

import java.util.Arrays;

/**
 * Created by Koterwong on 2016/7/21.
 */

public class ReviewOrderItem {

    public String id;

    public String order_id;

    public String product_id;

    public String create_time;

    public String uid;

    public String content;

    public String cover_ids;

    //宝贝描述
    public String rating_desc;

    //服务态度
    public String rating_attitude;

    //发货速度
    public String rating_speed;

    //物流服务
    public String rating_shipping;

    public String rate;

    public String avatar;

    public String username;

    public String format_time;

    public String[] images;

    public ReviewOrderMsgItem[] messages;


    @Override public String toString() {
        return "ReviewOrderItem{" +
                "id='" + id + '\'' +
                ", order_id='" + order_id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", create_time='" + create_time + '\'' +
                ", uid='" + uid + '\'' +
                ", content='" + content + '\'' +
                ", cover_ids='" + cover_ids + '\'' +
                ", rating_desc='" + rating_desc + '\'' +
                ", rating_attitude='" + rating_attitude + '\'' +
                ", rating_speed='" + rating_speed + '\'' +
                ", rating_shipping='" + rating_shipping + '\'' +
                ", rate='" + rate + '\'' +
                ", avatar='" + avatar + '\'' +
                ", username='" + username + '\'' +
                ", format_time='" + format_time + '\'' +
                ", images=" + Arrays.toString(images) +
                ", messages=" + Arrays.toString(messages) +
                '}';
    }
}
