package com.android.juzbao.dao;

import com.android.zcomponent.http.HttpDataLoader;
import com.server.api.model.CommonReturn;
import com.server.api.model.jifenmodel.ServicePhoneResult;
import com.server.api.model.jifenmodel.ShopScore;
import com.server.api.service.JiFenService;

public class JifenDao {

  public static void sendQueryShopScore(HttpDataLoader httpDataLoader, String shop_id) {
    JiFenService.JifenShopScoreRequest request = new JiFenService.JifenShopScoreRequest();
    request.shop_id = shop_id;
    httpDataLoader.doPostProcess(request, ShopScore.class);
  }

  public static void sendOrderRefund(HttpDataLoader httpDataLoader, String order_id) {
    JiFenService.JifenShopRefundRequest request = new JiFenService.JifenShopRefundRequest();
    request.order_id = order_id;
    httpDataLoader.doPostProcess(request, CommonReturn.class);
  }

  public static void sendQueryServicePhone(HttpDataLoader httpDataLoader) {
    JiFenService.jifenServicePhoneRequest request = new JiFenService.jifenServicePhoneRequest();
    httpDataLoader.doPostProcess(request, ServicePhoneResult.class);
  }
}
