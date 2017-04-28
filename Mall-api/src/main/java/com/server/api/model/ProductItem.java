
package com.server.api.model;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class ProductItem implements Serializable {

    public String id;

    public String title;

    public BigDecimal price;

    public String now_price;

    public BigDecimal maxprice;

    public BigDecimal price_start;

    public BigDecimal option_price;

    public BigDecimal cash_deposit;

    public BigDecimal identify_price;

    public String activity_short_desc;

    public String activity_title;

    public String auction_count;

    public String type;

    public String view;

    public String image;

    public String image_path;

    public String uid;

    public String shop_id;

    public String shop_title;

    public String category_id;

    public String category_title;

    public String address;

    public String address_full;

    public String province_id;

    public String city_id;

    public String area_id;

    public String description;

    public String quantity;

    public String sales;

    public String status;

    public String im_account;

    public String status_text;

    public String share_href;

    public Movie movie;

    /**
     * 支持7天无理由退货，[1:是, 0:否]
     */
    public String security_7days;

    /**
     * 0:否，1:24小时，2:48小时，3:72小时
     */
    public String security_delivery;

    public String in_special_panic;

    public String in_special_gift;

    public String is_auaction_permission;

    public Image[] images;

    public String start_time;

    public String end_time;

    public SpecialPanic[] special_panic;

    //新增商品赠送商品数量
    public String score;

    //新增商家剩余积分可以卖出多少件商品
    public int allow_quantity;

    //平台商品需要多少积分兑换。
    public String use_score;
}
