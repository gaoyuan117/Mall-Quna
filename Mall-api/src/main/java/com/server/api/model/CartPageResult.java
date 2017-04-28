package com.server.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * 查询购物车返回对象
 */
public class CartPageResult extends BaseEntity {
    @SerializedName("data")
    public Data Data;

    public static class Data {
        @SerializedName("total")
        public int Total;

        @SerializedName("result")
        public CartItem[] Result;
    }
}
