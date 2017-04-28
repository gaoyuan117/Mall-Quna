package com.server.api.service;

import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.google.gson.annotations.SerializedName;


public class ReviewService {
    /**
     * 添加评价
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Comment/postcomment", method = Method.Post, encoder = WebFormEncoder.class)
    public static class AddReviewRequest extends Endpoint {

        @SerializedName("order_id")
        public String OrderId;

        @SerializedName("product_id")
        public String ProductId;

        @SerializedName("content")
        public String Content;

        @SerializedName("rate")
        public int Rate;

        @SerializedName("rating_desc")
        public int RatingDesc;

        @SerializedName("rating_attitude")
        public int RatingAttitude;

        @SerializedName("rating_speed")
        public int RatingSpeed;

        @SerializedName("rating_shipping")
        public int RatingShipping;

        @SerializedName("cover_id[]")
        public String[] CoverIds;

    }

    /**
     * 追加评价
     */
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Comment/moreComment", method = Method.Post, encoder = WebFormEncoder.class)
    public static class MoreCommentRequest extends Endpoint {

        @SerializedName("order_id")
        public String OrderId;

        @SerializedName("product_id")
        public String ProductId;

        @SerializedName("content")
        public String Content;

        @SerializedName("cover_id[]")
        public String[] CoverIds;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/comments", method = Method.Post, encoder = WebFormEncoder.class)
    public static class ReviewRequest extends Endpoint {

        @SerializedName("id")
        public String Id;
    }

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Comment/getProductComment", method = Method.Post, encoder = WebFormEncoder.class)
    public static class GetProductReviewRequest extends Endpoint {

        @SerializedName("product_id")
        public String ProductId;

        @SerializedName("rate")
        public int Rate;

        @SerializedName("page")
        public int Page;

        @SerializedName("page_size")
        public int Pagesize;
    }

}
