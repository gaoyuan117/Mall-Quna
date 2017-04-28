
package com.server.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HomeProduct extends BaseEntity implements Serializable {
    @SerializedName("data")
    public Data Data;

    public static class Data {
        //今日头条商品
        @SerializedName("top_products")
        public HomeItem[] TopProducts;

        //拍真宝商品
        @SerializedName("paipin_products")
        public HomeItem[] PaipinProducts;

        //抢购会商品
        @SerializedName("panic_products")
        public HomeItem[] PanicProducts;

        //选礼物商品
        @SerializedName("gift_products")
        public HomeItem[] GiftProducts;
    }

    public static class HomeItem {

        public String product_id;

        public String title;

        public String type;

        public String slogan_title;

        public String image;
    }
}
