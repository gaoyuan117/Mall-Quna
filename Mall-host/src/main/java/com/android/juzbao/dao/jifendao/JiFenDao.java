package com.android.juzbao.dao.jifendao;

import android.text.TextUtils;

import com.android.zcomponent.constant.GlobalConst;
import com.android.zcomponent.http.HttpDataLoader;
import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.util.LogEx;
import com.server.api.model.AdProduct;
import com.server.api.model.Products;
import com.server.api.model.jifenmodel.EasyPeople;
import com.server.api.model.jifenmodel.EasyPeopleDetail;
import com.server.api.model.jifenmodel.IndexBanner;
import com.server.api.model.jifenmodel.IndexBannerDetail;
import com.server.api.model.jifenmodel.JifenCommonReturn;
import com.server.api.model.jifenmodel.JifenPayOrderReturn;
import com.server.api.model.jifenmodel.OrderMergeReturn;
import com.server.api.model.jifenmodel.PlatformOrderReturn;
import com.server.api.model.jifenmodel.StartAd;
import com.server.api.model.jifenmodel.UserScore;
import com.server.api.service.JiFenService;
import com.server.api.service.ProductService;

/**
 * 积分相关数据请求
 */
public class JiFenDao {

  public static void sendGetStartAd(HttpDataLoader httpDataLoader) {
    JiFenService.GetStartAdRequest request = new JiFenService.GetStartAdRequest();
    httpDataLoader.doPostProcess(request, StartAd.class);
  }

  public static void sendQueryEasyPeople(HttpDataLoader httpDataLoader) {
    JiFenService.EasyPeopleRequest request = new JiFenService.EasyPeopleRequest();
    httpDataLoader.doPostProcess(request, EasyPeople.class);
  }

  public static void sendQueryEasyPeopleDetail(HttpDataLoader httpDataLoader, String id) {
    JiFenService.EasyPeopleDetailRequest request = new JiFenService.EasyPeopleDetailRequest();
    request.id = id;
    httpDataLoader.doPostProcess(request, EasyPeopleDetail.class);
  }

  public static void sendJiFenGetUserScoreRequest(HttpDataLoader httpDataLoader) {
    JiFenService.JifenGetUserScoreRequset requset = new JiFenService.JifenGetUserScoreRequset();
    httpDataLoader.doPostProcess(requset, UserScore.class);
  }

  public static void sendJiFenGiveScoreRequest(HttpDataLoader httpDataLoader, String uid,
                                               String score_num) {
    JiFenService.JifenUserGiveScoreRequset requset = new JiFenService
        .JifenUserGiveScoreRequset();
    requset.uid = uid;
    requset.score_num = score_num;
    httpDataLoader.doPostProcess(requset, JifenCommonReturn.class);
  }

  public static void sendJiFenSubmitPlatformRequest(HttpDataLoader httpDataLoader, String
      product_id, String receiver_id) {
    JiFenService.JifenSummitPlatformProductRequest request = new JiFenService
        .JifenSummitPlatformProductRequest();
    request.id = product_id;
    request.receiver_id = receiver_id;
    httpDataLoader.doPostProcess(request, PlatformOrderReturn.class);
  }

  public static void snedJifenPayPlatformRequest(HttpDataLoader httpDataLoader, String id,
                                                 String receiver_id) {
    JiFenService.JifenPayPlatformProductRequest request = new JiFenService
        .JifenPayPlatformProductRequest();
    request.id = id;
    request.receiver_id = receiver_id;
    httpDataLoader.doPostProcess(request, PlatformOrderReturn.class);
  }

  public static void sendJiFenPayProductRequest(HttpDataLoader httpDataLoader, String order_id) {
    JiFenService.JifenPayProductRequest request = new JiFenService.JifenPayProductRequest();
    request.order_id = order_id;
    httpDataLoader.doPostProcess(request, JifenPayOrderReturn.class);
  }

  public static void sendQueryScoreProducts(HttpDataLoader httpDataLoader, String mType, int
      pageNum) {
    JiFenService.ProductsRequest request = new JiFenService.ProductsRequest();
    request.Page = pageNum;
    request.Type = mType;
    request.Pagesize = GlobalConst.INT_NUM_PAGE;
    httpDataLoader.doPostProcess(request, Products.class);
  }

  public static void sendOrderRefund(HttpDataLoader httpDataLoader, String order_id, String
      reason) {
    JiFenService.JifenOrderRefundRequest request = new JiFenService.JifenOrderRefundRequest();
    request.order_id = order_id;
    request.refund_desc = reason;
    httpDataLoader.doPostProcess(request, JifenCommonReturn.class);
  }

  public static void sendOrderMergeOrder(HttpDataLoader httpDataLoader, int[] orderIds) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < orderIds.length; i++) {
      builder.append(orderIds[i]);
      if (i != orderIds.length - 1) {
        builder.append(",");
      }
    }
    JiFenService.JifenOrderMergeRequest request = new JiFenService.JifenOrderMergeRequest();
    request.order_ids = builder.toString();
    httpDataLoader.doPostProcess(request, OrderMergeReturn.class);
  }

  public static void snedJifenHomeImg(HttpDataLoader httpDataLoader, String score_show_img) {
    ProductService.AdScoreImgRequest request = new ProductService.AdScoreImgRequest();
    request.Identifier = score_show_img;
    httpDataLoader.doPostProcess(request, AdProduct.class);
  }

  public static void sendQueryIndexBanner(HttpDataLoader httpDataLoader, String index_banner) {
    JiFenService.JifenIndexbannerRequest request = new JiFenService.JifenIndexbannerRequest();
    request.position = index_banner;
    httpDataLoader.doPostProcess(request, IndexBanner.class);
  }

  public static void sendQueryCategoryBanner(HttpDataLoader httpDataLoader, String categoryBanner) {
    JiFenService.JifenCategorybannerRequest request = new JiFenService.JifenCategorybannerRequest();
    request.position = categoryBanner;
    httpDataLoader.doPostProcess(request, IndexBanner.class);
  }

  public static void sendQuertIndexBannerDetail(HttpDataLoader httpDataLoader, String id) {
    JiFenService.JifenIndexbannerDetailRequest request = new JiFenService.JifenIndexbannerDetailRequest();
    request.id = id;
    httpDataLoader.doPostProcess(request, IndexBannerDetail.class);
  }

  public static String getHTML(String des) {
    String result = "";
    String[] temp_arr = des.split("<img src=\"");
    if (temp_arr.length > 1) {
      for (int i = 0; i < temp_arr.length; i++) {
        if (i == 0) {
          continue;
        }
        String http_str = temp_arr[i].substring(0, 4);
        if (!http_str.equals("http")) {
          temp_arr[i] = "<img src=\"" + Endpoint.HOST + temp_arr[i];
        } else {
          temp_arr[i] = "<img src=\"" + temp_arr[i];
        }
      }
      StringBuilder builder = new StringBuilder();
      for (String str : temp_arr) {
        builder = builder.append(str);
      }
      result = builder.toString();
    }
    LogEx.d("http", result);
    return TextUtils.isEmpty(result) ? des : result;
  }
}
