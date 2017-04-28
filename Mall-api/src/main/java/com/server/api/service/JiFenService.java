package com.server.api.service;

import com.android.zcomponent.communication.http.Context;
import com.android.zcomponent.communication.http.annotation.HttpPortal;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Koterwong on 2016/7/28.
 * <p>
 * 积分商城相关接口
 */
public class JiFenService {

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Ad/init", method = Context.Method.Post,
      encoder = WebFormEncoder.class)
  public static class GetStartAdRequest extends Endpoint {

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/information/informations", method = Context
      .Method.Post, encoder = WebFormEncoder.class)
  public static class EasyPeopleRequest extends Endpoint {

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/information/informationDetail", method =
      Context.Method.Post, encoder = WebFormEncoder.class)
  public static class EasyPeopleDetailRequest extends Endpoint {
    @SerializedName("id")
    public String id;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/getScore", method = Context.Method
      .Post, encoder = WebFormEncoder.class)
  public static class JifenGetUserScoreRequset extends Endpoint {

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/User/giveScore", method = Context.Method
      .Post, encoder = WebFormEncoder.class)
  public static class JifenUserGiveScoreRequset extends Endpoint {
    @SerializedName("uid")
    public String uid;
    @SerializedName("score_num")
    public String score_num;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/quickSubmitOrder", method = Context
      .Method.Post, encoder = WebFormEncoder.class)
  public static class JifenSummitPlatformProductRequest extends Endpoint {

    @SerializedName("id")
    public String id;

    @SerializedName("receiver_id")
    public String receiver_id;

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/buyScoreProduct", method = Context
      .Method.Post, encoder = WebFormEncoder.class)
  public static class JifenPayPlatformProductRequest extends Endpoint {

    @SerializedName("id")
    public String id;
    @SerializedName("receiver_id")
    public String receiver_id;

  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/scorePayOrder", method = Context
      .Method.Post, encoder = WebFormEncoder.class)
  public static class JifenPayProductRequest extends Endpoint {
    @SerializedName("order_id")
    public String order_id;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Product/getProducts", method = Context
      .Method.Post, encoder = WebFormEncoder.class)
  public static class ProductsRequest extends Endpoint {

    @SerializedName("type")
    public String Type;

    @SerializedName("page")
    public int Page;

    @SerializedName("pagesize")
    public int Pagesize;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/merge_order", method = Context.Method
      .Post, encoder = WebFormEncoder.class)
  public static class JifenOrderMergeRequest extends Endpoint {

    @SerializedName("order_ids")
    public String order_ids;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Order/apply_refund", method = Context
      .Method.Post, encoder = WebFormEncoder.class)
  public static class JifenOrderRefundRequest extends Endpoint {
    @SerializedName("order_id")
    public String order_id;

    @SerializedName("refund_desc")
    public String refund_desc;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Shop/ShopScore", method = Context.Method
      .Post, encoder = WebFormEncoder.class)
  public static class JifenShopScoreRequest extends Endpoint {

    @SerializedName("shop_id")
    public String shop_id;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Shop/agree_refund", method = Context.Method
      .Post, encoder = WebFormEncoder.class)
  public static class JifenShopRefundRequest extends Endpoint {

    @SerializedName("order_id")
    public String order_id;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Ad/getList", method = Context.Method
      .Post, encoder = WebFormEncoder.class)
  public static class JifenIndexbannerRequest extends Endpoint {

    @SerializedName("position")
    public String position;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Ad/getList", method = Context.Method
      .Post, encoder = WebFormEncoder.class)
  public static class JifenCategorybannerRequest extends Endpoint {

    @SerializedName("position")
    public String position;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Ad/getdetail", method = Context.Method
      .Post, encoder = WebFormEncoder.class)
  public static class JifenIndexbannerDetailRequest extends Endpoint {

    @SerializedName("id")
    public String id;
  }

  @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/Service/index", method = Context.Method
      .Post, encoder = WebFormEncoder.class)
  public static class jifenServicePhoneRequest extends Endpoint {}
}
